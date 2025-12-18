package ru.vladislavkomkov.model.entity.unit.impl.demon.first;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.demon.first.Imp;

public class IckyImpTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new IckyImp());
  }
  
  @Test
  void testOnDead()
  {
    Unit unit = new IckyImp();
    player.addToTable(unit, -1);
    unit.onDead(game, null,player, player2, new Cat());
    
    assertEquals(3, player.getUnitsCount());
    assertEquals(new Imp().getName(), player.cloneTable().get(1).getName());
    assertEquals(new Imp().getName(), player.cloneTable().get(2).getName());
  }
  
  @Test
  void testOnDeadGold()
  {
    Unit unit = new IckyImp().buildGold();
    player.addToTable(unit, -1);
    unit.onDead(game, null,player, player2, new Cat());
    
    assertEquals(3, player.getUnitsCount());
    assertEquals(new IckyImp().getName(), player.cloneTable().get(0).getName());
    assertEquals(new Imp().getName(), player.cloneTable().get(1).getName());
    assertEquals(new Imp().getName(), player.cloneTable().get(2).getName());
    
    assertEquals(new Imp().getAttack() * 2, player.cloneTable().get(1).getAttack());
    assertEquals(new Imp().getHealth() * 2, player.cloneTable().get(1).getHealth());
    
    assertEquals(new Imp().getAttack() * 2, player.cloneTable().get(2).getAttack());
    assertEquals(new Imp().getHealth() * 2, player.cloneTable().get(2).getHealth());
  }
  
  @Test
  protected void testOnDeadByIndex()
  {
    player.addToTable(new Cat());
    
    Unit unit = new IckyImp();
    player.addToTable(unit);
    
    for (int i = 0; i < 4; i++)
    {
      player.addToTable(new Cat());
    }
    
    unit.onDead(game, null,player, player2, new Cat());
    player.getTable().removeIf(unit1 -> unit1 == unit);
    
    assertEquals(new Cat().getName(), player.cloneTable().get(0).getName());
    assertEquals(new Imp().getName(), player.cloneTable().get(1).getName());
    assertEquals(new Imp().getName(), player.cloneTable().get(2).getName());
    
    for (int i = 3; i < 7; i++)
    {
      assertEquals(new Cat().getName(), player.cloneTable().get(i).getName());
    }
  }
  
  @Test
  protected void testOnDeadOverflow()
  {
    player.addToTable(new Cat());
    
    Unit unit = new IckyImp();
    player.addToTable(unit);
    
    for (int i = 0; i < 5; i++)
    {
      player.addToTable(new Cat());
    }
    
    unit.onDead(game, null,player, player2, new Cat());
    player.getTable().removeIf(unit1 -> unit1 == unit);
    
    assertEquals(new Cat().getName(), player.cloneTable().get(0).getName());
    assertEquals(new Imp().getName(), player.cloneTable().get(1).getName());
    
    for (int i = 2; i < 7; i++)
    {
      assertEquals(new Cat().getName(), player.cloneTable().get(i).getName());
    }
  }
}
