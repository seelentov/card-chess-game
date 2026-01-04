package ru.vladislavkomkov.model.entity.unit.impl.beast.fifth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.fight.Fight;

public class NiuzaoTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new Niuzao(player));
  }
  
  @Test
  void testOnAttack()
  {
    player.addToTable(new Niuzao(player));
    
    player2.addToTable(new Cat(player2));
    player2.addToTable(new Cat(player2));
    player2.addToTable(new Cat(player2));
    
    Fight fight = new Fight(game, player, player2);
    
    fight.doTurn();
    
    assertEquals(1, fight.getFightTable(player2).size());
  }
  
  @Test
  void testOnAttackGold()
  {
    player.addToTable(new Niuzao(player).buildGold());
    
    player2.addToTable(new Cat(player2));
    player2.addToTable(new Cat(player2));
    player2.addToTable(new Cat(player2));
    player2.addToTable(new Cat(player2));
    
    Fight fight = new Fight(game, player, player2);
    
    fight.doTurn();
    
    assertEquals(1, fight.getFightTable(player2).size());
  }
}
