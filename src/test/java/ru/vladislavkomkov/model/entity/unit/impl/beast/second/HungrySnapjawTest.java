package ru.vladislavkomkov.model.entity.unit.impl.beast.second;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

public class HungrySnapjawTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new HummingBird());
  }
  
  @Test
  void testOnAnotherDead()
  {
    Unit cat = new Cat();
    player.addToTable(cat);
    Unit hs = new HungrySnapjaw();
    player.addToTable(hs);
    hs.onPlayed(game, player);
    
    cat.onDead(game, player, player2, new Cat());
    
    assertEquals(new HungrySnapjaw().getHealth() + HungrySnapjaw.ATTACK_BOOST, hs.getHealth());
  }
  
  @Test
  void testOnAnotherDeadGold()
  {
    Unit cat = new Cat();
    player.addToTable(cat);
    Unit hs = new HungrySnapjaw().buildGold();
    player.addToTable(hs);
    hs.onPlayed(game, player);
    
    cat.onDead(game, player, player2, new Cat());
    
    assertEquals(new HungrySnapjaw().getHealth() + (HungrySnapjaw.ATTACK_BOOST * 2), hs.getHealth());
  }
}
