package ru.vladislavkomkov.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.controller.sender.MockConsumer;
import ru.vladislavkomkov.controller.sender.MockSender;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.event.Event;
import ru.vladislavkomkov.model.player.Player;

public class WSEventHandlerIntegrationTest
{
  private int port;
  private String gameUUID = "11000000-0000-0000-0000-000000000000";
  
  private Game game;
  private GameProcessor gameProcessor;
  
  private String playerUUID = "10000000-0000-0000-0000-000000000000";
  private Player player;
  private MockConsumer consumer;
  
  private String playerUUID2 = "20000000-0000-0000-0000-000000000000";
  private Player player2;
  private MockConsumer consumer2;
  
  private WSEventHandler handler;
  private WebSocketClient client;
  
  CountDownLatch connectLatch;
  
  @BeforeEach
  void setUp() throws Exception
  {
    port = 0;
    Game game = new Game(gameUUID);
    
    player = new Player(playerUUID, game);
    consumer = new MockConsumer(playerUUID);
    player.setSender(new MockSender(consumer));
    game.addPlayer(playerUUID, player);
    
    player2 = new Player(playerUUID2, game);
    consumer2 = new MockConsumer(playerUUID2);
    player2.setSender(new MockSender(consumer2));
    game.addPlayer(playerUUID2, player2);
    
    Map<String, Game> games = new HashMap<>();
    games.put(gameUUID, game);
    
    handler = new WSEventHandler(port, games);
    handler.start();
    
    connectLatch = new CountDownLatch(1);
    client = new WebSocketClient(new URI("ws://localhost:" + port))
    {
      @Override
      public void onOpen(ServerHandshake handshakedata) {
      
      }
      
      @Override
      public void onMessage(String message) {
      
      }
      
      @Override
      public void onClose(int code, String reason, boolean remote) {
      
      }
      
      @Override
      public void onError(Exception ex) {
      
      }
    };
    
  }
  
  @AfterEach
  void tearDown()
  {
    
  }
  
  @Test
  void testConnect()
  {
      assertFalse(consumer.isConnected);
    connect();
      client.send(new Event(gameUUID, playerUUID, Event.Type.CONNECTED).getBytes());
      assertTrue(consumer.isConnected);
  }
  
  void connect() throws Exception {
    client.connect();
    assertTrue(connectLatch.await(2, TimeUnit.SECONDS));
  }
}
