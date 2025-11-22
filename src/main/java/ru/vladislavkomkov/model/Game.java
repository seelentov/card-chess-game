package ru.vladislavkomkov.model;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.RandUtils;
import ru.vladislavkomkov.util.UUIDUtils;

public class Game implements AutoCloseable, Serializable
{
  final int PLAYERS_COUNT = 8;
  final int FIGHTS_COUNT = 4;
  final Fight[] fights = new Fight[FIGHTS_COUNT];
  boolean inFight = false;
  ExecutorService executor = Executors.newFixedThreadPool(FIGHTS_COUNT);
  Map<String, Player> players;
  int turn = 1;
  
  public Game(Map<String, Player> players)
  {
    this.players = players;
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
    inFight = false;
    calcFights();
    
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
    UUIDUtils.processGlobalActionListeners(player.listener.onStartTurnListeners, this, player);
    
    player.doForAll(unit -> unit.onStartTurn(this, player));
    player.removeTempBuffs();
    player.calcTriplets();
  }
  
  public void processEndTurn(Player player)
  {
    UUIDUtils.processGlobalActionListeners(player.listener.onEndTurnListeners, this, player);
    player.clearSpellCraft();
    
    player.doForAll(unit -> unit.onEndTurn(this, player));
  }
  
  public void processStartFight(Player player, Player player2)
  {
    UUIDUtils.processGlobalActionListeners(player.listener.onStartFightListeners, this, player);
    
    player.doForAll(unit -> unit.onStartFight(this, player, player2));
  }
  
  public void processEndFight(Player player, Player player2)
  {
    UUIDUtils.processGlobalActionListeners(player.listener.onEndFightListeners, this, player);
    
    player.doForAll(unit -> unit.onEndFight(this, player, player2));
  }
  
  public void doFight()
  {
    inFight = true;
    
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
  
  void calcFights()
  {
    Queue<Player> alive = players.values().stream().filter(Player::isAlive).collect(Collectors.toCollection(ArrayDeque::new));
    Queue<Player> dead = players.values().stream().filter(player -> !player.isAlive()).collect(Collectors.toCollection(ArrayDeque::new));
    
    if ((alive.size() + dead.size()) != PLAYERS_COUNT)
    {
      throw new IllegalArgumentException("Players count is " + (alive.size() + dead.size()) + " but expected " + PLAYERS_COUNT);
    }
    
    for (int i = 0; i < fights.length; i++)
    {
      if (!dead.isEmpty())
      {
        fights[i] = new Fight(this, alive.peek(), dead.peek());
        continue;
      }
      
      if (!alive.isEmpty())
      {
        fights[i] = new Fight(this, alive.peek(), alive.peek());
      }
    }
  }
  
  @Override
  public void close() throws Exception
  {
    executor.shutdown();
  }
}
