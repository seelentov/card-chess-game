package ru.vladislavkomkov.model.entity.unit.impl.beast.fifth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Mechapony;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Mechorse;
import ru.vladislavkomkov.model.fight.Fight;

public class MechanizedGiftHorseTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new MechanizedGiftHorse(player));
  }
  
  @Test
  void testOnDead()
  {
    player.addToTable(new MechanizedGiftHorse(player));
    
    Unit unit21 = new Cat(player2);
    unit21.setBaseAttack(99999);
    unit21.setHealth(99999);
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    
    fight.doTurn();
    
    List<Unit> units = fight.getFightTable(player);
    
    units.forEach(unit -> assertEquals(new Mechorse().getName(), unit.getName()));
    units.forEach(unit -> unit.setIsTaunt(true));
    
    fight.doTurn();
    fight.doTurn();
    
    units.forEach(unit -> assertEquals(new Mechapony().getName(), unit.getName()));
  }
}
