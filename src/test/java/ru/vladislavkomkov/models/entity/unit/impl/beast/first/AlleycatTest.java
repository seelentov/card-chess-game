package ru.vladislavkomkov.models.entity.unit.impl.beast.first;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.entity.unit.UnitTestCase;
import ru.vladislavkomkov.models.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.models.entity.unit.impl.trash.demon.first.Imp;

public class AlleycatTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new Alleycat());
  }
  
  @Test
  protected void testOnPlayed()
  {
    new Alleycat().onPlayed(game, player, 0);
    assertEquals(new Cat().getName(), player.cloneTable().get(0).getName());
  }
  
  @Test
  protected void testOnPlayedByIndex()
  {
    player.addToTable(new Imp());
    
    Unit unit = new Alleycat();
    player.addToTable(unit);
    
    for (int i = 0; i < 4; i++)
    {
      player.addToTable(new Imp());
    }
    
    unit.onPlayed(game, player);
    
    assertEquals(new Imp().getName(), player.cloneTable().get(0).getName());
    assertEquals(new Alleycat().getName(), player.cloneTable().get(1).getName());
    assertEquals(new Cat().getName(), player.cloneTable().get(2).getName());
    
    for (int i = 3; i < 7; i++)
    {
      assertEquals(new Imp().getName(), player.cloneTable().get(i).getName());
    }
  }
  
  @Test
  protected void testOnPlayedOverflow()
  {
    player.addToTable(new Imp());
    
    Unit unit = new Alleycat();
    player.addToTable(unit);
    
    for (int i = 0; i < 5; i++)
    {
      player.addToTable(new Imp());
    }
    
    unit.onPlayed(game, player);
    
    assertEquals(new Imp().getName(), player.cloneTable().get(0).getName());
    assertEquals(new Alleycat().getName(), player.cloneTable().get(1).getName());
    
    for (int i = 2; i < 7; i++)
    {
      assertEquals(new Imp().getName(), player.cloneTable().get(i).getName());
    }
  }
}
