package ru.vladislavkomkov.model.entity.unit.impl.beast.fourth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.fight.Fight;

public class StompingStegodonTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new StompingStegodon());
  }
  
  @Test
  void testOnAttack()
  {
      player.addToTable(new StompingStegodon(player));
      player.addToTable(new Cat(player));
      player.addToTable(new Cat(player));
      player.addToTable(new Cat(player));
      player.addToTable(new Cat(player));
      player.addToTable(new Cat(player));
      player.addToTable(new Cat(player));
      
      Unit unit21 = new Cat(player2);
      unit21.setBaseAttack(0);
      unit21.setHealth(999999);
      player2.addToTable(unit21);
      
      Fight fight = new Fight(game, player, player2);
      
      for (int i = 1; i <= 7; i++) {
          fight.doTurn();
          fight.doTurn();
          
          List<Unit> table = fight.getFightTable(player);
          
          for (int j = 1; j < table.size(); j++) {
            if(j == i - 1){
                continue;
            }
              
              assertEquals(table.get(j).getAttack());
            
          }
      }
  }
}
