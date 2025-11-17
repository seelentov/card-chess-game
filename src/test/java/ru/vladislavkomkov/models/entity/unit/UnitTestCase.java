package ru.vladislavkomkov.models.entity.unit;

import static org.junit.jupiter.api.Assertions.*;

import ru.vladislavkomkov.GamePlayerTestCase;

public abstract class UnitTestCase extends GamePlayerTestCase {
    protected void testDefault(Unit unit){
        onHanded(unit);
        onPlayed(unit);
        onSell(unit);
        onDead(unit);
        onAttackAttacked(unit);
    };

    void onPlayed(Unit unit){
        setUp();
        
        assertNull(player.cloneTable()[0]);
        unit.onPlayed(game, player, 0);
        assertEquals(unit.getName(), player.cloneTable()[0].getName());
        
        tearDown();
    }
    
    void onHanded(Unit unit){
        setUp();
        
        unit.onHandled(game, player);
        assertEquals(unit.getName(), player.cloneHand().get(0).get().getName());
        
        tearDown();
    }
    
    void onSell(Unit unit){
        setUp();
        
        int money = player.getMoney();
        player.addToTable(unit, 0);
        assertNotNull(player.cloneTable()[0]);
        unit.onSell(game, player);
        assertEquals(money + 1, player.getMoney());
        assertNull(player.cloneTable()[0]);
        
        tearDown();
    }

    void onDead(Unit unit){
        setUp();
        
        player.addToTable(unit, 0);
        Unit unit2 = (Unit) unit.clone();
        player2.addToTable(unit2, 0);

        if(unit.isRebirth){
            unit.onDead(game,player,player2,unit2);
            assertFalse(unit.isRebirth);
        }
        
        tearDown();
    }

    void onAttackAttacked(Unit unit){
        setUp();
        
        player.addToTable(unit, 0);
        Unit unit2 = (Unit) unit.clone();
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
        
        tearDown();
    }
}
