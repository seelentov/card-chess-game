package ru.vladislavkomkov.controller;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.event.Event;
import ru.vladislavkomkov.model.player.Player;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class Server extends WebSocketServer
{
  private static final Integer QUEUE_LENGTH = 100;

  private static final Logger log = LoggerFactory.getLogger(Server.class);
  
  private Map<String, Game> games = new HashMap<>();
  private Map<String, BlockingQueue<Event>> queues = new HashMap<>();

  private ExecutorService executor = Executors.newCachedThreadPool();

  public Server(int port)
  {
    super(new InetSocketAddress(port));
  }
  
  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake)
  {
    log.info("Connected {}", conn.getLocalSocketAddress());
  }
  
  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote)
  {
    
  }
  
  @Override
  public void onMessage(WebSocket conn, String message)
  {
    throw new RuntimeException("Not implemented");
  }
  
  @Override
  public void onMessage(WebSocket conn, ByteBuffer buffer)
  {
    try
    {
      Event event = new Event(buffer);
      String key = event.getPlayerUUID() + "_" + event.getGameUUID();
      if (!queues.containsKey(key))
      {
        queues.put(key, new ArrayBlockingQueue<>(QUEUE_LENGTH));

        executor.submit(()->{
            try {
                Event ev = queues.get(key).take();
                process(ev);
            } catch (InterruptedException ex) {
              log.error("Error on process event: {}", event, ex);
            }
        });
      }

      queues.get(key).put(event);
    }
    catch (Exception ex)
    {
      log.error("Error on parse event: ", ex);
    }
  }
  
  @Override
  public void onError(WebSocket conn, Exception ex)
  {
    log.error("Error: ", ex);
  }
  
  @Override
  public void onStart()
  {
    log.info("Start socket on {}", getPort());
  }
  
  private void process(Event event)
  {
    Game game = games.get(event.getGameUUID());
    String playerUUID = event.getPlayerUUID();

    switch (event.getType()){
      case CONNECTED -> {

      }
      default -> throw new RuntimeException("Unexpected event type: " + event.getType());
    }
  }
}
