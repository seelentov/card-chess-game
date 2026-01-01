package ru.vladislavkomkov.model.entity.unit.impl.beast.fourth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Manasaber;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.fight.Fight;

public class MamaBearTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new MamaBear());
  }
  
  @Test
  void testOnSummonedFight()
  {
    player.addToTable(new MamaBear(player));
    
    player2.addToTable(new Cat());
    
    Fight fight = new Fight(game, player, player2);
    
    fight.addToFightTable(player, new Cat(), 1, false);
    
    assertEquals(new Cat().getAttack() + MamaBear.ATTACK_BOOST, fight.getFightTable(player).get(1).getAttack());
  }
  
  @Test
  void testOnSummonedFightDisappear()
  {
      Unit unit = new MamaBear(player);
      player.addToTable(unit);
      
      player2.addToTable(new Cat());
      
      Fight fight = new Fight(game, player, player2);
      
      fight.addToFightTable(player, new Cat(), 1, false);
      
      fight.removeFromFightTable(player, 0);
      
      fight.addToFightTable(player, new Cat(), 1, false);
      
      assertEquals(new Cat().getAttack() + MamaBear.ATTACK_BOOST, fight.getFightTable(player).get(0).getAttack());
      assertEquals(new Cat().getAttack(), fight.getFightTable(player).get(1).getAttack());
  }
  
  @Test
  void testOnSummonedPrepare()
  {
    player.addToTable(new MamaBear(player));
    player.addToTable(new Cat(player));
    
    assertEquals(new Cat().getAttack() + MamaBear.ATTACK_BOOST, player.getTable().get(1).getAttack());
    assertEquals(new Cat().getHealth() + MamaBear.HEALTH_BOOST, player.getTable().get(1).getHealth());
  }
  
  @Test
  void testOnSummonedPrepareDisappear()
  {
    player.addToTable(new MamaBear(player));
    player.addToTable(new Cat(player));
    
    player.sellCard(0);
    
    player.addToTable(new Cat(player));
    
    assertEquals(new Cat().getAttack() + MamaBear.ATTACK_BOOST, player.getTable().get(0).getAttack());
    assertEquals(new Cat().getHealth() + MamaBear.HEALTH_BOOST, player.getTable().get(0).getHealth());
    
    assertEquals(new Cat().getAttack(), player.getTable().get(1).getAttack());
    assertEquals(new Cat().getHealth(), player.getTable().get(1).getHealth());
  }
}
