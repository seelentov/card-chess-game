package ru.vladislavkomkov.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SpellServiceTest {
    @Test
    void testGetAll(){
        var units = SpellService.getAll();

        assertNotNull(units);
        assertFalse(units.isEmpty());
    }

    @Test
    void testGetTavern(){
        var units = SpellService.getTavern();

        assertNotNull(units);
        units.forEach(unit -> assertTrue(unit.isTavern()));
    }

    @Test
    void testGetTavernByLvl(){
        for (int level = 1; level <= 6; level++) {
            int fLevel = level;

            var units = SpellService.getByTavern(fLevel);

            assertNotNull(units);
            units.forEach(unit -> assertEquals(fLevel, unit.getLevel()));
        }
    }
}
