package ru.vladislavkomkov.models.entity.unit.impl.beast.first;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.models.entity.unit.UnitTestCase;
import ru.vladislavkomkov.models.entity.unit.impl.trash.beast.first.Cat;

public class AlleycatTest extends UnitTestCase {
    @Test
    void testDefault(){
        super.testDefault(new Alleycat());
    }

    @Test
    protected void testOnPlayed() {
        new Alleycat().onPlayed(game, player, 0);
        assertEquals(new Cat().getName(), player.cloneTable()[1].getName());
    }
}
