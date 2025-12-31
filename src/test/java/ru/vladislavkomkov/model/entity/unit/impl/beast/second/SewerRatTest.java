package ru.vladislavkomkov.model.entity.unit.impl.beast.second;

import org.junit.jupiter.api.Test;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.fight.Fight;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SewerRatTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new SewerRat());
  }
  
  @Test
  void testOnDead()
  {
    player.addToTable(new SewerRat(player));
    player2.addToTable(new Cat());
    player2.addToTable(new Cat());
    
    Fight fight = new Fight(game, player, player2);
    
    fight.doTurn();
    fight.doTurn();
    
    assertEquals(new SewerRat.HalfShell().getName(), fight.getFightTable(player).get(0).getName());
  }
  
  @Test
  void testOnDeadGold()
  {
    player.addToTable(new SewerRat(player).buildGold());
    player2.addToTable(new Cat());
    player2.addToTable(new Cat());
    player2.addToTable(new Cat());
    player2.addToTable(new Cat());
    
    Fight fight = new Fight(game, player, player2);
    
    fight.doTurn();
    fight.doTurn();
    fight.doTurn();
    fight.doTurn();
    
    assertEquals(new SewerRat.HalfShell().getName(), fight.getFightTable(player).get(0).getName());
    assertEquals(new SewerRat.HalfShell().buildGold().getAttack(), fight.getFightTable(player).get(0).getAttack());
  }
}
