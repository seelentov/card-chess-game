package ru.vladislavkomkov.models.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.GamePlayerTestCase;
import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.models.unit.impl.mech.Mech;

public class UnitTest extends GamePlayerTestCase {
    @Test
    void testOnStartTurn(){
        int moneyStart = player.getMoney();
        int moneyStep = 10;
        
        Unit mech = new Unit(){
            public void onStartTurn(Game game, Player player){
                super.onStartTurn(game,player);
                player.addMoney(moneyStep);
            }
        };

        player.addToTable(mech, 0);
        game.processStartTurn(player);
        
        assertEquals(moneyStart + (moneyStep), player.getMoney());
    }
    
    @Test
    void testOnEndTurn(){
        int moneyStart = player.getMoney();
        int moneyStep = 10;
        
        Unit mech = new Unit(){
            public void onEndTurn(Game game, Player player){
                super.onEndTurn(game,player);
                player.addMoney(moneyStep);
            }
        };
        
        player.addToTable(mech, 0);
        game.processEndTurn(player);
        
        assertEquals(moneyStart + (moneyStep), player.getMoney());
        
    }
    
    @Test
    void testOnStartFight(){
        
    }
    
    @Test
    void testOnEndFight(){
        
    }
    
    @Test
    void testOnAttacked(){
        
    }
    
    @Test
    void testOnAttack(){
        
    }
    
    @Test
    void testOnDead(){
        
    }
}
