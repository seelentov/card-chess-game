package ru.vladislavkomkov.models.unit.impl;

import org.junit.jupiter.api.Test;
import ru.vladislavkomkov.models.unit.Unit;
import ru.vladislavkomkov.models.unit.UnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccordoTronTest extends UnitTest {
    Unit unit = new AccordoTron();

    @Test
    void testDefault(){
        super.testDefault(unit);
    }

    @Test
    protected void testOnStartTurn() {
        int initMoney = player.getMoney();
        player.addToTable(unit,0);
        game.processStartTurn(player);
        assertEquals(initMoney + 1, player.getMoney());
    }
}
