package ru.vladislavkomkov.model.entity.unit.impl.beast.second;

import org.junit.jupiter.api.Test;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.fight.Fight;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HungrySnapjawTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new HungrySnapjaw());
  }
  
  @Test
  void testOnDead()
  {
    Unit unit11 = new Cat(player);
    unit11.setIsTaunt(true);
    player.addToTable(unit11);
    
    Unit unit12 = new Cat(player);
    unit12.setIsTaunt(true);
    player.addToTable(unit12);
    
    Unit unit13 = new HungrySnapjaw(player);
    player.addToTable(unit13);
    
    player2.addToTable(new Cat());
    player2.addToTable(new Cat());
    player2.addToTable(new Cat());
    
    Fight fight = new Fight(game, player, player2);
    
    fight.doTurn();
    
    assertEquals(
        HungrySnapjaw.HEALTH_BOOST + new HungrySnapjaw().getHealth(),
        fight.getFightTable(player).get(1).getHealth());
    assertEquals(
        HungrySnapjaw.HEALTH_BOOST + new HungrySnapjaw().getHealth(),
        player.getTable().get(2).getHealth());
    
    fight.doTurn();
    
    assertEquals(
        (HungrySnapjaw.HEALTH_BOOST * 2) + new HungrySnapjaw().getHealth(),
        fight.getFightTable(player).get(0).getHealth());
    assertEquals(
        (HungrySnapjaw.HEALTH_BOOST * 2) + new HungrySnapjaw().getHealth(),
        player.getTable().get(2).getHealth());
  }
  
  @Test
  void testOnDeadIfDead()
  {
    Unit unit11 = new Cat(player);
    player.addToTable(unit11);

    Unit unit12 = new HungrySnapjaw(player);
    unit12.setIsTaunt(true);
    player.addToTable(unit12);

    Unit unit13 = new Cat(player);
    player.addToTable(unit13);

    Unit unit14 = new Cat(player);
    player.addToTable(unit14);

    Unit unit15 = new Cat(player);
    player.addToTable(unit15);


    Unit unit21 = new Cat();
    unit21.setBaseAttack(2);
    player2.addToTable(unit21);

    Unit unit22 = new Cat();
    unit22.setBaseAttack(2);
    player2.addToTable(unit22);

    Unit unit23 = new Cat();
    unit23.setBaseAttack(2);
    player2.addToTable(unit23);
    
    Fight fight = new Fight(game, player, player2);

    fight.doTurn();
    fight.doTurn();
    fight.doTurn();
    fight.doTurn();
    fight.doTurn();
    fight.doTurn();

    assertEquals(new HungrySnapjaw().getHealth() + HungrySnapjaw.HEALTH_BOOST, player.getTable().get(1).getHealth());
  }
}
