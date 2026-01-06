package ru.vladislavkomkov.controller.sender;

import java.util.*;

import ru.vladislavkomkov.model.ActionEvent;
import ru.vladislavkomkov.model.event.Event;
import ru.vladislavkomkov.model.event.data.SenderWaiterDataReq;
import ru.vladislavkomkov.model.fight.FightInfo;
import ru.vladislavkomkov.model.player.Player;

public class MockConsumer
{
  public int health = 0;
  public int armor = 0;
  
  public boolean isGameStarted = false;
  
  public int gold = 0;
  public int maxGold = 0;
  
  public boolean freeze = false;
  public List<Map> tavern = new ArrayList<>();
  
  public List<Map> hand = new ArrayList<>();
  public List<Map> table = new ArrayList<>();
  
  public int tavernLevel = 1;
  
  public int timer = 0;
  
  public Map<String, Object> waiters = new HashMap<>();
  
  public boolean inFight = false;
  
  public Queue<Map> scenario = new ArrayDeque<>();
  
  public boolean isPlayer1 = false;
  public List<Map> inFightTable = new ArrayList<>();
  public List<Map> inFightTableEnemy = new ArrayList<>();
  
  public boolean isConnected = false;
  
  String UUID;
  
  public MockConsumer(String UUID)
  {
    this.UUID = UUID;
  }
  
  public Object getWaiter(String key)
  {
    return waiters.get(key);
  }
  
  public void consume(Event event)
  {
    switch (event.getType())
    {
      case CONNECTED -> {
        this.isConnected = true;
      }
      case DISCONNECTED -> {
        this.isConnected = false;
      }
      case START -> {
        this.isGameStarted = true;
      }
      case MONEY -> {
        this.gold = event.getDataAsInt();
      }
      case MAX_MONEY -> {
        this.maxGold = event.getDataAsInt();
      }
      case HAND -> {
        this.hand = event.getData(List.class);
      }
      case TABLE -> {
        this.table = event.getData(List.class);
      }
      case ARMOR -> {
        this.armor = event.getDataAsInt();
      }
      case HEALTH -> {
        this.health = event.getDataAsInt();
      }
      case LVL -> {
        this.tavernLevel = event.getDataAsInt();
      }
      case TAVERN -> {
        this.tavern = event.getData(List.class);
      }
      case FREEZE -> {
        this.freeze = event.getDataAsBool();
      }
      case PRE_FIGHT_TIMER -> {
        this.timer = event.getDataAsInt();
      }
      case WAIT_REQ -> {
        Map waiter = event.getData(Map.class);
        waiters.put((String) waiter.get(SenderWaiterDataReq.F_KEY), waiter.get(SenderWaiterDataReq.F_DATA));
      }
      case FIGHT -> {
        Map<String, Map<String, Object>> info = event.getData(Map.class);
        this.inFight = true;
        if (info.get(FightInfo.F_PLAYER_1).get(Player.F_UUID).equals(this.UUID))
        {
          isPlayer1 = true;
        }
        else if (info.get(FightInfo.F_PLAYER_2).get(Player.F_UUID).equals(this.UUID))
        {
          isPlayer1 = false;
        }
        else
        {
          throw new RuntimeException("Wrong players");
        }
        
        this.scenario = new LinkedList<>((List) info.get(FightInfo.F_HISTORY));
      }
    }
  }
  
  public Optional<Map> processFight()
  {
    Map ev = scenario.poll();
    
    if (ev == null)
    {
      return Optional.empty();
    }
    
    boolean isThisPlayer = ((Map<String, String>) ev.get(ActionEvent.F_PLAYER)).get(Player.F_UUID).equals(UUID);
    
    List<Map> inFightTable = (List<Map>) ev.get(ActionEvent.F_PLAYER_UNITS);
    List<Map> inFightTableEnemy = (List<Map>) ev.get(ActionEvent.F_ENEMY_UNITS);
    
    this.inFightTable = isThisPlayer ? inFightTable : inFightTableEnemy;
    this.inFightTableEnemy = isThisPlayer ? inFightTableEnemy : inFightTable;
    
    return Optional.of(ev);
  }
}
