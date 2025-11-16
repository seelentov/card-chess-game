package ru.vladislavkomkov.models.entity.unit.impl.mech.fourth;

import org.junit.jupiter.api.Test;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.entity.unit.impl.mech.MechTestCase;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccordoTronTest extends MechTestCase {
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
