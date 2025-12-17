package ru.vladislavkomkov.model.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.GamePlayerTestCase;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.spell.impl.first.TavernCoin;
import ru.vladislavkomkov.model.entity.spell.impl.spellcraft.impl.DeepBlues;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Alleycat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

public class PlayerTest extends GamePlayerTestCase
{
  @Test
  void testResetTavern()
  {
    player.resetTavern();
    
    assertEquals(
        Tavern.getCountByLevel(player.getLevel()),
        player.getTavern().cards.stream().filter(card1 -> card1.getEntity() instanceof Unit).count());
    
    for (Tavern.Slot slot : player.getTavern().getCards())
    {
      assertEquals(player.getLevel(), slot.getEntity().getLevel());
    }
    
    assertEquals(
        1,
        player.getTavern().cards.stream().filter(card1 -> card1.getEntity() instanceof Spell).count());
  }
  
  @Test
  void testPlayCardSpell()
  {
    
    int incMoney = player.getMoney();
    int moneyTest = 10;
    
    player.addToHand(Card.of(new Spell()
    {
      @Override
      public void build()
      {
      
      }
      
      @Override
      public void onPlayed(Game game, Player player, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2, boolean auto)
      {
        player.addMoney(moneyTest);
      }
    }));
    
    assertEquals(1, player.cloneHand().size());
    
    player.playCard(0, 0);
    
    assertTrue(player.cloneHand().isEmpty());
    
    assertEquals(incMoney + moneyTest, player.getMoney());
  }
  
  @Test
  void testPlayCardUnit()
  {
    String testName = "test";
    
    player.addToHand(Card.of(new Unit()
    {
      @Override
      public String getName()
      {
        return testName;
      }
    }));
    
    assertEquals(1, player.cloneHand().size());
    
    player.playCard(0, 0);
    
    assertTrue(player.cloneHand().isEmpty());
    
    assertEquals(testName, player.cloneTable().get(0).getName());
  }
  
  @Test
  void testCloneHand()
  {
    for (int i = 0; i < 2; i++)
    {
      player.addToHand(Card.of(new Cat()));
    }
    
    List<Card> hand = player.cloneHand();
    
    assertEquals(new Cat().getName(), hand.get(0).getEntity().getName());
    assertEquals(new Cat().getName(), hand.get(1).getEntity().getName());
    
    for (int i = 2; i < 10; i++)
    {
      int finalI = i;
      assertThrowsExactly(IndexOutOfBoundsException.class, () -> hand.get(finalI));
    }
  }
  
  @Test
  void testCloneTable()
  {
    for (int i = 0; i < 2; i++)
    {
      player.addToTable(new Cat(), i);
    }
    
    List<Unit> units = player.cloneTable();
    
    assertEquals(2, units.size());
    assertEquals(new Cat().getName(), units.get(0).getName());
    assertEquals(new Cat().getName(), units.get(1).getName());
  }
  
  @Test
  void testAddSingleToHand()
  {
    player = new Player(game);
    player.addToHand(new Card(new TavernCoin()));
    
    assertEquals(1, player.cloneHand().stream().filter(Objects::nonNull).count());
    assertEquals(player.cloneHand().get(0).getEntity().getName(), new TavernCoin().getName());
  }
  
  @Test
  void testAddManyToHand()
  {
    player = new Player(game);
    for (int i = 0; i < 20; i++)
    {
      if (i % 2 == 0)
      {
        player.addToHand(new Card(new TavernCoin()));
      }
      else
      {
        player.addToHand(new Card(new DeepBlues()));
      }
    }
    
    player.addToHand(new Card(new Alleycat()));
    
    List<Card> cards = player.cloneHand();
    
    assertEquals(10, cards.stream().filter(Objects::nonNull).count());
    
    for (int i = 0; i < 10; i++)
    {
      String cardName = cards.get(i).getEntity().getName();
      if (i % 2 == 0)
      {
        assertEquals(cardName, new TavernCoin().getName());
      }
      else
      {
        assertEquals(cardName, new DeepBlues().getName());
      }
    }
  }
  
  @Test
  void testBuyCard()
  {
    player.getTavern().add(Card.of(new Alleycat()));
    player.buyCard(0);
    assertEquals(new Alleycat().getName(), player.cloneHand().get(0).getEntity().getName());
  }
  
  @Test
  void testPlaySpellCard()
  {
    assertEquals(3, player.getMoney());
    
    player.addToHand(Card.of(new TavernCoin()));
    
    player.playCard(0, 0);
    
    assertEquals(4, player.getMoney());
    assertEquals(0, player.cloneHand().stream().filter(Objects::nonNull).count());
  }
  
  @Test
  void testPlayUnitCard()
  {
    player.addToHand(Card.of(new Alleycat()));
    
    player.playCard(0, 0);
    
    assertEquals(0, player.cloneHand().stream().filter(Objects::nonNull).count());
    
    assertEquals(2, player.cloneTable().stream().filter(Objects::nonNull).count());
    
    Unit unit1 = player.cloneTable().get(0);
    Unit unit2 = player.cloneTable().get(1);
    
    assertEquals(new Alleycat().getName(), unit1.getName());
    assertEquals(new Cat().getName(), unit2.getName());
  }
  
  @Test
  void testAddToTable()
  {
    player.addToTable(new Alleycat(), 1);
    
    assertEquals(new Alleycat().getName(), player.cloneTable().get(0).getName());
  }
  
  @Test
  void testDoForAll()
  {
    player.addToTable(new Cat(), 1);
    player.addToTable(new Cat(), 1);
    
    player.doForAll(unit -> {
      unit.incHealth(2);
      unit.incAttack(2);
    });
    
    List<Unit> units = player.cloneTable();
    
    for (int i = 0; i < 2; i++)
    {
      assertEquals(3, units.get(i).getAttack());
    }
  }
  
  @Test
  void testApplyDamage()
  {
    int armor = 5;
    player.addArmor(armor);
    
    for (int i = 1; i <= armor; i++)
    {
      player.applyDamage(1);
      assertEquals(armor - i, player.getArmor());
      assertEquals(player.maxHealth, player.getHealth());
    }
    
    for (int i = 1; i <= player.getMaxHealth(); i++)
    {
      player.applyDamage(1);
      assertEquals(player.getMaxHealth() - i, player.getHealth());
    }
    
    assertFalse(player.isAlive());
  }
  
  @Test
  void testIsAlive()
  {
    int halfHP = player.getHealth() / 2;
    int armor = player.getArmor();
    
    player.applyDamage(halfHP + armor);
    assertTrue(player.isAlive());
    player.applyDamage(halfHP + 1);
    assertFalse(player.isAlive());
  }
  
  @Test
  void testIncLevel()
  {
    for (int i = 1; i < 10; i++)
    {
      assertEquals(Math.min(i, Player.MAX_LEVEL), player.getLevel());
      player.incLevel(true);
    }
  }
  
  @Test
  void testTriplet()
  {
    player.addToHand(Card.of(new Cat()));
    assertEquals(1, player.hand.size());
    
    player.addToHand(Card.of(new Cat()));
    assertEquals(2, player.hand.size());
    
    player.addToHand(Card.of(new Cat()));
    assertEquals(1, player.hand.size());
    
    Unit goldUnit = (Unit) player.hand.get(0).getEntity();
    assertEquals(new Cat().getName(), goldUnit.getName());
    assertEquals(new Cat().getAttack() * 2, goldUnit.getAttack());
    assertEquals(new Cat().getHealth() * 2, goldUnit.getHealth());
  }
  
  @Test
  void testTripletBuffed()
  {
    Unit unit = new Cat();
    player.addToTable(unit);
    
    new DeepBlues().onPlayed(game, player, 0);
    
    player.addToHand(Card.of(new Cat()));
    assertEquals(1, player.hand.size());
    
    player.addToHand(Card.of(new Cat()));
    assertEquals(1, player.hand.size());
    
    Unit goldUnit = (Unit) player.hand.get(0).getEntity();
    assertEquals(new Cat().getName(), goldUnit.getName());
    assertEquals(new Cat().getAttack() * 2 + (DeepBlues.ATTACK_BOOST), goldUnit.getAttack());
    assertEquals(new Cat().getHealth() * 2 + (DeepBlues.HEALTH_BOOST), goldUnit.getHealth());
    
    game.processStartTurn(player);
    
    goldUnit = (Unit) player.hand.get(0).getEntity();
    assertEquals(new Cat().getName(), goldUnit.getName());
    assertEquals(new Cat().getAttack() * 2, goldUnit.getAttack());
    assertEquals(new Cat().getHealth() * 2, goldUnit.getHealth());
  }
  
  @Test
  void testDynamicIncTavernPrice()
  {
    game.incTurn();
    
    for (int j = 1; j < 100; j++)
    {
      assertEquals(Math.min(j, Player.MAX_LEVEL), player.getLevel());
      for (int i = player.getIncLevelPrice(); i >= 0; i--)
      {
        assertEquals(i, player.getIncLevelPrice());
        game.doTurnBegin();
        game.incTurn();
      }
      player.incLevel();
    }
  }
  
  @Test
  void testDynamicIncTavernPriceIncByMiddle()
  {
    game.incTurn();
    
    for (int j = 1; j < 100; j++)
    {
      assertEquals(Math.min(j, Player.MAX_LEVEL), player.getLevel());
      int price = player.getIncLevelPrice();
      for (int i = price; i >= 0; i--)
      {
        assertEquals(i, player.getIncLevelPrice());
        game.doTurnBegin();
        game.incTurn();
        
        if(i == (price / 2)){
          break;
        }
      }
      player.incLevel();
    }
  }
  
  @Test
  void testMaxMoneyIncreasement()
  {
    game.incTurn();
    
    for (int j = 3; j < 100; j++)
    {
      assertEquals(Math.min(j, Player.MAX_MONEY), player.maxMoney);
      game.doTurnBegin();
    }
  }
}
