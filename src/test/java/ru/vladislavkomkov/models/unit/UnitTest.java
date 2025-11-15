package ru.vladislavkomkov.models.unit;

import static org.junit.jupiter.api.Assertions.*;

import ru.vladislavkomkov.GamePlayerTestCase;

public abstract class UnitTest extends GamePlayerTestCase {
    protected abstract void testOnPlayed();
    
    protected abstract void testOnHanded();
    
    protected abstract void testOnSell();
    
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
    
    protected void onSell(Unit unit){
        int money = player.getMoney();
        player.addToTable(unit, 0);
        assertNotNull(player.cloneTable()[0]);
        unit.onSell(game, player);
        assertEquals(money + 1, player.getMoney());
        assertNull(player.cloneTable()[0]);
    }
}
