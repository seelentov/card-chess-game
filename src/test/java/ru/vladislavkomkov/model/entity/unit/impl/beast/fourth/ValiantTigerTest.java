package ru.vladislavkomkov.model.entity.unit.impl.beast.fourth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.fight.Fight;

public class ValiantTigerTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new ValiantTiger());
  }
  
  @Test
  void testOnDead()
  {
    player.addToTable(new ValiantTiger(player));
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    
    Unit unit21 = new Cat(player2);
    unit21.setBaseAttack(99999);
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    fight.doTurn();
    
    assertEquals(new Cat().getAttack(), fight.getFightTable(player).get(0).getAttack());
    assertEquals(new Cat().getAttack() + ValiantTiger.ATTACK_BOOST, fight.getFightTable(player).get(1).getAttack());
    assertEquals(new Cat().getAttack() + ValiantTiger.ATTACK_BOOST, player.getTable().get(2).getAttack());
  }
}
