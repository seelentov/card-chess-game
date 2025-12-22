package ru.vladislavkomkov.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.controller.sender.MockConsumer;
import ru.vladislavkomkov.controller.sender.MockWebSocketClient;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.event.Event;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

public class WSEventHandlerIntegrationTest extends IntegrationTestCase
{
  private int port;
  private String gameUUID = "11000000-0000-0000-0000-000000000000";
  
  private Game game;
  
  private String playerUUID = "10000000-0000-0000-0000-000000000000";
  private MockConsumer consumer;
  private WebSocketClient client;
  
  private String playerUUID2 = "20000000-0000-0000-0000-000000000000";
  private MockConsumer consumer2;
  private WebSocketClient client2;
  
  private WSEventHandler handler;
  
  private CountDownLatch connectLatch = new CountDownLatch(2);
  private CountDownLatch disconnectLatch = new CountDownLatch(2);
  private CountDownLatch startLatch = new CountDownLatch(1);
  
  private String UUIDTemp;
  
  @BeforeEach
  void setUp() throws Exception
  {
    port = 0;
    game = new Game(gameUUID);
    
    Map<String, Game> games = new HashMap<>();
    games.put(gameUUID, game);
    
    handler = new WSEventHandler(port, games)
    {
      @Override
      public void onStart()
      {
        super.onStart();
        startLatch.countDown();
      }
    };
    port = handler.getPort();
    
    handler.start();
    assertTrue(startLatch.await(10, TimeUnit.SECONDS));
    
    consumer = new MockConsumer(playerUUID);
    consumer2 = new MockConsumer(playerUUID2);
    
    client = new MockWebSocketClient(new URI("ws://localhost:" + handler.getPort()), consumer)
    {
      @Override
      public void onOpen(ServerHandshake handshakedata)
      {
        super.onOpen(handshakedata);
        connectLatch.countDown();
      }
      
      @Override
      public void onClose(int code, String reason, boolean remote)
      {
        super.onClose(code, reason, remote);
        disconnectLatch.countDown();
      }
    };
    client2 = new MockWebSocketClient(new URI("ws://localhost:" + handler.getPort()), consumer2)
    {
      @Override
      public void onOpen(ServerHandshake handshakedata)
      {
        super.onOpen(handshakedata);
        connectLatch.countDown();
      }
      
      @Override
      public void onClose(int code, String reason, boolean remote)
      {
        super.onClose(code, reason, remote);
        disconnectLatch.countDown();
      }
    };
    
    client.connect();
    client2.connect();
    
    assertTrue(connectLatch.await(10, TimeUnit.SECONDS));
  }
  
  @AfterEach
  void tearDown() throws Exception
  {
    if (client != null)
    {
      client.close();
    }
    
    if (client2 != null)
    {
      client2.close();
    }
    
    assertTrue(disconnectLatch.await(10, TimeUnit.SECONDS));
    
    if (handler != null)
    {
      handler.stop();
    }
  }
  
  @Test
  void testConnect() throws Exception
  {
    while (game.getPlayers().size() < Game.PLAYERS_COUNT - 1)
    {
      String uuid = UUIDUtils.generateKey();
      game.addPlayer(uuid, new Player(uuid, game));
    }
    UUIDTemp = UUIDUtils.generateKey();
    
    assertFalse(consumer.isConnected);
    
    client.send(new Event(gameUUID, UUIDTemp, Event.Type.CONNECTED).getBytes());
    
    assertTrue(waitForCondition(() -> consumer.isConnected, 10000));
    assertTrue(game.getPlayers().values().stream().anyMatch(player1 -> player1.getUUID().equals(UUIDTemp)));
    assertNotNull(game.getPlayers().get(UUIDTemp).getSender());
  }
  
  @Test
  void testDisconnect() throws Exception
  {
    testConnect();
    
    client.send(new Event(gameUUID, UUIDTemp, Event.Type.DISCONNECTED).getBytes());
    
    assertTrue(waitForCondition(() -> !consumer.isConnected, 10000));
    assertNull(game.getPlayers().get(UUIDTemp).getSender());
  }
  
  @Test
  void testConnectIfAllReserved() throws Exception
  {
    while (game.getPlayers().size() < Game.PLAYERS_COUNT)
    {
      String uuid = UUIDUtils.generateKey();
      game.addPlayer(uuid, new Player(uuid, game));
    }
    UUIDTemp = UUIDUtils.generateKey();
    
    assertFalse(consumer.isConnected);
    
    try
    {
      client.send(new Event(gameUUID, UUIDTemp, Event.Type.CONNECTED).getBytes());
    }
    catch (Exception ignored)
    {
    }
    
    assertTrue(waitForCondition(() -> !consumer.isConnected, 10000));
    assertFalse(game.getPlayers().containsKey(UUIDTemp));
  }
}
