package ru.vladislavkomkov.controller.sender;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.event.Event;

public class MockConsumer
{
  public boolean gameStarted = false;
  
  public int gold = 0;
  public int maxGold = 0;
  
  public boolean freeze = false;
  
  public List<Card> hand = new ArrayList<>();
  public List<Unit> table = new ArrayList<>();
  
  public int tavernLevel = 1;
  
  public MockConsumer()
  {
  }
  
  public void consume(Event event)
  {
    switch (event.getType()){
      case START -> {
        this.gameStarted = true;
      }
      case MONEY -> {
        this.gold = event.getDataAsInt();
      }
    }
  }
}
