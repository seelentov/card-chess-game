package ru.vladislavkomkov.model.entity.unit.impl.beast.sixth;

import org.junit.jupiter.api.Test;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.beast.second.SewerRat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.fight.Fight;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoldrinnTheGreatWolfTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new SewerRat());
  }
  
  @Test
  void testOnDead()
  {
    Unit unit11 = new GoldrinnTheGreatWolf(player);
    unit11.setIsTaunt(true);
    unit11.setHealth(1);
    player.addToTable(unit11);
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    
    player2.addToTable(new Cat(player2));
    
    Fight fight = new Fight(game, player, player2);
    fight.doTurn();
    
    List<Unit> units = fight.getFightTable(player);

    assertEquals(GoldrinnTheGreatWolf.ATTACK_BOOST + new Cat().getAttack(), units.get(0).getAttack());
    assertEquals(GoldrinnTheGreatWolf.ATTACK_BOOST + new Cat().getAttack(), units.get(1).getAttack());
    assertEquals(GoldrinnTheGreatWolf.ATTACK_BOOST + new Cat().getAttack(), units.get(2).getAttack());
    
    assertEquals(GoldrinnTheGreatWolf.HEALTH_BOOST + new Cat().getHealth(), units.get(0).getHealth());
    assertEquals(GoldrinnTheGreatWolf.HEALTH_BOOST + new Cat().getHealth(), units.get(1).getHealth());
    assertEquals(GoldrinnTheGreatWolf.HEALTH_BOOST + new Cat().getHealth(), units.get(2).getHealth());
  }
}
