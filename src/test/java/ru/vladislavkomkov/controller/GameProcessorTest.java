package ru.vladislavkomkov.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.controller.sender.MockConsumer;
import ru.vladislavkomkov.controller.sender.MockSender;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.player.Player;

public class GameProcessorTest
{
  private String UUIDPart = "0000000-0000-0000-0000-000000000000";
  private String gameUUID = "11000000-0000-0000-0000-000000000000";
  private Game game;
  
  private Player player1;
  private MockConsumer player1Consumer;
  
  private Player player2;
  private MockConsumer player2Consumer;
  
  private Player player3;
  private MockConsumer player3Consumer;
  
  private Player player4;
  private MockConsumer player4Consumer;
  
  private Player player5;
  private MockConsumer player5Consumer;
  
  private Player player6;
  private MockConsumer player6Consumer;
  
  private Player player7;
  private MockConsumer player7Consumer;
  
  private Player player8;
  private MockConsumer player8Consumer;
  
  private final Map<String, Player> players = new HashMap<>();
  private final Map<String, MockConsumer> playerConsumers = new HashMap<>();
  
  private GameProcessor gameProcessor;
  
  @BeforeEach
  protected void setUp()
  {
    game = new Game(players, gameUUID);
    
    player1 = new Player("1" + UUIDPart, game);
    player1Consumer = new MockConsumer();
    player1.setSender(new MockSender(player1Consumer));
    
    player2 = new Player("2" + UUIDPart, game);
    player2Consumer = new MockConsumer();
    player2.setSender(new MockSender(player2Consumer));
    
    player3 = new Player("3" + UUIDPart, game);
    player3Consumer = new MockConsumer();
    player3.setSender(new MockSender(player3Consumer));
    
    player4 = new Player("4" + UUIDPart, game);
    player4Consumer = new MockConsumer();
    player4.setSender(new MockSender(player4Consumer));
    
    player5 = new Player("5" + UUIDPart, game);
    player5Consumer = new MockConsumer();
    player5.setSender(new MockSender(player5Consumer));
    
    player6 = new Player("6" + UUIDPart, game);
    player6Consumer = new MockConsumer();
    player6.setSender(new MockSender(player6Consumer));
    
    player7 = new Player("7" + UUIDPart, game);
    player7Consumer = new MockConsumer();
    player7.setSender(new MockSender(player7Consumer));
    
    player8 = new Player("8" + UUIDPart, game);
    player8Consumer = new MockConsumer();
    player8.setSender(new MockSender(player8Consumer));
    
    players.put("1" + UUIDPart, player1);
    playerConsumers.put("1" + UUIDPart, player1Consumer);
    
    players.put("2" + UUIDPart, player2);
    playerConsumers.put("2" + UUIDPart, player2Consumer);
    
    players.put("3" + UUIDPart, player3);
    playerConsumers.put("3" + UUIDPart, player3Consumer);
    
    players.put("4" + UUIDPart, player4);
    playerConsumers.put("4" + UUIDPart, player4Consumer);
    
    players.put("5" + UUIDPart, player5);
    playerConsumers.put("5" + UUIDPart, player5Consumer);
    
    players.put("6" + UUIDPart, player6);
    playerConsumers.put("6" + UUIDPart, player6Consumer);
    
    players.put("7" + UUIDPart, player7);
    playerConsumers.put("7" + UUIDPart, player7Consumer);
    
    players.put("8" + UUIDPart, player8);
    playerConsumers.put("8" + UUIDPart, player8Consumer);
    
    gameProcessor = new GameProcessor(Map.of(gameUUID, game), 0);
  }
  
  @AfterEach
  protected void tearDown()
  {
  }
  
  @Test
  void testStartGame()
  {
    gameProcessor.start();
    playerConsumers.values().forEach(consumer -> assertTrue(consumer.isGameStarted));
  }
  
  @Test
  void testIncMoneyBeforeLimit() throws Exception
  {
    for (int i = 3; i < Player.MAX_MONEY; i++)
    {
      gameProcessor.process();
      int finalI = i;
      players.values().forEach(player -> assertEquals(finalI, player.getMoney()));
      playerConsumers.values().forEach(consumer -> assertEquals(finalI, consumer.gold));
    }
    
    for (int i = 0; i < 2; i++)
    {
      gameProcessor.process();
      players.values().forEach(player -> assertEquals(Player.MAX_MONEY, player.getMoney()));
      playerConsumers.values().forEach(consumer -> assertEquals(Player.MAX_MONEY, consumer.gold));
    }
  }
  
  @Test
  void testPreFight() throws Exception
  {
    gameProcessor.processPreFight();
    
    players.values().forEach(player -> player.buyCard(0));
    players.values().forEach(player -> assertEquals(0, player.getMoney()));
    playerConsumers.values().forEach(consumer -> assertEquals(0, consumer.gold));

    players.values().forEach(player -> assertEquals(1, player.getHand().size()));
    playerConsumers.values().forEach(consumer -> assertEquals(1, consumer.hand.size()));
    
    players.values().forEach(player -> player.playCard(0));
    players.values().forEach(player -> assertEquals(0, player.getHand().size()));
    playerConsumers.values().forEach(consumer -> assertEquals(0, consumer.hand.size()));
    players.values().forEach(player -> assertEquals(1, player.getTable().size()));
    playerConsumers.values().forEach(consumer -> assertEquals(1, consumer.table.size()));
  }

  @Test
  void testStartArmorHealth() throws Exception
  {
    gameProcessor.process();
    players.values().forEach(player -> assertEquals(Player.START_HEALTH, player.getHealth()));
    players.values().forEach(player -> assertEquals(Player.START_ARMOR, player.getArmor()));
  }

  @Test
  void testTavern() throws Exception
  {
    gameProcessor.process();

    players.values().forEach(Player::incLevel);

    players.values().forEach(player -> assertEquals(0, player.getMoney()));
    playerConsumers.values().forEach(consumer -> assertEquals(0, consumer.gold));

    players.values().forEach(player -> assertEquals(2, player.getLevel()));
    playerConsumers.values().forEach(consumer -> assertEquals(2, consumer.tavernLevel));
  }
  
  private void waitStatus(Game.State state) throws Exception
  {
    waitStatus(state, 5000);
  }
  
  private void waitStatus(Game.State state, int timeout) throws Exception
  {
    while (game.getState() != state)
    {
      if (timeout <= 0)
      {
        break;
      }
      Thread.sleep(100);
      timeout -= 100;
    }
  }
}
