package ru.vladislavkomkov.models.spell.impl;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.models.spell.Spell;
import ru.vladislavkomkov.models.spell.SpellTest;

import static org.junit.jupiter.api.Assertions.*;

public class TavernCoinTest extends SpellTest {
    Spell spell = new TavernCoin();
    
    @Test
    protected void testOnPlayed(){
        int money = player.getMoney();
        spell.onPlayed(game,player, 0);
        assertEquals(money + 1, player.getMoney());
    }
    
    @Test
    protected void testOnHanded(){
        super.onHanded(spell);
    }
}
