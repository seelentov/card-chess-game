package ru.vladislavkomkov.model.entity.unit.impl.beast.second;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Manasaber;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cubling;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

public class HummingBirdTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new HummingBird());
  }
  
  @Test
  void testOnStartFight()
  {
    player.addToTable(new HummingBird());
    
    for (int i = 0; i < 3; i++)
    {
      player.addToTable(new Cat());
    }
    Fight fight = new Fight(game, player, player2);
    game.processStartFight(fight, player, player2);
    
    Unit bird = player.getTable().get(0);
    assertEquals(new HummingBird().getName(), bird.getName());
    assertEquals(new HummingBird().getAttack(), bird.getAttack());
    
    for (int i = 1; i < 4; i++)
    {
      Unit unit = fight.getFightTable(player).get(i);
      assertEquals(new Cat().getName(), unit.getName());
      assertEquals(new Cat().getAttack() + HummingBird.ATTACK_BOOST, unit.getAttack());
    }
  }
  
  @Test
  void testOnStartFightGold()
  {
    player.addToTable(new HummingBird().buildGold());
    
    for (int i = 0; i < 3; i++)
    {
      player.addToTable(new Cat());
    }
    Fight fight = new Fight(game, player, player2);
    game.processStartFight(fight, player, player2);
    
    Unit bird = player.getTable().get(0);
    assertEquals(new HummingBird().getName(), bird.getName());
    assertEquals(new HummingBird().newGold().getAttack(), bird.getAttack());
    
    for (int i = 1; i < 4; i++)
    {
      Unit unit = fight.getFightTable(player).get(i);
      assertEquals(new Cat().getName(), unit.getName());
      assertEquals(new Cat().getAttack() + (HummingBird.ATTACK_BOOST * 2), unit.getAttack());
    }
  }
  
  @Test
  void testAppearUnits()
  {
    player.addToTable(new Manasaber());
    player.addToTable(new HummingBird());

    player2.addToTable(new Cat());
    player2.addToTable(new Cat());

    Fight fight = new Fight(game, player, player2);
    game.processStartFight(fight, player, player2);

    fight.doTurn();

    assertEquals(new Cubling().getName(), fight.getFightTable(player).get(0).getName());
    assertEquals(new Cubling().getAttack() + HummingBird.ATTACK_BOOST, fight.getFightTable(player).get(0).getAttack());
    assertEquals(new Cubling().getHealth(), fight.getFightTable(player).get(0).getHealth());

    assertEquals(new Cubling().getName(), fight.getFightTable(player).get(1).getName());
    assertEquals(new Cubling().getAttack() + HummingBird.ATTACK_BOOST, fight.getFightTable(player).get(1).getAttack());
    assertEquals(new Cubling().getHealth(), fight.getFightTable(player).get(1).getHealth());

    fight.doTurn();

    assertEquals(new Cubling().getName(), fight.getFightTable(player).get(0).getName());
    assertEquals(new Cubling().getAttack() + HummingBird.ATTACK_BOOST, fight.getFightTable(player).get(0).getAttack());
    assertEquals(new Cubling().getHealth(), fight.getFightTable(player).get(0).getHealth());
  }
}
