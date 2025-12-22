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

import ru.vladislavkomkov.controller.sender.WebSocketSender;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.event.Event;
import ru.vladislavkomkov.model.player.Player;

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

      log.info("Message received: {}", event);
      if (!games.containsKey(event.getGameUUID()))
      {
        throw new RuntimeException("Game " + event.getGameUUID() + " doesn't exist");
      }
      
      Game game = games.get(event.getGameUUID());
      
      String playerUUID = event.getPlayerUUID();
      
      if (event.getType() == Event.Type.CONNECTED)
      {
        if (game.getPlayers().containsKey(playerUUID))
        {
          game.setPlayerSender(playerUUID, new WebSocketSender(conn));
          log.info("Player already connected: {} - {}", playerUUID, game.getUUID());
          return;
        }
        
        if(game.getPlayers().size() >= Game.PLAYERS_COUNT){
          throw new RuntimeException("Game lobby is full");
        }
        
        clearUserFromGames(playerUUID);
        
        game.addPlayer(playerUUID, new Player(playerUUID, game));
        game.setPlayerSender(playerUUID, new WebSocketSender(conn));
        log.info("Player connected: {} - {}", playerUUID, game.getUUID());
        
        return;
      }
      
      if (event.getType() == Event.Type.DISCONNECTED)
      {
        game.getPlayers().get(playerUUID).sendMessage(Event.Type.DISCONNECTED);
        game.getPlayers().get(playerUUID).setSender(null);
        log.info("Player disconnected: {} - {}", playerUUID, game.getUUID());
        return;
      }
      
      if (game.getPlayers().values().stream().noneMatch(player -> player.getUUID().equals(playerUUID)))
      {
        throw new RuntimeException("Player " + playerUUID + " doesn't exist");
      }
      
      String key = playerUUID + "_" + event.getGameUUID();
      if (!eventDispatchers.containsKey(key))
      {
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
  
  @Override
  public void stop() throws InterruptedException
  {
    eventDispatchers.values().forEach(eventDispatcher -> {
      try
      {
        eventDispatcher.close();
      }
      catch (Exception ex)
      {
        log.error("Failed close event dispatcher", ex);
      }
    });

    super.stop();
  }
  
  private void clearUserFromGames(String uuid)
  {
    games.values().forEach(game -> game.removePlayer(uuid));
  }
}
