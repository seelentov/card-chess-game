package ru.vladislavkomkov.models.entity.spell;

import static org.junit.jupiter.api.Assertions.*;

import ru.vladislavkomkov.GamePlayerTestCase;

public abstract class SpellTestCase extends GamePlayerTestCase {
    protected abstract void testOnPlayed();
    
    protected abstract void testOnHanded();
    
    protected void onHanded(Spell spell){
        assertNull(player.cloneHand()[0]);
        spell.onHandled(game, player);
        assertEquals(spell.getName(), player.cloneHand()[0].get().getName());
    }
}
