package ru.vladislavkomkov.model.entity.unit.impl.beast.fifth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.AttacksCount;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.demon.first.Imp;
import ru.vladislavkomkov.model.fight.Fight;

public class LightfeatherScreecherTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new LightfeatherScreecher(player));
  }
  
  @Test
  void testOnStartFight()
  {
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new LightfeatherScreecher(player));
    
    Fight fight = new Fight(game, player, player2);
    game.processStartFight(fight, player, player2);
    
    assertEquals(AttacksCount.DOUBLE, fight.getFightTable(player).get(0).getAttacksCount());
    assertEquals(AttacksCount.DEFAULT, fight.getFightTable(player).get(1).getAttacksCount());
    assertTrue(fight.getFightTable(player).get(0).isBubbled());
  }
  
  @Test
  void testOnStartFightGold()
  {
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new LightfeatherScreecher(player).buildGold());
    
    Fight fight = new Fight(game, player, player2);
    game.processStartFight(fight, player, player2);
    
    assertEquals(AttacksCount.DOUBLE, fight.getFightTable(player).get(0).getAttacksCount());
    assertEquals(AttacksCount.DOUBLE, fight.getFightTable(player).get(1).getAttacksCount());
    assertEquals(AttacksCount.DEFAULT, fight.getFightTable(player).get(2).getAttacksCount());
    
    assertTrue(fight.getFightTable(player).get(0).isBubbled());
    assertTrue(fight.getFightTable(player).get(1).isBubbled());
    assertFalse(fight.getFightTable(player).get(2).isBubbled());
  }
  
  @Test
  void testOnStartFightGoldSpace()
  {
    player.addToTable(new Cat(player));
    player.addToTable(new Imp(player));
    player.addToTable(new LightfeatherScreecher(player).buildGold());
    
    Fight fight = new Fight(game, player, player2);
    game.processStartFight(fight, player, player2);
    
    assertEquals(AttacksCount.DOUBLE, fight.getFightTable(player).get(0).getAttacksCount());
    assertEquals(AttacksCount.DEFAULT, fight.getFightTable(player).get(1).getAttacksCount());
    assertEquals(AttacksCount.DOUBLE, fight.getFightTable(player).get(2).getAttacksCount());
    
    assertTrue(fight.getFightTable(player).get(0).isBubbled());
    assertFalse(fight.getFightTable(player).get(1).isBubbled());
    assertTrue(fight.getFightTable(player).get(2).isBubbled());
  }
}
