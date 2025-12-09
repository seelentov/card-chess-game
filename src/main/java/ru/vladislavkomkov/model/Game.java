package ru.vladislavkomkov.model;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import ru.vladislavkomkov.controller.sender.Sender;
import ru.vladislavkomkov.model.event.Event;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.ListenerUtils;
import ru.vladislavkomkov.util.RandUtils;
import ru.vladislavkomkov.util.UUIDUtils;

public class Game implements AutoCloseable, Serializable
{
  final String uuid;
  final int PLAYERS_COUNT = 8;
  final int FIGHTS_COUNT = 4;
  final List<Fight> fights = new ArrayList<>();
  final List<Fight.Info> fightHistory = new ArrayList<>();
  public State state = State.LOBBY;
  ExecutorService executor = Executors.newFixedThreadPool(FIGHTS_COUNT);
  Map<String, Player> players;
  int turn = 1;
  public Game()
  {
    this(new HashMap<>(), UUIDUtils.generateKey());
  }
  
  public Game(Map<String, Player> players)
  {
    this(players, UUIDUtils.generateKey());
  }
  
  public Game(String uuid)
  {
    this(new HashMap<>(), uuid);
  }
  
  public Game(Map<String, Player> players, String uuid)
  {
    this.players = players;
    this.uuid = uuid;
  }
  
  public void setPlayerSender(String playerUUID, Sender sender)
  {
    players.get(playerUUID).setSender(sender);
    sender.send(new Event(
        uuid,
        playerUUID,
        Event.Type.CONNECTED).getBytes());
  }
  
  public void sendPreFightTimer(int ms)
  {
    players.forEach((key, player) -> player.getSender().send(new Event(uuid, key, Event.Type.PRE_FIGHT_TIMER, ms).getBytes()));
  }
  
  public void sendStartGame()
  {
    players.forEach((key, player) -> player.getSender().send(new Event(uuid, key, Event.Type.START).getBytes()));
  }
  
  public void doSenderWaiter(String uuid, String key, Integer param)
  {
    Player player = getPlayer(uuid);
    player.doSenderWaiter(key, param);
  }
  
  public void buyTavernCard(String uuid, int index)
  {
    Player player = getPlayer(uuid);
    player.buyCard(this, index);
  }
  
  public void playCard(String uuid, int indexCard, int indexCast, boolean isTavernCast, int indexCast2, boolean isTavernCast2)
  {
    Player player = getPlayer(uuid);
    player.playCard(this, indexCard, indexCast, isTavernCast, indexCast2, isTavernCast2);
  }
  
  public void sellCard(String uuid, int index)
  {
    Player player = getPlayer(uuid);
    player.sellCard(this, index);
  }
  
  public void lvlUp(String uuid)
  {
    Player player = getPlayer(uuid);
    player.incLevel(this);
  }
  
  public void resetTavern(String uuid)
  {
    Player player = getPlayer(uuid);
    player.resetTavernManual(this);
  }
  
  public void moveTable(String uuid, int index, int index2)
  {
    Player player = getPlayer(uuid);
    player.moveTable(this, index, index2);
  }
  
  public void freezeTavern(String uuid)
  {
    Player player = getPlayer(uuid);
    player.freezeTavern(this);
  }
  
  private Player getPlayer(String uuid)
  {
    return players.get(uuid);
  }
  
  private void clearSenderWaiters()
  {
    players.values().forEach(Player::clearSenderWaiters);
    players.values().forEach(player -> player.getSender().send(new Event(getUUID(), player.getUUID(), Event.Type.CLEAR_WAITERS).getBytes()));
  }
  
  public void doPreFight()
  {
    clearSenderWaiters();
    state = State.PREPARE;

    for (Player player : players.values())
    {
      player.resetMoney(this);
      player.resetTavern(this);
      processStartTurn(player);
      processEndTurn(player);
    }
  }
  
  public void processStartTurn(Player player)
  {
    ListenerUtils.processGlobalActionListeners(player.listener.onStartTurnListeners, this, player);

    player.doForAll(unit -> unit.onStartTurn(this, player));
    player.removeTempBuffs();
    player.calcTriplets();
  }
  
  public void processEndTurn(Player player)
  {
    ListenerUtils.processGlobalActionListeners(player.listener.onEndTurnListeners, this, player);
    player.clearSpellCraft();

    player.doForAll(unit -> unit.onEndTurn(this, player));
  }
  
  public void processStartFight(Player player, Player player2)
  {
    ListenerUtils.processGlobalActionListeners(player.listener.onStartFightListeners, this, player);

    player.doForAll(unit -> unit.onStartFight(this, player, player2));
  }
  
  public void processEndFight(Player player, Player player2)
  {
    ListenerUtils.processGlobalActionListeners(player.listener.onEndFightListeners, this, player);

    player.doForAll(unit -> unit.onEndFight(this, player, player2));
  }
  
  public void doFight()
  {
    state = State.FIGHT;

    List<CompletableFuture<?>> fightFutures = new ArrayList<>();

    for (Fight fight : fights)
    {
      fightFutures.add(CompletableFuture.supplyAsync(() -> {
        boolean isPlayerFirst = RandUtils.getRand(1) == 0;

        Player player = isPlayerFirst ? fight.getPlayer1() : fight.getPlayer2();
        Player player2 = isPlayerFirst ? fight.getPlayer2() : fight.getPlayer1();

        processStartFight(player, player2);

        Optional<Fight.Info> result;
        do
        {
          result = fight.doTurn();
        }
        while (result.isEmpty());

        fightHistory.add(result.get());
        processEndFight(player, player2);

        return null;
      }, executor));
    }

    CompletableFuture<Void> allOf = CompletableFuture.allOf(
        fightFutures.toArray(new CompletableFuture[0]));

    allOf.join();
  }
  
  public int getTurn()
  {
    return turn;
  }
  
  public void incTurn()
  {
    turn++;
  }
  
  public boolean calcFights()
  {
    Queue<Player> alive = newPlayerQueue(true);
    Queue<Player> dead = newPlayerQueue(false);

    if (alive.size() < 2)
    {
      Player player = alive.peek();

      if (player != null)
      {
        player.getSender().send(new Event(
            uuid,
            player.getUUID(),
            Event.Type.WIN).getBytes());
      }

      return true;
    }

    fights.clear();

    List<Player> players = new ArrayList<>(alive);

    Map<String, Map<String, Integer>> fightCounts = new HashMap<>();

    for (Player player : players)
    {
      fightCounts.put(player.getUUID(), new HashMap<>());
    }

    for (Fight.Info fightInfo : fightHistory)
    {
      String p1 = fightInfo.player1.getUUID();
      String p2 = fightInfo.player2.getUUID();

      fightCounts.get(p1).put(p2, fightCounts.get(p1).getOrDefault(p2, 0) + 1);
      fightCounts.get(p2).put(p1, fightCounts.get(p2).getOrDefault(p1, 0) + 1);
    }

    List<Player> availablePlayers = new ArrayList<>(players);
    Collections.shuffle(availablePlayers);

    while (availablePlayers.size() >= 2)
    {
      Player player1 = availablePlayers.remove(0);

      Player player2 = findOpponent(player1, availablePlayers, fightCounts, players);

      if (player2 != null)
      {
        availablePlayers.remove(player2);
        fights.add(new Fight(this, player1, player2));
      }
      else if (!availablePlayers.isEmpty())
      {
        player2 = availablePlayers.remove(0);
        fights.add(new Fight(this, player1, player2));
      }
    }

    if (!availablePlayers.isEmpty())
    {
      Player lonelyPlayer = availablePlayers.get(0);
      lonelyPlayer.getSender().send(new Event(
          uuid,
          lonelyPlayer.getUUID(),
          Event.Type.WIN).getBytes());
    }

    return false;
  }
  
  private Player findOpponent(Player player, List<Player> availablePlayers,
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
    return players.values().stream().filter(player -> isAlive == player.isAlive()).collect(Collectors.toCollection(ArrayDeque::new));
  }
  
  public String addPlayer()
  {
    String key = UUIDUtils.generateKey();
    players.put(key, new Player(key));
    return key;
  }
  
  public void removePlayer(String key)
  {
    players.remove(key);
  }
  
  @Override
  public void close() throws Exception
  {
    executor.shutdown();
  }
  
  public String getUUID()
  {
    return uuid;
  }
  
  public enum State
  {
    FIGHT,
    PREPARE,
    LOBBY,
    END
  }
}
