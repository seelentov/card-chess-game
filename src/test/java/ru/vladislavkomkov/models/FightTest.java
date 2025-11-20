package ru.vladislavkomkov.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.GamePlayerTestCase;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.entity.unit.impl.demon.first.IckyImp;
import ru.vladislavkomkov.models.entity.unit.impl.demon.first.IckyImpTest;
import ru.vladislavkomkov.models.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.models.entity.unit.impl.trash.demon.first.Imp;
import ru.vladislavkomkov.models.entity.unit.impl.undead.first.RisenRider;
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

        assertFalse(fight.doTurn());

        assertEquals(1, fight.player1Units.size());
        assertEquals(0, fight.player2Units.size());

        assertTrue(fight.doTurn());

        assertEquals(player2.getMaxHealth() - (unit.getLevel() + player.getLevel()), player2.getHealth());
    }

    @Test
    void testFightIsRebirth(){
        Unit unit = new RisenRider();
        Unit unit2 = new Cat();
        Unit unit3 = new Cat();

        player.addToTable(unit, -1);
        player2.addToTable(unit2, -1);
        player2.addToTable(unit3, -1);

        Fight fight = new Fight(game, player, player2);

        assertTrue(fight.player1Units.get(0).getIsRebirth());

        assertEquals(1, fight.player1Units.size());
        assertEquals(2, fight.player2Units.size());

        assertFalse(fight.doTurn());

        assertEquals(1, fight.player1Units.size());
        assertEquals(1, fight.player2Units.size());

        assertFalse(fight.player1Units.get(0).getIsRebirth());

        assertFalse(fight.doTurn());

        assertEquals(0, fight.player1Units.size());
        assertEquals(0, fight.player2Units.size());

        assertTrue(fight.doTurn());
    }

    @Test
    void testFightOnDead(){
        Unit unit = new IckyImp();
        Unit unit2 = new Cat();

        player.addToTable(unit, -1);
        player2.addToTable(unit2, -1);

        Fight fight = new Fight(game, player, player2);

        assertEquals(1, fight.player1Units.size());
        assertEquals(1, fight.player2Units.size());

        assertFalse(fight.doTurn());

        assertEquals(2, fight.player1Units.size());
        assertEquals(0, fight.player2Units.size());

        assertEquals(new Imp().getName(), fight.player1Units.get(0).getName());
        assertEquals(new Imp().getName(), fight.player1Units.get(1).getName());

        assertEquals(1, player.getUnitsCount());

        assertEquals(new IckyImp().getName(), player.cloneTable().get(0).getName());

        assertTrue(fight.doTurn());
    }

    @Test
    void testFightOnDeadSummonInPlace(){
        Unit unit = new Cat();
        unit.setIsDisguise(true);
        player2.addToTable(unit, -1);

        player2.addToTable(new IckyImp(), -1);

        for (int i = 0; i < 4; i++) {
            Unit u = new Cat();
            u.setIsDisguise(true);
            player2.addToTable(u, -1);
        }


        Unit enUnit = new Cat();
        enUnit.incAttack(99);
        player.addToTable(enUnit, -1);

        Fight fight = new Fight(game, player, player2);

        assertFalse(fight.doTurn());

        assertEquals(0, fight.player1Units.size());

        assertEquals(7, fight.player2Units.size());
        assertEquals(new Cat().getName(), fight.player2Units.get(0).getName());

        for (int i = 1; i < 3; i++) {
            assertEquals(new Imp().getName(), fight.player2Units.get(i).getName());
        }

        for (int i = 3; i < 7; i++) {
            assertEquals(new Cat().getName(), fight.player2Units.get(i).getName());
        }
    }

    @Test
    void testFightOnDeadSummonInPlaceIfOverflow(){
        Unit unit = new Cat();
        unit.setIsDisguise(true);
        player2.addToTable(unit, -1);

        player2.addToTable(new IckyImp(), -1);

        for (int i = 0; i < 6; i++) {
            Unit u = new Cat();
            u.setIsDisguise(true);
            player2.addToTable(u, -1);
        }


        Unit enUnit = new Cat();
        enUnit.incAttack(99);
        player.addToTable(enUnit, -1);

        Fight fight = new Fight(game, player, player2);

        assertFalse(fight.doTurn());

        assertEquals(0, fight.player1Units.size());

        assertEquals(7, fight.player2Units.size());
        assertEquals(new Cat().getName(), fight.player2Units.get(0).getName());

        assertEquals(new Imp().getName(), fight.player2Units.get(1).getName());

        for (int i = 2; i < 7; i++) {
            assertEquals(new Cat().getName(), fight.player2Units.get(i).getName());
        }
    }

    @Test
    void testFightOnDeadSummonInPlaceAndSummonedAttacker(){
    }

    @Test
    void testAttackOnlyTaunt(){
        int tauntHealth = 1000;

        for (int i = 0; i < 6; i++) {
            Unit u = new Cat();
            u.setAttack(0);
            player.addToTable(u, -1);
        }
        Unit unit = new Cat();
        unit.setAttack(0);
        unit.setHealth(tauntHealth);
        unit.setIsTaunt(true);
        player.addToTable(unit, -1);

        player2.addToTable(new Cat(), -1);

        Fight fight = new Fight(game, player, player2);

        for (int i = 0; i < tauntHealth; i++) {
            assertFalse(fight.doTurn());
        }

        assertEquals(7, player.inFightTable.size());

        for (int i = 0; i < 6; i++) {
            assertEquals(new Cat().getName(),player.inFightTable.get(i).getName());
            assertEquals(new Cat().getHealth(),player.inFightTable.get(i).getHealth());
        }

        assertEquals(tauntHealth / 2, player.inFightTable.get(6).getHealth());
    }
}
