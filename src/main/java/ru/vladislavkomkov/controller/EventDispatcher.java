package ru.vladislavkomkov.controller;

import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vladislavkomkov.controller.sender.WebSocketSender;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.event.Event;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventDispatcher
{
  private static final Logger log = LoggerFactory.getLogger(EventDispatcher.class);
  
  static final Integer QUEUE_LENGTH = 100;
  
  ExecutorService executor = Executors.newSingleThreadExecutor();
  BlockingQueue<Event> queue = new ArrayBlockingQueue<>(QUEUE_LENGTH);
  
  Game game;
  
  public EventDispatcher(Game game)
  {
    this.game = game;
    
    executor.submit(() -> {
      
      while (true)
      {
        Event ev = null;
        
        try
        {
          ev = queue.take();
          
          log.info("PROCESS {}", ev);
          process(game, ev);
          log.info("COMPLETE {}", ev);
        }
        catch (Exception ex)
        {
          log.error("ERROR {}", ev, ex);
        }
      }
    });
  }
  
  public void put(Event event) throws InterruptedException
  {
    queue.put(event);
  }
  
  private void process(Game game, Event event)
  {
    process(game, event, null);
  }
  
  private void process(Game game, Event event, WebSocket conn)
  {
    String playerUUID = event.getPlayerUUID();
    
    switch (event.getType())
    {
      case CONNECTED -> {
        if (conn == null)
        {
          throw new RuntimeException("WenSocketClient is null");
        }
        
        game.setPlayerSender(playerUUID, new WebSocketSender(conn));
      }
      case BYU -> {
        game.buyTavernCard(playerUUID, event.getData().get(0));
      }
      case PLAY -> {
        List<Integer> data = event.getData();
        
        if (data.size() < 5)
        {
          throw new IllegalArgumentException("Expected " + 5 + " arguments, but got " + data.size());
        }
        
        if (data.get(2) != 0 && data.get(2) != 1)
        {
          throw new IllegalArgumentException("Expected 0/1 arguments, but got " + data.get(2));
        }
        
        if (data.get(4) != 0 && data.get(4) != 1)
        {
          throw new IllegalArgumentException("Expected 0/1 arguments, but got " + data.get(2));
        }
        
        game.playCard(
            playerUUID,
            data.get(0),
            data.get(1),
            data.get(2) == 1,
            data.get(3),
            data.get(4) == 1);
      }
      case SELL -> {
        game.sellCard(playerUUID, event.getData().get(0));
      }
      case FREEZE -> {
        game.freezeTavern(playerUUID);
      }
      case LVL_UP -> {
        game.lvlUp(playerUUID);
      }
      case MOVE -> {
        game.moveTable(playerUUID, event.getData().get(0), event.getData().get(1));
      }
      case ROLL -> {
        game.resetTavern(playerUUID);
      }
      default -> throw new RuntimeException("Unexpected event type: " + event.getType());
    }
  }
}
