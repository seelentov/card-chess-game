package ru.vladislavkomkov.models.entity.unit.impl.demon.first;

import org.junit.jupiter.api.Test;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.entity.unit.UnitTestCase;
import ru.vladislavkomkov.models.entity.unit.impl.beast.first.Alleycat;
import ru.vladislavkomkov.models.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.models.entity.unit.impl.trash.demon.first.Imp;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IckyImpTest extends UnitTestCase {
    @Test
    void testDefault(){
        super.testDefault(new IckyImp());
    }

    @Test
    void testOnDead(){
        Unit unit = new IckyImp();
        player.addToTable(unit, -1);
        unit.onDead(game, player, player2, new Cat());

        assertEquals(3, player.getUnitsCount());
        assertEquals(new Imp().getName(), player.cloneTable().get(1).getName());
        assertEquals(new Imp().getName(), player.cloneTable().get(2).getName());
    }
    
    @Test
    protected void testOnDeadByIndex() {
        player.addToTable(new Cat());
        
        Unit unit = new IckyImp();
        player.addToTable(unit);
        
        for (int i = 0; i < 4; i++) {
            player.addToTable(new Cat());
        }
        
        unit.onDead(game,player,player2,new Cat());
        
        assertEquals(new Cat().getName(), player.cloneTable().get(0).getName());
        assertEquals(new IckyImp().getName(), player.cloneTable().get(1).getName());
        assertEquals(new Imp().getName(), player.cloneTable().get(2).getName());
        
        for (int i = 3; i < 7; i++) {
            assertEquals(new Cat().getName(), player.cloneTable().get(i).getName());
        }
    }
    
    @Test
    protected void testOnDeadOverflow() {
        player.addToTable(new Cat());
        
        Unit unit = new IckyImp();
        player.addToTable(unit);
        
        for (int i = 0; i < 5; i++) {
            player.addToTable(new Cat());
        }
        
        unit.onDead(game,player,player2,new Cat());
        
        assertEquals(new Cat().getName(), player.cloneTable().get(0).getName());
        assertEquals(new IckyImp().getName(), player.cloneTable().get(1).getName());
        
        for (int i = 2; i < 7; i++) {
            assertEquals(new Cat().getName(), player.cloneTable().get(i).getName());
        }
    }
}
