package ru.vladislavkomkov.models.entity.unit.impl.demon.first;

import org.junit.jupiter.api.Test;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.entity.unit.UnitTestCase;
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
}
