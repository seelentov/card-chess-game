package ru.vladislavkomkov.model.entity.unit.impl.none.third;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cubling;
import ru.vladislavkomkov.model.fight.Fight;

public class BirdBuddyTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    testDefault(new BirdBuddy());
  }
    
    @Test
    void testOnDead()
    {
        player.addToTable(new Cat(player));
        player.addToTable(new Cat(player));
        player.addToTable(new Cat(player));
        player.addToTable(new Cat(player));
        player.addToTable(new Cat(player));
        player.addToTable(new Cat(player));
        
        player.getTable().forEach(unit -> unit.setIsTaunt(true));
        
        player.addToTable(new BirdBuddy(player));
        
        Unit unit21 = new Cat();
        unit21.setBaseAttack(999);
        unit21.setHealth(999);
        player2.addToTable(unit21);
        
        Fight fight = new Fight(game, player, player2);
        
        for (int i = 1; i < player.getTable().size(); i++)
        {
            int finalI = i;
            fight.getFightTable(player).stream().filter(unit -> unit instanceof Cat).forEach(unit -> {
                assertEquals(finalI, unit.getAttack());
                assertEquals(finalI, unit.getHealth());
            });
            
            fight.doTurn();
        }
    }
    
    @Test
    void testOnDeadIfDead()
    {
        player.addToTable(new Cat(player));
        player.addToTable(new Cat(player));
        player.addToTable(new BirdBuddy(player));
        player.addToTable(new Cat(player));
        player.addToTable(new Cat(player));
        player.addToTable(new Cat(player));
        
        Unit unit17 = new Cubling();
        unit17.setBaseAttack(0);
        unit17.setHealth(999999);
        unit17.setIsTaunt(true);
        player.addToTable(unit17);
        
        Unit unit21 = new Cat();
        unit21.setBaseAttack(999);
        unit21.setHealth(999);
        player2.addToTable(unit21);
        
        Fight fight = new Fight(game, player, player2);
        
        int fights = 4;
        
        for (int i = 1; i < fights; i+=1)
        {
            int finalI = i;
            fight.getFightTable(player).stream().filter(unit -> unit instanceof Cat).forEach(unit -> {
                assertEquals(finalI, unit.getAttack());
                assertEquals(finalI, unit.getHealth());
            });
            
            fight.doTurn();
            fight.doTurn();
        }
        
        for (int i = 0; i < fight.getFightTable(player).size(); i++)
        {
            fight.getFightTable(player).stream().filter(unit -> unit instanceof Cat).forEach(unit -> {
                assertEquals(fights - 1, unit.getAttack());
                assertEquals(fights - 1, unit.getHealth());
            });
            
            fight.doTurn();
        }
    }
}
