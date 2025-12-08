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
  public enum State
  {
    FIGHT,
    PREPARE,
    LOBBY,
    END
  }
  
  final String uuid;
  
  final int PLAYERS_COUNT = 8;
  final int FIGHTS_COUNT = 4;
  final List<Fight> fights = new ArrayList<>();
  
  public State state = State.LOBBY;
  
  ExecutorService executor = Executors.newFixedThreadPool(FIGHTS_COUNT);
  Map<String, Player> players;
  int turn = 1;
  
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
  
  public void sendPreFightTimer(long ms)
  {
    players.forEach((key, player) -> player.getSender().send(new Event(uuid, key, Event.Type.PRE_FIGHT_TIMER).getBytes()));
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
    player.moveTable(index, index2);
  }
  
  public void freezeTavern(String uuid)
  {
    Player player = getPlayer(uuid);
    player.freezeTavern();
  }
  
  private Player getPlayer(String uuid)
  {
    return players.get(uuid);
  }
  
  public void doPreFight()
  {
    state = State.PREPARE;
    
    for (Player player : players.values())
    {
      player.resetMoney();
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
        
        while (true)
        {
          if (fight.doTurn())
          {
            break;
          }
        }
        
        processStartFight(player, player2);
        
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

    // TODO:Калькуляция битв
    // Способ добавить новый файт это сделать fights.add(new Fight(player, player2))
    // Уникальный идентификатор пользователя player.getUUID()

    return false;
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
}
