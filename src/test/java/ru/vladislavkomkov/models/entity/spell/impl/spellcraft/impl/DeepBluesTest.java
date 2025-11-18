package ru.vladislavkomkov.models.entity.spell.impl.spellcraft.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.models.card.Card;
import ru.vladislavkomkov.models.entity.spell.impl.spellcraft.SpellCraftTestCase;
import ru.vladislavkomkov.models.entity.unit.Unit;

public class DeepBluesTest extends SpellCraftTestCase {
    @Test
    void testDefault(){
        super.testDefault(new DeepBlues());
    }

    @Test
    void testIncBoostAfterUse(){
        int unitIndex = 0;

        Unit unit = new Unit() {};

        player.addToTable(unit, unitIndex);

        for (int i = 1; i <= 100; i++) {
            player.addToHand(Card.of(new DeepBlues()));

            int tempAttack = unit.getAttack();
            int tempHealth = unit.getHealth();

            player.playCard(game,0,unitIndex);
            assertEquals(tempAttack + (DeepBlues.ATTACK_BOOST * i), unit.getAttack());
            assertEquals(tempHealth + (DeepBlues.HEALTH_BOOST * i), unit.getHealth());
        }
    }

    @Test
    void testDisappearOnStartTurnOnce (){
        int unitIndex = 0;

        Unit unit = new Unit() {};

        player.addToTable(unit, unitIndex);

        player.addToHand(Card.of(new DeepBlues()));

        int tempAttack = unit.getAttack();
        int tempHealth = unit.getHealth();

        player.playCard(game,0,unitIndex);

        assertEquals(tempAttack + (DeepBlues.ATTACK_BOOST), unit.getAttack());
        assertEquals(tempHealth + (DeepBlues.HEALTH_BOOST), unit.getHealth());

        game.processStartTurn(player);

        assertEquals(tempAttack, unit.getAttack());
        assertEquals(tempHealth, unit.getHealth());
    }
}
