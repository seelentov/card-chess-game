package ru.vladislavkomkov.model.entity.unit.impl.beast.fourth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.demon.first.IckyImp;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.demon.first.Imp;
import ru.vladislavkomkov.model.fight.Fight;

public class RylakMetalheadTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new RylakMetalhead());
  }
  
  @Test
  void testOnDeadLeft()
  {
    player.addToTable(new IckyImp(player));
    player.addToTable(new RylakMetalhead(player));
    player.addToTable(new Cat(player));
    
    Unit unit21 = new Cat(player2);
    unit21.setBaseAttack(10000);
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    fight.doTurn();
    
    assertEquals(new Imp().getName(), fight.getFightTable(player).get(1).getName());
    assertEquals(new Imp().getName(), fight.getFightTable(player).get(2).getName());
  }
  
  @Test
  void testOnDeadRight()
  {
    player.addToTable(new Cat(player));
    player.addToTable(new RylakMetalhead(player));
    player.addToTable(new IckyImp(player));
    
    Unit unit21 = new Cat(player2);
    unit21.setBaseAttack(10000);
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    fight.doTurn();
    
    assertEquals(new Imp().getName(), fight.getFightTable(player).get(2).getName());
    assertEquals(new Imp().getName(), fight.getFightTable(player).get(3).getName());
  }
  
  @Test
  void testOnDeadNone()
  {
    player.addToTable(new Cat(player));
    player.addToTable(new RylakMetalhead(player));
    player.addToTable(new Cat(player));
    
    Unit unit21 = new Cat(player2);
    unit21.setBaseAttack(10000);
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    fight.doTurn();
    
    assertEquals(new Cat().getName(), fight.getFightTable(player).get(0).getName());
    assertEquals(new Cat().getName(), fight.getFightTable(player).get(1).getName());
  }
  
  @Test
  void testOnDeadBoth()
  {
    player.addToTable(new IckyImp(player));
    player.addToTable(new RylakMetalhead(player).buildGold());
    player.addToTable(new IckyImp(player));
    
    Unit unit21 = new Cat(player2);
    unit21.setBaseAttack(10000);
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    fight.doTurn();
    
    assertEquals(new Imp().getName(), fight.getFightTable(player).get(1).getName());
    assertEquals(new Imp().getName(), fight.getFightTable(player).get(2).getName());
    assertEquals(new Imp().getName(), fight.getFightTable(player).get(4).getName());
    assertEquals(new Imp().getName(), fight.getFightTable(player).get(5).getName());
  }
}
