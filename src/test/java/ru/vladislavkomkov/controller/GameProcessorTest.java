package ru.vladislavkomkov.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.controller.sender.MockConsumer;
import ru.vladislavkomkov.controller.sender.MockSender;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.spell.impl.TripleReward;
import ru.vladislavkomkov.model.entity.spell.impl.second.StrikeOil;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Alleycat;
import ru.vladislavkomkov.model.entity.unit.impl.mech.fourth.AccordoTron;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.event.Event;
import ru.vladislavkomkov.model.event.data.SenderWaiterDataRes;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.model.player.Tavern;

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
  private EventDispatcher eventDispatcher;
  
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
    eventDispatcher = new EventDispatcher(game, true);
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
  void testPlaySell() throws Exception
  {
    gameProcessor.processPreFight();
    
    players.values().forEach(player -> player.getHand().add(Card.of(new Cat())));
    players.values().forEach(player -> player.setMoney(0));
    
    for (Player player : players.values()) {
      eventDispatcher.process(new Event(gameUUID, player.getUUID(), Event.Type.PLAY, List.of(0,0,0,0,0)));
    }
    
    players.values().forEach(player -> assertEquals(0, player.getHand().size()));
    playerConsumers.values().forEach(consumer -> assertEquals(0, consumer.hand.size()));
    players.values().forEach(player -> assertEquals(1, player.getTable().size()));
    playerConsumers.values().forEach(consumer -> assertEquals(1, consumer.table.size()));
    
    for (Player player : players.values()) {
      eventDispatcher.process(new Event(gameUUID, player.getUUID(), Event.Type.SELL, 0));
    }
    
    players.values().forEach(player -> assertEquals(0, player.getTable().size()));
    playerConsumers.values().forEach(consumer -> assertEquals(0, consumer.table.size()));
    players.values().forEach(player -> assertEquals(1, player.getMoney()));
    playerConsumers.values().forEach(consumer -> assertEquals(1, consumer.gold));
  }
  
  @Test
  void testBuy() throws Exception {
    gameProcessor.processPreFight();
    
    players.values().forEach(player -> player.setMoney(player.getBuyPrice()));
    
    for (Player player : players.values()) {
      eventDispatcher.process(new Event(gameUUID, player.getUUID(), Event.Type.BUY, 0));
    }
    
    players.values().forEach(player -> assertEquals(0, player.getMoney()));
    playerConsumers.values().forEach(consumer -> assertEquals(0, consumer.gold));
    
    players.values().forEach(player -> assertEquals(1, player.getHand().size()));
    playerConsumers.values().forEach(consumer -> assertEquals(1, consumer.hand.size()));
  }

  @Test
  void testStartArmorHealth() throws Exception
  {
    gameProcessor.process();
    players.values().forEach(player -> assertEquals(Player.START_HEALTH, player.getHealth()));
    players.values().forEach(player -> assertEquals(Player.START_ARMOR, player.getArmor()));
  }
  
  @Test
  void testIncTavern() throws Exception
  {
    gameProcessor.process();
    
    players.values().forEach(player -> player.setMoney(5));
    
    for (Player player : players.values()) {
      eventDispatcher.process(new Event(gameUUID, player.getUUID(), Event.Type.LVL));
    }
    
    players.values().forEach(player -> assertEquals(0, player.getMoney()));
    playerConsumers.values().forEach(consumer -> assertEquals(0, consumer.gold));
    
    players.values().forEach(player -> assertEquals(2, player.getLevel()));
    playerConsumers.values().forEach(consumer -> assertEquals(2, consumer.tavernLevel));
  }
  
  @Test
  void testTavernReset() throws Exception
  {
    players.values().forEach(player -> assertTrue(player.getTavern().getCards().isEmpty()));
    playerConsumers.values().forEach(consumer -> assertTrue(consumer.tavern.isEmpty()));
    
    gameProcessor.process();
    
    for (Player player : players.values()) {
      eventDispatcher.process(new Event(gameUUID, player.getUUID(), Event.Type.RESET_TAVERN));
    }
    
    players.values().forEach(player -> assertFalse(player.getTavern().getCards().isEmpty()));
    playerConsumers.values().forEach(consumer -> assertFalse(consumer.tavern.isEmpty()));
  }
  
  @Test
  void testTavernFreeze() throws Exception
  {
    gameProcessor.process();
    
    players.values().forEach(player -> player.getTavern().getCards().forEach(slot -> assertFalse(slot.isFreezed())));
    playerConsumers.values().forEach(consumer -> consumer.tavern.forEach(slot -> {
      boolean isFreezed = (Boolean) slot.get(Tavern.Slot.F_IS_FREEZED);
      assertFalse(isFreezed);
    }));
    
    for (Player player : players.values()) {
      eventDispatcher.process(new Event(gameUUID, player.getUUID(), Event.Type.FREEZE));
    }
    
    players.values().forEach(player -> player.getTavern().getCards().forEach(slot -> assertTrue(slot.isFreezed())));
    playerConsumers.values().forEach(consumer -> consumer.tavern.forEach(slot -> {
      boolean isFreezed = (Boolean) slot.get(Tavern.Slot.F_IS_FREEZED);
      assertTrue(isFreezed);
    }));
  }
  
  @Test
  void testTavernBuy() throws Exception
  {
    gameProcessor.process();
    
    player1.setMoney(player1.getBuyPrice());
    
    int serverTavernSize = player1.getTavern().getCards().size();
    int clientTavernSize = player1Consumer.tavern.size();
    
    assertEquals(serverTavernSize, clientTavernSize);
    
    eventDispatcher.process(new Event(gameUUID, player1.getUUID(), Event.Type.BUY, 0));
    
    int serverTavernSizeAfter = player1.getTavern().getCards().size();
    int clientTavernSizeAfter = player1Consumer.tavern.size();
    
    assertEquals((serverTavernSizeAfter + (1)), serverTavernSize);
    assertEquals((clientTavernSizeAfter + (1)), clientTavernSize);
  }
  
  @Test
  void testMoveTavern() throws Exception
  {
    gameProcessor.process();
    
    player1.addToTable(new Cat());
    player1.addToTable(new Alleycat());
    player1.addToTable(new AccordoTron());
    
    eventDispatcher.process(new Event(gameUUID, player1.getUUID(), Event.Type.MOVE, List.of(0,1)));
    
    assertEquals(new Alleycat().getName(), player1.getTable().get(0).getName());
    assertEquals(new Cat().getName(), player1.getTable().get(1).getName());
    assertEquals(new AccordoTron().getName(), player1.getTable().get(2).getName());
  }
  
  @Test
  void testPreFightTimer()
  {
    int ms = 10;
    game.sendPreFightTimer(ms);
    assertEquals(ms, player1Consumer.timer);
  }
  
  @Test
  void testIncMaxMoney() throws Exception
  {
    gameProcessor.process();
    
    int maxMoneyInit = player1.getMaxMoney();
    
    player1.addToHand(Card.of(new StrikeOil()));
    eventDispatcher.process(new Event(gameUUID, player1.getUUID(), Event.Type.PLAY, List.of(0,0,0,0,0)));
    
    assertEquals(maxMoneyInit+1, player1.getMaxMoney());
    assertEquals(maxMoneyInit+1, player1Consumer.maxGold);
  }
  
  @Test
  void testReqRes() throws Exception
  {
    gameProcessor.process();
    
    Spell tripleReward = new TripleReward();
    tripleReward.build();
    player1.addToHand(Card.of(tripleReward));
    
    assertEquals(new TripleReward().getName(), ((Map)(player1Consumer.hand.get(0).get(Card.F_ENTITY))).get(Entity.F_NAME));

    eventDispatcher.process(new Event(gameUUID, player1.getUUID(), Event.Type.PLAY, List.of(0,0,0,0,0)));

    Optional<String> key = player1Consumer.waiters.keySet().stream().findFirst();
    assertTrue(key.isPresent());

    assertTrue(player1Consumer.hand.isEmpty());
    
    eventDispatcher.process(new Event(gameUUID, player1.getUUID(), Event.Type.RES, new SenderWaiterDataRes(key.get(), 0)));
    assertNotEquals(new TripleReward().getName(), ((Map)(player1Consumer.hand.get(0).get(Card.F_ENTITY))).get(Entity.F_NAME));
  }
}
