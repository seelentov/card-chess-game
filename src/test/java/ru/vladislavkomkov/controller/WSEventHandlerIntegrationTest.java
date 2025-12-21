package ru.vladislavkomkov.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import ru.vladislavkomkov.controller.sender.MockSender;
import ru.vladislavkomkov.controller.sender.MockWebSocketClient;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.event.Event;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.model.player.Tavern;

public class WSEventHandlerIntegrationTest
{
  private int port;
  private String gameUUID = "11000000-0000-0000-0000-000000000000";
  
  private Game game;
  private GameProcessor gameProcessor;
  
  private String playerUUID = "10000000-0000-0000-0000-000000000000";
  private Player player;
  private MockConsumer consumer;
  private WebSocketClient client;
  
  private String playerUUID2 = "20000000-0000-0000-0000-000000000000";
  private Player player2;
  private MockConsumer consumer2;
  private WebSocketClient client2;
  
  private WSEventHandler handler;
  
  CountDownLatch connectLatch = new CountDownLatch(2);
  
  @BeforeEach
  void setUp() throws Exception
  {
    port = 11111;
    Game game = new Game(gameUUID);
    
    player = new Player(playerUUID, game);
    consumer = new MockConsumer(playerUUID);
    game.addPlayer(playerUUID, player);
    
    player2 = new Player(playerUUID2, game);
    consumer2 = new MockConsumer(playerUUID2);
    game.addPlayer(playerUUID2, player2);
    
    Map<String, Game> games = new HashMap<>();
    games.put(gameUUID, game);
    
    handler = new WSEventHandler(port, games);
    port = handler.getPort();
    
    client = new MockWebSocketClient(new URI("ws://localhost:" + handler.getPort()), consumer)
    {
      @Override
      public void onOpen(ServerHandshake handshakedata)
      {
        connectLatch.countDown();
      }
    };
    client2 = new MockWebSocketClient(new URI("ws://localhost:" + handler.getPort()), consumer2)
    {
      @Override
      public void onOpen(ServerHandshake handshakedata)
      {
        connectLatch.countDown();
      }
    };
    
    handler.start();
    client.connect();
    client2.connect();
    
    assertTrue(connectLatch.await(10, TimeUnit.SECONDS));
  }
  
  @AfterEach
  void tearDown()
  {
  }
  
  @Test
  void testConnect() throws Exception
  {
    assertFalse(consumer.isConnected);
    assertNull(player.getSender());
    
    client.send(new Event(gameUUID, playerUUID, Event.Type.CONNECTED).getBytes());
    
    assertTrue(waitForCondition(() -> consumer.isConnected, 2000));
    assertNotNull(player.getSender());
  }
  
  @Test
  void testSendEvent() throws Exception {
    testConnect();
    
    player.getTavern().getCards().clear();
    player.getTavern().getCards().add(new Tavern.Slot(Card.of(new Cat())));
    client.send(new Event(gameUUID, playerUUID, Event.Type.BUY, 0).getBytes());
    
    assertTrue(waitForCondition(() -> !consumer.hand.isEmpty(), 2000));
    assertTrue(waitForCondition(() -> ((Map<String,String>)consumer.hand.get(0).get(Card.F_ENTITY)).get(Unit.F_NAME).equals(new Cat().getName()), 2000));
  }
  
  private boolean waitForCondition(BooleanSupplier condition, long timeoutMs) throws InterruptedException
  {
    long start = System.currentTimeMillis();
    while (!condition.getAsBoolean())
    {
      if (System.currentTimeMillis() - start > timeoutMs)
      {
        return false;
      }
      Thread.sleep(50);
    }
    return true;
  }
}
