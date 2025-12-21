package ru.vladislavkomkov.controller;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;

import ru.vladislavkomkov.controller.sender.Sender;
import ru.vladislavkomkov.controller.sender.WebSocketSender;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.event.Event;
import ru.vladislavkomkov.model.event.data.SenderWaiterDataRes;

public class EventDispatcher implements AutoCloseable
{
  static final Integer QUEUE_LENGTH = 1_000;
  static final Logger log = LoggerFactory.getLogger(EventDispatcher.class);
  ExecutorService executor = Executors.newSingleThreadExecutor();
  BlockingQueue<Event> queue = new ArrayBlockingQueue<>(QUEUE_LENGTH);

  volatile AtomicBoolean isStoped = new AtomicBoolean(false);

  Game game;
  
  public EventDispatcher(Game game)
  {
    this(game, false);
  }
  
  public EventDispatcher(Game game, boolean withoutExecutor)
  {
    this.game = game;
    
    if (!withoutExecutor)
      executor.submit(() -> {
        
        while (!isStoped.get())
        {
          Event ev = null;
          
          try
          {
            ev = queue.take();
            
            log.info("PROCESS {}", ev);
            process(ev);
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
  
  void process(Event event)
  {
    String playerUUID = event.getPlayerUUID();
    
    switch (event.getType())
    {
      case BUY -> {
        game.buyTavernCard(playerUUID, event.getDataAsInt());
      }
      case PLAY -> {
        List<Integer> data = event.getData(new TypeReference<List<Integer>>()
        {
        });
        
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
        game.sellCard(playerUUID, event.getDataAsInt());
      }
      case FREEZE -> {
        game.freezeTavern(playerUUID);
      }
      case LVL -> {
        game.lvlUp(playerUUID);
      }
      case MOVE -> {
        List<Integer> data = event.getData(new TypeReference<List<Integer>>()
        {
        });
        
        game.moveTable(playerUUID, data.get(0), data.get(1));
      }
      case RESET_TAVERN -> {
        game.resetTavern(playerUUID);
      }
      case RES -> {
        SenderWaiterDataRes data = event.getData(SenderWaiterDataRes.class);
        game.doSenderWaiter(playerUUID, data.getKey(), data.getParam());
      }
        default -> throw new RuntimeException("Unexpected event type: " + event.getType());
    }
  }


  @Override
  public void close() throws Exception {
    this.isStoped.set(true);
  }
}
