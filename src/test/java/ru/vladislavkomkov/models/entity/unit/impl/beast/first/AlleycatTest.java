package ru.vladislavkomkov.models.entity.unit.impl.beast.first;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.entity.unit.UnitTestCase;
import ru.vladislavkomkov.models.entity.unit.impl.trash.beast.first.Cat;

public class AlleycatTest extends UnitTestCase {
    Unit unit = new Alleycat();

    @Test
    void testDefault(){
        super.testDefault(unit);
    }

    @Test
    protected void testOnPlayed() {
        unit.onPlayed(game, player, 0);
        assertEquals(new Cat().getName(), player.cloneTable()[1].getName());
    }
}
