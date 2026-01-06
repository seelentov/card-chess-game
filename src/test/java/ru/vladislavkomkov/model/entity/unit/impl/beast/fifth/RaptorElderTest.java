package ru.vladislavkomkov.model.entity.unit.impl.beast.fifth;

import org.junit.jupiter.api.Test;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Manasaber;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cubling;
import ru.vladislavkomkov.model.fight.Fight;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RaptorElderTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new RaptorElder(player));
  }
  
  @Test
  void testOnStartFight()
  {
    player.addToTable(new RaptorElder(player));
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    
    Fight fight = new Fight(game, player, player2);
    game.processStartFight(fight, player, player2);
    
    List<Unit> units = fight.getFightTable(player);
    
    assertEquals(new RaptorElder().getAttack() + RaptorElder.ATTACK_BOOST, units.get(0).getAttack());
    assertEquals(new RaptorElder().getHealth() + RaptorElder.HEALTH_BOOST, units.get(0).getHealth());
    
    assertEquals(new Cat().getAttack() + RaptorElder.ATTACK_BOOST, units.get(1).getAttack());
    assertEquals(new Cat().getHealth() + RaptorElder.HEALTH_BOOST, units.get(1).getHealth());
    
    assertEquals(new Cat().getAttack() + RaptorElder.ATTACK_BOOST, units.get(2).getAttack());
    assertEquals(new Cat().getHealth() + RaptorElder.HEALTH_BOOST, units.get(2).getHealth());
  }
  
  @Test
  void testOnSummoned()
  {
    player.addToTable(new Manasaber(player));
    player.addToTable(new Cat(player));
    player.addToTable(new RaptorElder(player));
    
    Unit unit21 = new Cat(player);
    unit21.setBaseAttack(99999);
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    game.processStartFight(fight, player, player2);
    
    fight.doTurn();
    
    List<Unit> units = fight.getFightTable(player);
    
    assertEquals(new Cubling().getAttack() + (RaptorElder.ATTACK_BOOST * 3), units.get(0).getAttack());
    assertEquals(new Cubling().getHealth() + (RaptorElder.HEALTH_BOOST * 3), units.get(0).getHealth());
    
    assertEquals(new Cubling().getAttack() + (RaptorElder.ATTACK_BOOST * 3), units.get(1).getAttack());
    assertEquals(new Cubling().getHealth() + (RaptorElder.HEALTH_BOOST * 3), units.get(1).getHealth());
    
    assertEquals(new Cat().getAttack() + (RaptorElder.ATTACK_BOOST * 3), units.get(2).getAttack());
    assertEquals(new Cat().getHealth() + (RaptorElder.HEALTH_BOOST * 3), units.get(2).getHealth());
    
    assertEquals(new RaptorElder().getAttack() + (RaptorElder.ATTACK_BOOST * 3), units.get(3).getAttack());
    assertEquals(new RaptorElder().getHealth() + (RaptorElder.HEALTH_BOOST * 3), units.get(3).getHealth());
  }
  
  @Test
  void testOnDisappear()
  {
    Unit unit12 = new RaptorElder(player);
    unit12.setIsDisguise(false);
    unit12.setIsTaunt(true);
    player.addToTable(unit12);
    
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    
    Unit unit21 = new Cat(player);
    unit21.setBaseAttack(99999);
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    game.processStartFight(fight, player, player2);
    
    fight.doTurn();
    
    List<Unit> units = fight.getFightTable(player);
    
    assertEquals(new Cat().getAttack(), units.get(0).getAttack());
    assertEquals(new Cat().getHealth(), units.get(0).getHealth());
    
    assertEquals(new Cat().getAttack(), units.get(1).getAttack());
    assertEquals(new Cat().getHealth(), units.get(1).getHealth());
  }
}
