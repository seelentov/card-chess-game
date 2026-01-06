package ru.vladislavkomkov.model.entity.unit.impl.beast.fifth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.fight.Fight;

public class SilithidBurrowerTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new SilithidBurrower(player));
  }
  
  @Test
  void testOnDead()
  {
    Unit unit11 = new SilithidBurrower(player);
    unit11.setIsTaunt(true);
    
    player.addToTable(unit11);
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    
    Unit unit21 = new Cat(player);
    unit21.setBaseAttack(9999);
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    fight.doTurn();
    
    fight.getFightTable(player).forEach(unit -> {
      assertEquals(new Cat().getAttack() + SilithidBurrower.ATTACK_BOOST, unit.getAttack());
      assertEquals(new Cat().getHealth() + SilithidBurrower.HEALTH_BOOST, unit.getHealth());
    });
  }
  
  @Test
  void testOnDeadWithBoost()
  {
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new SilithidBurrower(player));
    
    player.getTable().stream().filter(unit -> unit instanceof Cat).forEach(unit -> {
      unit.setIsTaunt(true);
    });
    
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    
    Unit unit21 = new Cat(player);
    unit21.setBaseAttack(9999);
    unit21.setHealth(99999);
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    
    for (int i = 0; i < 4; i++)
    {
      fight.doTurn();
    }
    
    fight.getFightTable(player).forEach(unit -> {
      assertEquals(new Cat().getAttack() + (SilithidBurrower.ATTACK_BOOST * 4), unit.getAttack());
      assertEquals(new Cat().getHealth() + (SilithidBurrower.ATTACK_BOOST * 4), unit.getHealth());
    });
  }
  
  @Test
  void testBoostTable()
  {
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new SilithidBurrower(player));
    
    player.getTable().stream().filter(unit -> unit instanceof Cat).forEach(unit -> {
      unit.setIsTaunt(true);
    });
    
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    
    Unit unit21 = new Cat(player);
    unit21.setBaseAttack(9999);
    unit21.setHealth(99999);
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    
    for (int i = 0; i < 4; i++)
    {
      fight.doTurn();
    }
    
    assertEquals(4, ((SilithidBurrower) player.getTable().get(3)).stacks);
    
    for (int i = 0; i < 2; i++)
    {
      fight.doTurn();
    }
    
    assertEquals(4, ((SilithidBurrower) player.getTable().get(3)).stacks);
  }
}
