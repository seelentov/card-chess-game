package ru.vladislavkomkov.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UnitUtilsTest {
    @Test
    void testGetUnits(){
        var units = UnitUtils.getUnits();

        assertNotNull(units);
        assertFalse(units.isEmpty());
    }

    @Test
    void testGetTavernUnits(){
        var units = UnitUtils.getTavernUnits();

        assertNotNull(units);
        units.forEach(unit -> assertTrue(unit.isTavern()));
    }

    @Test
    void testGetTavernUnitsByLvl(){
        for (int level = 1; level <= 6; level++) {
            int fLevel = level;

            var units = UnitUtils.getUnitsByTavern(fLevel);

            assertNotNull(units);
            units.forEach(unit -> assertEquals(fLevel, unit.getLevel()));
        }
    }
}
