package ru.vladislavkomkov.models.unit.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.models.unit.Unit;
import ru.vladislavkomkov.models.unit.UnitTest;

public class AlleycatTest extends UnitTest {
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
