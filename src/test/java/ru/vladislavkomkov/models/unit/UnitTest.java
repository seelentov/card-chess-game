package ru.vladislavkomkov.models.unit;

import static org.junit.jupiter.api.Assertions.*;

import ru.vladislavkomkov.GamePlayerTestCase;

public abstract class UnitTest extends GamePlayerTestCase {
    protected void testDefault(Unit unit){
        onHanded(unit);
        onPlayed(unit);
        onSell(unit);
        onDead(unit);
        onAttackAttacked(unit);
    };

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

    protected void onDead(Unit unit){
        player.addToTable(unit, 0);
        Unit unit2 = unit.clone();
        player2.addToTable(unit2, 0);

        if(unit.isRebirth){
            unit.onDead(game,player,player2,unit2);
            assertFalse(unit.isRebirth);
        }
    }

    protected void onAttackAttacked(Unit unit){
        player.addToTable(unit, 0);
        Unit unit2 = unit.clone();
        player2.addToTable(unit2, 0);

        int unitBeginHP = unit.getHealth();
        int unit2BeginHP = unit2.getHealth();

        boolean unitBeginBubbled = unit.isBubbled;
        boolean unit2BeginBubbled = unit2.isBubbled;

        unit.onAttack(game,player,player2,unit2);

        if(unitBeginBubbled){
            assertFalse(unit.isBubbled);
            unit.onAttack(game,player,player2,unit2);
        }

        unit2.onAttacked(game,player,player2,unit);

        if(unit2BeginBubbled){
            assertFalse(unit2.isBubbled);
            unit2.onAttacked(game,player,player2,unit);
        }

        assertEquals(unitBeginHP - unit2.getAttack(), unit.getHealth());
        assertEquals(unit2BeginHP - unit.getAttack(), unit2.getHealth());
    }
}
