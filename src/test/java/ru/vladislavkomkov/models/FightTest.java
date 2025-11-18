package ru.vladislavkomkov.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.GamePlayerTestCase;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.models.player.Player;

public class FightTest extends GamePlayerTestCase {
    @Test
    void testFightDraw(){
        Unit unit = new Cat();
        unit.incAttack(2);
        unit.incHealth(6);
        
        Unit unit2 = new Cat();
        unit2.incAttack(2);
        unit2.incHealth(6);
        
        player.addToTable(unit, 0);
        player2.addToTable(unit2, 0);
        
        Fight fight = new Fight(game, player, player2);
        
        for (int i = 1; i <= 3; i++) {
            assertFalse(fight.doTurn());
            
            assertEquals(unit.getMaxHealth() - (unit2.getAttack() * i), unit.getHealth());
            assertEquals(unit2.getMaxHealth() - (unit.getAttack() * i), unit2.getHealth());
        }
        
        assertTrue(fight.doTurn());
    }
    
    @Test
    void testFightWin(){
        Unit unit = new Cat();
        unit.incAttack(2);
        unit.incHealth(6);
        
        Unit unit2 = new Cat();
        unit2.incAttack(2);
        unit2.incHealth(5);
        
        player.addToTable(unit, 0);
        player2.addToTable(unit2, 0);
        
        Fight fight = new Fight(game, player, player2);
        
        for (int i = 1; i <= 2; i++) {
            assertFalse(fight.doTurn());
            
            assertEquals(unit.getMaxHealth() - (unit2.getAttack() * i), unit.getHealth());
            assertEquals(unit2.getMaxHealth() - (unit.getAttack() * i), unit2.getHealth());
        }
        
        assertTrue(fight.doTurn());
        
        assertEquals(player2.getMaxHealth() - (unit.getLevel() + player.getLevel()), player2.getHealth());
    }
    
    @Test
    void testFightDestruction(){
        for (int i = 0; i < 3; i++) {
            player.addToTable(new Cat(), -1);
        }
        
        for (int i = 0; i < 3; i++) {
            player2.addToTable(new Cat(), 0);
        }
        
        Fight fight = new Fight(game, player, player2);
        
        for (int i = 2; i >= 0; i--) {
            assertFalse(fight.doTurn());
            
            assertEquals(i, fight.player1Units.size());
            assertEquals(i, fight.player2Units.size());
        }
        
        assertTrue(fight.doTurn());
    }
    
    @Test
    void testFightIsBubbled(){
        Unit unit = new Cat();
        Unit unit2 = new Cat();
        unit.setIsBubbled(true);
        unit2.setIsBubbled(true);
        
        player.addToTable(unit, -1);
        player2.addToTable(unit2, -1);
        
        Fight fight = new Fight(game, player, player2);
        
        assertFalse(fight.doTurn());
        
        assertEquals(1, fight.player1Units.size());
        assertEquals(1, fight.player2Units.size());
    }
    
    @Test
    void testFightOneIsBubbled(){
        Unit unit = new Cat();
        Unit unit2 = new Cat();
        
        unit.setIsBubbled(true);
        
        player.addToTable(unit, -1);
        player2.addToTable(unit2, -1);
        
        Fight fight = new Fight(game, player, player2);
        
        
        assertEquals(1, fight.player1Units.size());
        assertEquals(0, fight.player2Units.size());
        
        assertTrue(fight.doTurn());
        
        assertEquals(player2.getMaxHealth() - (unit.getLevel() + player.getLevel()), player2.getHealth());
    }
}
