package ru.vladislavkomkov.models.entity.unit.impl.mech.fourth;

import org.junit.jupiter.api.Test;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.entity.unit.impl.mech.Mech;
import ru.vladislavkomkov.models.entity.unit.impl.mech.MechTestCase;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccordoTronTest extends MechTestCase {
    @Test
    void testDefault(){
        testDefault(new AccordoTron());
    }

    @Test
    protected void testOnStartTurn() {
        int initMoney = player.getMoney();
        player.addToTable(new AccordoTron(),0);
        game.processStartTurn(player);
        assertEquals(initMoney + 1, player.getMoney());
    }
}
