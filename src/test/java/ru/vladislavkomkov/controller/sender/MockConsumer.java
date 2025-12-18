package ru.vladislavkomkov.controller.sender;

import java.util.*;
import java.util.function.Consumer;

import kotlin.collections.ArrayDeque;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.event.Event;
import ru.vladislavkomkov.model.event.data.SenderWaiterDataReq;

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
  
  public MockConsumer()
  {
  }
  
  public Object getWaiter(String key)
  {
    return waiters.get(key);
  }
  
  public void consume(Event event)
  {
    switch (event.getType())
    {
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
        SenderWaiterDataReq waiter = event.getData(SenderWaiterDataReq.class);
        waiters.put(waiter.getKey(), waiter.getData());
      }
    }
  }
}
