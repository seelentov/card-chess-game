package ru.vladislavkomkov.model.entity.unit.impl.beast.fourth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.fight.Fight;

public class StompingStegodonTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new StompingStegodon());
  }
  
  @Test
  void testOnAttack()
  {
    testCaseOnAttack(false);
  }
  
  @Test
  void testOnAttackGold()
  {
    testCaseOnAttack(true);
  }
  
  void testCaseOnAttack(boolean isGold)
  {
    Unit unit11 = isGold ? new StompingStegodon(player).buildGold() : new StompingStegodon(player);
    player.addToTable(unit11);
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    
    Unit unit21 = new Cat(player2);
    unit21.setBaseAttack(0);
    unit21.setHealth(999999);
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    
    fight.doTurn();
    
    List<Unit> units = fight.getFightTable(player);
    
    assertBuffsCount(units.get(0), 0, (isGold ? new StompingStegodon(player).buildGold() : new StompingStegodon(player)), isGold);
    for (int i = 1; i < units.size(); i++)
    {
      assertEquals(
          new Cat().getAttack() + (StompingStegodon.ATTACK_BOOST * (isGold ? 2 : 1)),
          units.get(i).getAttack());
    }
    
    fight.doTurn();
    fight.doTurn();
    
    assertBuffsCount(units.get(0), 1, (isGold ? new StompingStegodon(player).buildGold() : new StompingStegodon(player)), isGold);
    assertBuffsCount(units.get(1), 1, isGold);
    for (int i = 2; i < units.size(); i++)
    {
      assertBuffsCount(units.get(i), 2, isGold);
    }
    
    fight.doTurn();
    fight.doTurn();
    
    assertBuffsCount(units.get(0), 3, (isGold ? new StompingStegodon(player).buildGold() : new StompingStegodon(player)), isGold);
    assertBuffsCount(units.get(1), 3, isGold);
    assertBuffsCount(units.get(2), 2, isGold);
    
    for (int i = 3; i < units.size(); i++)
    {
      assertBuffsCount(units.get(i), 4, isGold);
    }
    
    fight.doTurn();
    fight.doTurn();
    
    assertBuffsCount(units.get(0), 7, (isGold ? new StompingStegodon(player).buildGold() : new StompingStegodon(player)), isGold);
    assertBuffsCount(units.get(1), 7, isGold);
    assertBuffsCount(units.get(2), 6, isGold);
    assertBuffsCount(units.get(3), 4, isGold);
    
    for (int i = 4; i < units.size(); i++)
    {
      assertBuffsCount(units.get(i), 8, isGold);
    }
    
    fight.doTurn();
    fight.doTurn();
    
    assertBuffsCount(units.get(0), 15, (isGold ? new StompingStegodon(player).buildGold() : new StompingStegodon(player)), isGold);
    assertBuffsCount(units.get(1), 15, isGold);
    assertBuffsCount(units.get(2), 14, isGold);
    assertBuffsCount(units.get(3), 12, isGold);
    assertBuffsCount(units.get(4), 8, isGold);
    
    for (int i = 5; i < units.size(); i++)
    {
      assertBuffsCount(units.get(i), 16, isGold);
    }
    
    fight.doTurn();
    fight.doTurn();
    
    assertBuffsCount(units.get(0), 31, (isGold ? new StompingStegodon(player).buildGold() : new StompingStegodon(player)), isGold);
    assertBuffsCount(units.get(1), 31, isGold);
    assertBuffsCount(units.get(2), 30, isGold);
    assertBuffsCount(units.get(3), 28, isGold);
    assertBuffsCount(units.get(4), 24, isGold);
    assertBuffsCount(units.get(5), 16, isGold);
    assertBuffsCount(units.get(6), 32, isGold);
  }
  
  void assertBuffsCount(Unit unit, int buffsCount)
  {
    assertBuffsCount(unit, buffsCount, new Cat(player), false);
  }
  
  void assertBuffsCount(Unit unit, int buffsCount, boolean isGold)
  {
    assertBuffsCount(unit, buffsCount, isGold ? new Cat(player).buildGold() : new Cat(player), isGold);
  }
  
  void assertBuffsCount(Unit unit, int buffsCount, Unit nw)
  {
    assertBuffsCount(unit, buffsCount, nw, false);
  }
  
  void assertBuffsCount(Unit unit, int buffsCount, Unit nw, boolean isGold)
  {
    assertEquals((nw.getAttack() + ((StompingStegodon.ATTACK_BOOST * (isGold ? 2 : 1)) * buffsCount)) / (StompingStegodon.ATTACK_BOOST * (isGold ? 2 : 1)),
        unit.getAttack() / (StompingStegodon.ATTACK_BOOST * (isGold ? 2 : 1)));
  }
}
