package ru.vladislavkomkov.model.entity.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ru.vladislavkomkov.GamePlayerTestCase;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.fight.Fight;

public abstract class UnitTestCase extends GamePlayerTestCase
{
  protected void testDefault(Unit unit)
  {
    onSell(unit.newThis());
    onDead(unit.newThis());
    triplet(unit.newThis());
    onAttackAttacked(unit.newThis());
    testAttackCount(unit.newThis());
  };
  
  void onSell(Unit unit)
  {
    setUp();
    
    int money = player.getMoney();
    player.addToTable(unit, 0);
    assertNotNull(player.cloneTable().get(0));
    unit.onSell(game, null, player);
    assertEquals(money + 1, player.getMoney());
    assertEquals(0, player.getUnitsCount());
    
    tearDown();
  }
  
  void onDead(Unit unit)
  {
    setUp();
    
    player.addToTable(unit, 0);
    Unit unit2 = (Unit) unit.newBase();
    player2.addToTable(unit2, 0);
    
    if (unit.isRebirth)
    {
      unit.onAttack(game, null, player, player2, unit2);
    }
    
    assertFalse(unit.isRebirth);
    unit.onDead(game, null, player, player2, unit2);
    
    tearDown();
  }
  
  void triplet(Unit unit)
  {
    setUp();
    
    player.incLevel();
    player.incLevel();
    
    player.addToTable(unit);
    assertEquals(0, player.cloneHand().size());
    assertEquals(1, player.getUnitsCount());
    
    player.addToHand(Card.of(unit.newBase()));
    assertEquals(1, player.cloneHand().size());
    assertEquals(1, player.getUnitsCount());
    
    player.addToHand(Card.of(unit.newBase()));
    assertEquals(1, player.cloneHand().size());
    assertEquals(0, player.getUnitsCount());
    
    Unit gold = (Unit) player.cloneHand().get(0).getEntity();
    assertEquals(unit.getName(), gold.getName());
    assertEquals(unit.getAttack() * 2, gold.getAttack());
    assertEquals(unit.getHealth() * 2, gold.getHealth());
    
    tearDown();
  }
  
  void onAttackAttacked(Unit unit)
  {
    setUp();
    
    player.addToTable(unit, 0);
    Unit unit2 = (Unit) unit.newBase();
    player2.addToTable(unit2, 0);
    
    int unitBeginHP = unit.getHealth();
    int unit2BeginHP = unit2.getHealth();
    
    boolean unitBeginBubbled = unit.isBubbled;
    boolean unitBeginIsRebirth = unit.isRebirth;
    boolean unit2BeginBubbled = unit2.isBubbled;
    boolean unit2BeginIsRebirth = unit2.isRebirth;
    
    unit.onAttack(game, null, player, player2, unit2);
    
    if (unitBeginBubbled)
    {
      assertFalse(unit.isBubbled);
      unit.onAttack(game, null, player, player2, unit2);
    }
    
    unit2.onAttacked(game, null, player, player2, unit);
    
    if (unit2BeginBubbled)
    {
      assertFalse(unit2.isBubbled);
      unit2.onAttacked(game, null, player, player2, unit);
    }
    
    if (unitBeginIsRebirth)
    {
      unit.onAttacked(game, null, player, player2, unit2);
      assertFalse(unit.isRebirth);
    }
    
    if (unit2BeginIsRebirth)
    {
      unit2.onAttacked(game, null, player, player2, unit);
      assertFalse(unit2.isRebirth);
    }
    
    assertEquals(unitBeginHP - unit2.getAttack(), unit.getHealth());
    assertEquals(unit2BeginHP - unit.getAttack(), unit2.getHealth());
    
    tearDown();
  }
  
  void testAttackCount(Unit unit)
  {
    setUp();
    
    Unit unit1 = unit.newBase();
    
    if (unit1.getAttack() == 0)
    {
      return;
    }
    
    int attacksCount = unit1.getAttacksCount().toInt();
    
    player.addToTable(unit1);
    
    Unit unit2 = new Cat();
    unit2.setBaseAttack(0);
    unit2.setHealth(100000);
    
    player2.addToTable(unit2);
    
    int startHealth = unit2.getHealth();
    
    Fight fight = new Fight(game, player, player2);
    
    fight.doTurn();
    
    assertEquals(startHealth - (unit1.getAttack() * attacksCount), fight.getFightTable(player2).get(0).getHealth());
    
    tearDown();
  }
}
