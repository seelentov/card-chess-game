package ru.vladislavkomkov.model.entity.unit.impl.beast.third;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.UnitType;

public class SlyRaptorTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    testDefault(new SlyRaptor());
  }
  
  @Test
  void testOnDead()
  {
    player.addToTable(new SlyRaptor(player));
    player.getTable().get(0).onDead(game, null, player, player2, null);
    
    assertTrue(player.getTable().get(1).isType(UnitType.BEAST));
    assertEquals(SlyRaptor.ATTACK, player.getTable().get(1).getAttack());
    assertEquals(SlyRaptor.HEALTH, player.getTable().get(1).getHealth());
  }
  
  @Test
  void testOnDeadGold()
  {
    player.addToTable(new SlyRaptor(player).buildGold());
    player.getTable().get(0).onDead(game, null, player, player2, null);
    
    assertTrue(player.getTable().get(1).isType(UnitType.BEAST));
    assertEquals(SlyRaptor.ATTACK * 2, player.getTable().get(1).getAttack());
    assertEquals(SlyRaptor.HEALTH * 2, player.getTable().get(1).getHealth());
  }
}
