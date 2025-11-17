package ru.vladislavkomkov.models.entity.spell;

import static org.junit.jupiter.api.Assertions.*;

import ru.vladislavkomkov.GamePlayerTestCase;

public abstract class SpellTestCase extends GamePlayerTestCase {
    protected void testDefault(Spell spell){
        onHandled(spell);
    };
    
    void onHandled(Spell spell){
        setUp();
        
        spell.onHandled(game, player);
        assertEquals(spell.getName(), player.cloneHand().get(0).get().getName());
        
        tearDown();
    }
}
