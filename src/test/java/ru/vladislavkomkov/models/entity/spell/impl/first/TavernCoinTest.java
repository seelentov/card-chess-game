package ru.vladislavkomkov.models.entity.spell.impl.first;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.models.entity.spell.Spell;
import ru.vladislavkomkov.models.entity.spell.SpellTestCase;

public class TavernCoinTest extends SpellTestCase {
    @Test
    void testDefault(){
        super.testDefault(new TavernCoin());
    }
    
    @Test
    protected void testOnPlayed(){
        int money = player.getMoney();
        new TavernCoin().cast(game, player, 0);
        assertEquals(money + 1, player.getMoney());
    }
}
