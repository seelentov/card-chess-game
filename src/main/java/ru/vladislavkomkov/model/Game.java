package ru.vladislavkomkov.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import ru.vladislavkomkov.controller.GameProcessor;
import ru.vladislavkomkov.controller.sender.Sender;
import ru.vladislavkomkov.model.event.Event;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.fight.FightEvent;
import ru.vladislavkomkov.model.fight.FightInfo;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.ListenerUtils;
import ru.vladislavkomkov.util.RandUtils;
import ru.vladislavkomkov.util.UUIDUtils;

public class Game implements AutoCloseable
{
  public static final int PLAYERS_COUNT = 8;
  public static final int FIGHTS_COUNT = 4;
  
  final String uuid;
  final ExecutorService executor = Executors.newFixedThreadPool(FIGHTS_COUNT);
  List<Fight> fights = new ArrayList<>();
  final List<FightInfo> fightHistory = new CopyOnWriteArrayList<>();
  
  final Map<String, Player> players;
  private final ReadWriteLock playersLock = new ReentrantReadWriteLock();
  private final boolean acceptingNewPlayers;
  
  State state = State.LOBBY;
  int turn = 1;
  
  public Game()
  {
    this(new HashMap<>(), UUIDUtils.generateKey(), true);
  }
  
  public Game(Map<String, Player> players)
  {
    this(players, UUIDUtils.generateKey(), true);
  }
  
  public Game(String uuid)
  {
    this(new HashMap<>(), uuid, true);
  }
  
  public Game(Map<String, Player> players, String uuid)
  {
    this(players, uuid, true);
  }
  
  private Game(Map<String, Player> players, String uuid, boolean acceptingNewPlayers)
  {
    this.players = players;
    this.uuid = uuid;
    this.acceptingNewPlayers = acceptingNewPlayers;
  }
  
  public Map<String, Player> getPlayers()
  {
    playersLock.readLock().lock();
    try
    {
      return new HashMap<>(players);
    }
    finally
    {
      playersLock.readLock().unlock();
    }
  }
  
  public String addPlayer()
  {
    playersLock.writeLock().lock();
    try
    {
      if (!acceptingNewPlayers)
      {
        throw new IllegalStateException("Cannot add player: game already started");
      }
      String key = UUIDUtils.generateKey();
      addPlayerInternal(key, new Player(key, this));
      return key;
    }
    finally
    {
      playersLock.writeLock().unlock();
    }
  }
  
  public void addPlayer(String UUID, Player player)
  {
    playersLock.writeLock().lock();
    try
    {
      if (!acceptingNewPlayers)
      {
        throw new IllegalStateException("Cannot add player: game already started");
      }
      addPlayerInternal(UUID, player);
    }
    finally
    {
      playersLock.writeLock().unlock();
    }
  }
  
  private void addPlayerInternal(String UUID, Player player)
  {
    players.put(UUID, player);
  }
  
  public void removePlayer(String key)
  {
    Player player;
    playersLock.writeLock().lock();
    try
    {
      player = players.remove(key);
      if (player == null)
        return;
    }
    finally
    {
      playersLock.writeLock().unlock();
    }
    player.sendMessage(Event.Type.DISCONNECTED);
  }
  
  public void setPlayerSender(String playerUUID, Sender sender)
  {
    Player player;
    playersLock.readLock().lock();
    try
    {
      player = players.get(playerUUID);
      if (player == null)
      {
        throw new IllegalArgumentException("Player not found: " + playerUUID);
      }
    }
    finally
    {
      playersLock.readLock().unlock();
    }
    player.setSender(sender);
    player.sendMessage(Event.Type.CONNECTED);
    player.sendFullStat();
    if (getState() != State.LOBBY)
    {
      player.sendMessage(Event.Type.START);
    }
  }
  
  public void startGame(GameProcessor processor)
  {
    playersLock.writeLock().lock();
    try
    {
      if (state != State.LOBBY)
      {
        throw new IllegalStateException("Game already started");
      }
      if (players.size() % 2 != 0)
      {
        throw new IllegalStateException("Users count not % 2");
      }
      
      processor.start(getUUID());
    }
    finally
    {
      playersLock.writeLock().unlock();
    }
  }
  
  public void sendPreFightTimer(int ms)
  {
    playersLock.readLock().lock();
    try
    {
      players.forEach((key, player) -> player.sendMessage(Event.Type.PRE_FIGHT_TIMER, ms));
    }
    finally
    {
      playersLock.readLock().unlock();
    }
  }
  
  public void sendStartGame()
  {
    playersLock.readLock().lock();
    try
    {
      players.forEach((key, player) -> player.sendMessage(Event.Type.START));
    }
    finally
    {
      playersLock.readLock().unlock();
    }
  }
  
  public void doTurnBegin()
  {
    playersLock.readLock().lock();
    try
    {
      players.values().forEach(Player::sendFullStat);
      clearSenderWaiters();
      state = State.PREPARE;
      
      for (Player player : players.values())
      {
        if (turn != 1)
        {
          if (player.getMaxMoneyBase() < Player.MAX_MONEY)
          {
            player.incMaxMoney();
          }
          player.getStatistic().counters.incrementIncLevelDecreaser();
        }
        player.resetMoney();
        player.resetTavern(true);
        processStartTurn(player);
        player.sendLvlIncPrice();
      }
    }
    finally
    {
      playersLock.readLock().unlock();
    }
  }
  
  public void doTurnEnd()
  {
    playersLock.readLock().lock();
    try
    {
      for (Player player : players.values())
      {
        processEndTurn(player);
      }
    }
    finally
    {
      playersLock.readLock().unlock();
    }
  }
  
  public void doFight()
  {
    playersLock.readLock().lock();
    try
    {
      state = State.FIGHT;
      
      List<CompletableFuture<?>> fightFutures = new ArrayList<>();
      
      for (Fight fight : fights)
      {
        fightFutures.add(CompletableFuture.supplyAsync(() -> {
          fight.setup();
          
          boolean isPlayerFirst = RandUtils.getRand(1) == 0;
          Player player = isPlayerFirst ? fight.getPlayer1() : fight.getPlayer2();
          Player player2 = isPlayerFirst ? fight.getPlayer2() : fight.getPlayer1();
          
          processStartFight(fight, player, player2);
          processStartFight(fight, player2, player);
          
          Optional<FightInfo> result;
          
          do
          {
            result = fight.doTurn();
          }
          while (result.isEmpty());
          
          processEndFight(fight, player, player2);
          processEndFight(fight, player2, player);
          
          fight.addToHistory(FightEvent.Type.END, player, null);
          fightHistory.add(result.get());
          
          FightInfo info = result.get();
          player.sendMessage(Event.Type.FIGHT, info);
          player2.sendMessage(Event.Type.FIGHT, info);
          
          return null;
        }, executor));
      }
      
      CompletableFuture<Void> allOf = CompletableFuture.allOf(
          fightFutures.toArray(new CompletableFuture[0]));
      allOf.join();
    }
    finally
    {
      playersLock.readLock().unlock();
    }
  }
  
  public void setFights(List<Fight> fights)
  {
    this.fights = fights;
  }
  
  public boolean calcFights()
  {
    playersLock.readLock().lock();
    try
    {
      Queue<Player> alive = newPlayerQueue(true);
      Queue<Player> dead = newPlayerQueue(false);
      
      if (alive.size() < 2)
      {
        Player player = alive.peek();
        if (player != null)
        {
          player.sendMessage(Event.Type.WIN);
          state = State.END;
        }
        return true;
      }
      
      fights.clear();
      
      List<Player> playersList = new ArrayList<>(alive);
      Map<String, Map<String, Integer>> fightCounts = new HashMap<>();
      
      for (Player player : playersList)
      {
        fightCounts.put(player.getUUID(), new HashMap<>());
      }
      
      for (FightInfo fightInfo : fightHistory)
      {
        String p1 = fightInfo.player1.getUUID();
        String p2 = fightInfo.player2.getUUID();
        fightCounts.get(p1).merge(p2, 1, Integer::sum);
        fightCounts.get(p2).merge(p1, 1, Integer::sum);
      }
      
      List<Player> availablePlayers = new ArrayList<>(playersList);
      Collections.shuffle(availablePlayers);
      
      // Если нечётное число живых — добавляем одного мёртвого (если есть)
      if (availablePlayers.size() % 2 != 0 && !dead.isEmpty())
      {
        Player dummyOpponent = dead.poll(); // берём любого мёртвого
        availablePlayers.add(dummyOpponent);
      }
      
      while (availablePlayers.size() >= 2)
      {
        Player player1 = availablePlayers.remove(0);
        Player player2 = findOpponent(player1, availablePlayers, fightCounts, playersList);
        
        if (player2 != null)
        {
          availablePlayers.remove(player2);
          fights.add(new Fight(this, player1, player2, false));
        }
        else if (!availablePlayers.isEmpty())
        {
          player2 = availablePlayers.remove(0);
          fights.add(new Fight(this, player1, player2, false));
        }
      }
      
      long aliveCount = players.values().stream().filter(Player::isAlive).count();
      if (aliveCount <= 1)
      {
        Player winner = players.values().stream().filter(Player::isAlive).findFirst().orElse(null);
        if (winner != null)
        {
          winner.sendMessage(Event.Type.WIN);
        }
        state = State.END;
        return true;
      }
      
      return false;
    }
    finally
    {
      playersLock.readLock().unlock();
    }
  }
  
  public void doSenderWaiter(String uuid, String key, Integer param)
  {
    Player player = getPlayer(uuid);
    if (player != null)
      player.doSenderWaiter(key, param);
  }
  
  public void buyTavernCard(String uuid, int index)
  {
    Player player = getPlayer(uuid);
    if (player != null)
      player.buyCard(index);
  }
  
  public void playCard(String uuid, int indexCard, List<Integer> input)
  {
    Player player = getPlayer(uuid);
    if (player != null)
      player.playCard(indexCard, input);
  }
  
  public void sellCard(String uuid, int index)
  {
    Player player = getPlayer(uuid);
    if (player != null)
      player.sellCard(index);
  }
  
  public void lvlUp(String uuid)
  {
    Player player = getPlayer(uuid);
    if (player != null)
      player.incLevel();
  }
  
  public void resetTavern(String uuid)
  {
    Player player = getPlayer(uuid);
    if (player != null)
      player.resetTavernManual();
  }
  
  public void moveTable(String uuid, int index, int index2)
  {
    Player player = getPlayer(uuid);
    if (player != null)
      player.moveTable(index, index2);
  }
  
  public void freezeTavern(String uuid)
  {
    Player player = getPlayer(uuid);
    if (player != null)
      player.freezeTavern();
  }
  
  private Player getPlayer(String uuid)
  {
    playersLock.readLock().lock();
    try
    {
      return players.get(uuid);
    }
    finally
    {
      playersLock.readLock().unlock();
    }
  }
  
  public void processStartTurn(Player player)
  {
    ListenerUtils.processGlobalActionListeners(
        player.getListener().onStartTurnListeners, this, null, player);
    player.doForAll(unit -> unit.onStartTurn(this, null, player));
    player.removeTempBuffs();
    player.calcTriplets();
  }
  
  public void processEndTurn(Player player)
  {
    ListenerUtils.processGlobalActionListeners(
        player.getListener().onEndTurnListeners, this, null, player);
    player.clearSpellCraft();
    player.doForAll(unit -> unit.onEndTurn(this, null, player));
  }
  
  public void processStartFight(Fight fight, Player player, Player player2)
  {
    ListenerUtils.processFightActionListeners(
        ListenerUtils.getPlayerListener(fight, player).onStartFightListeners, this, fight, player, player2);
    fight.getFightTable(player).forEach(unit -> unit.onStartFight(this, fight, player, player2));
  }
  
  public void processEndFight(Fight fight, Player player, Player player2)
  {
    ListenerUtils.processFightActionListeners(
        ListenerUtils.getPlayerListener(fight, player).onEndFightListeners, this, fight, player, player2);
    fight.getFightTable(player).forEach(unit -> unit.onEndFight(this, fight, player, player2));
  }
  
  public void clearSenderWaiters()
  {
    playersLock.readLock().lock();
    try
    {
      players.values().forEach(Player::clearSenderWaiters);
      players.values().forEach(player -> player.sendMessage(Event.Type.CLEAR_WAITERS));
    }
    finally
    {
      playersLock.readLock().unlock();
    }
  }
  
  private Player findOpponent(
      Player player,
      List<Player> availablePlayers,
      Map<String, Map<String, Integer>> fightCounts,
      List<Player> allPlayers)
  {
    
    String playerId = player.getUUID();
    Map<String, Integer> playerFights = fightCounts.get(playerId);
    
    boolean foughtWithAll = true;
    for (Player other : allPlayers)
    {
      if (playerFights.getOrDefault(other.getUUID(), 0) == 0)
      {
        foughtWithAll = false;
        break;
      }
    }
    
    if (!foughtWithAll)
    {
      for (Player opponent : availablePlayers)
      {
        if (playerFights.getOrDefault(opponent.getUUID(), 0) == 0)
        {
          return opponent;
        }
      }
    }
    
    if (!availablePlayers.isEmpty())
    {
      Player bestOpponent = null;
      int minFights = Integer.MAX_VALUE;
      for (Player opponent : availablePlayers)
      {
        int fightsWithOpponent = playerFights.getOrDefault(opponent.getUUID(), 0);
        if (fightsWithOpponent < minFights)
        {
          minFights = fightsWithOpponent;
          bestOpponent = opponent;
        }
      }
      return bestOpponent;
    }
    
    return null;
  }
  
  private Queue<Player> newPlayerQueue(boolean isAlive)
  {
    // Вызывается ТОЛЬКО под read/write lock
    return players.values().stream()
        .filter(player -> isAlive == player.isAlive())
        .collect(Collectors.toCollection(ArrayDeque::new));
  }
  
  public String getUUID()
  {
    return uuid;
  }
  
  public State getState()
  {
    return state;
  }
  
  public int getTurn()
  {
    return turn;
  }
  
  public void incTurn()
  {
    turn++;
  }
  
  @Override
  public void close() throws Exception
  {
    executor.shutdown();
  }
  
  public enum State
  {
    FIGHT,
    PREPARE,
    LOBBY,
    END
  }
}