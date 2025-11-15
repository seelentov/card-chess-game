package ru.vladislavkomkov.models.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.GamePlayerTestCase;

public abstract class UnitTest extends GamePlayerTestCase {
    @Test
    protected abstract void testOnPlayed();
    
    @Test
    protected abstract void testOnHanded();
    
    protected void onPlayed(Unit unit){
        assertNull(player.cloneTable()[0]);
        unit.onPlayed(game, player, 0);
        assertEquals(unit.getName(), player.cloneTable()[0].getName());
    }
    
    protected void onHanded(Unit unit){
        assertNull(player.cloneHand()[0]);
        unit.onHandled(game, player);
        assertEquals(unit.getName(), player.cloneHand()[0].get().getName());
    }
}
