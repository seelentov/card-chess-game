package ru.vladislavkomkov.model.entity.unit.impl.beast.first;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.demon.first.Imp;

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
    new Alleycat();
    Unit cat = new Alleycat();
    player.addToTable(cat);
    cat.onPlayed(game, null, player);
    
    assertEquals(new Cat().getName(), player.cloneTable().get(1).getName());
  }
  
  @Test
  protected void testOnPlayedGold()
  {
    Unit cat = new Alleycat().buildGold();
    player.addToTable(cat);
    cat.onPlayed(game, null, player);
    
    assertEquals(new Cat().getName(), player.getTable().get(1).getName());
    
    assertEquals(new Cat().getAttack() * 2, player.cloneTable().get(0).getAttack());
    assertEquals(new Cat().getHealth() * 2, player.cloneTable().get(0).getHealth());
    
    assertTrue(player.cloneTable().get(0).isGold());
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
    
    unit.onPlayed(game, null, player);
    
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
    
    unit.onPlayed(game, null, player);
    
    assertEquals(new Imp().getName(), player.cloneTable().get(0).getName());
    assertEquals(new Alleycat().getName(), player.cloneTable().get(1).getName());
    
    for (int i = 2; i < 7; i++)
    {
      assertEquals(new Imp().getName(), player.cloneTable().get(i).getName());
    }
  }
  
  @Test
  void testPlayBetween()
  {
    player.addToTable(new Alleycat());
    player.addToTable(new Cat());
    
    player.addToHand(Card.of(new Alleycat()));
    player.playCard(0, 1);
    
    assertEquals(new Alleycat().getName(), player.getTable().get(0).getName());
    assertEquals(new Alleycat().getName(), player.getTable().get(1).getName());
    assertEquals(new Cat().getName(), player.getTable().get(2).getName());
    assertEquals(new Cat().getName(), player.getTable().get(2).getName());
  }
}
