package ru.vladislavkomkov.controller;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.event.Event;

public class WSEventHandler extends WebSocketServer
{
  static final Logger log = LoggerFactory.getLogger(WSEventHandler.class);
  
  final Map<String, Game> games;
  final Map<String, EventDispatcher> eventDispatchers = new HashMap<>();
  
  public WSEventHandler(int port, Map<String, Game> games)
  {
    super(new InetSocketAddress(port));
    this.games = games;
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
      if (!eventDispatchers.containsKey(key))
      {
        Game game = games.get(event.getGameUUID());
        eventDispatchers.put(key, new EventDispatcher(game));
      }
      
      eventDispatchers.get(key).put(event);
    }
    catch (Exception ex)
    {
      log.error("Error on put event: ", ex);
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
}
