package ru.vladislavkomkov.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.GamePlayerTestCase;
import ru.vladislavkomkov.models.card.Card;
import ru.vladislavkomkov.models.card.SpellCard;
import ru.vladislavkomkov.models.card.UnitCard;
import ru.vladislavkomkov.models.spell.impl.TavernCoin;
import ru.vladislavkomkov.models.unit.Unit;
import ru.vladislavkomkov.models.unit.impl.Alleycat;
import ru.vladislavkomkov.models.unit.impl.Cat;

import java.util.Arrays;
import java.util.Objects;

public class PlayerTest extends GamePlayerTestCase {
    @Test
    void testCloneHand(){
        for (int i = 0; i < 2; i++) {
            player.addToHand(Card.of(new Cat()));
        }
        
        Card[] hand = player.cloneHand();
        
        assertEquals(new Cat().getName(), hand[0].get().getName());
        assertEquals(new Cat().getName(), hand[1].get().getName());
        
        for (int i = 2; i < 10; i++) {
            assertNull(hand[i]);
        }
    }
    
    @Test
    void testCloneTable(){
        for (int i = 0; i < 2; i++) {
            player.addToTable(new Cat(), i);
        }
        
        Unit[] units = player.cloneTable();
        
        assertEquals(new Cat().getName(), units[0].getName());
        assertEquals(new Cat().getName(), units[1].getName());
        
        for (int i = 2; i < 7; i++) {
            assertNull(units[i]);
        }
    }

    @Test
    void testAddSingleToHand(){
        player = new Player();
        player.addToHand(new SpellCard(new TavernCoin()));

        assertEquals(1, Arrays.stream(player.cloneHand()).filter(Objects::nonNull).count());
        assertEquals(player.cloneHand()[0].get().getName(), new TavernCoin().getName());
    }

    @Test
    void testAddManyToHand(){
        player = new Player();
        for (int i = 0; i < 20; i++) {
            if(i % 2 == 0){
                player.addToHand(new SpellCard(new TavernCoin()));
            } else {
                player.addToHand(new UnitCard(new Alleycat()));
            }
        }

        player.addToHand(new UnitCard(new Alleycat()));

        Card[] cards = player.cloneHand();

        assertEquals(10, Arrays.stream(cards).filter(Objects::nonNull).count());

        for (int i = 0; i < 10; i++) {
            String cardName = cards[i].get().getName();
            if(i % 2 == 0){
                assertEquals(cardName, new TavernCoin().getName());
            } else {
                assertEquals(cardName, new Alleycat().getName());
            }
        }
    }

    @Test
    void testBuyCard(){
        player.getTavern().add(Card.of(new Alleycat()));
        player.buyCard(game, 0);
        assertEquals(new Alleycat().getName(), player.cloneHand()[0].get().getName());
    }
    
    @Test
    void testPlaySpellCard(){
        assertEquals(3, player.getMoney());

        player.addToHand(Card.of(new TavernCoin()));

        player.playCard(game, 0, 0);

        assertEquals(4, player.getMoney());
        assertEquals(0, Arrays.stream(player.cloneHand()).filter(Objects::nonNull).count());
    }
    
    @Test
    void testPlayUnitCard(){
        player.addToHand(Card.of(new Alleycat()));
        
        player.playCard(game, 0, 0);
        
        assertEquals(0, Arrays.stream(player.cloneHand()).filter(Objects::nonNull).count());
        
        assertEquals(2, Arrays.stream(player.cloneTable()).filter(Objects::nonNull).count());
        
        Unit unit1 = player.cloneTable()[0];
        Unit unit2 = player.cloneTable()[1];
        
        assertEquals(new Alleycat().getName(), unit1.getName());
        assertEquals(new Cat().getName(), unit2.getName());
    }
    
    @Test
    void testAddToTable(){
        player.addToTable(new Alleycat(), 1);
        
        assertEquals(new Alleycat().getName(), player.cloneTable()[1].getName());
    }
    
    @Test
    void testDoForAll(){
        player.addToTable(new Cat(), 1);
        player.addToTable(new Cat(), 1);

        player.doForAll(unit -> {
            unit.incHealth(2);
            unit.incAttack(2);
        });
        
        Unit[] units = player.cloneTable();
        
        for (int i = 0; i < 2; i++) {
            assertEquals(3,units[i].getAttack());
        }
    }
}
