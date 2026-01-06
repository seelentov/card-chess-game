package ru.vladislavkomkov.model.entity.unit.impl.beast.sixth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.fight.Fight;

public class P0ULTR0NTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new P0ULTR0N());
  }
  
  @Test
  void testAvenge()
  {
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new P0ULTR0N(player));
    
    player.getTable().stream()
        .filter(table -> table instanceof Cat)
        .forEach(unit -> unit.setIsTaunt(true));
    
    Unit unit21 = new Cat(player2);
    unit21.setBaseAttack(99999);
    unit21.setHealth(99999);
    int startHealthUnit21 = unit21.getHealth();
    
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    
    for (int i = 0; i < 4; i++)
    {
      fight.doTurn();
      
    }
    
    Unit unit21FromBattle = fight.getFightTable(player2).get(0);
    
    assertEquals(
        startHealthUnit21 - ((new Cat(player).getAttack() * 4) + new P0ULTR0N(player).getAttack()),
        unit21FromBattle.getHealth());
  }
}
