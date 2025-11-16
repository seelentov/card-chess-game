package ru.vladislavkomkov.models.unit.impl.mech;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.GamePlayerTestCase;
import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.models.unit.impl.mech.fourth.AccordoTron;

public class MechTest extends GamePlayerTestCase {
    @Test
    void testMagnetize(){
        Mech mech = new AccordoTron();
        Mech mech2 = new AccordoTron();
        Mech mech3 = new AccordoTron();
        mech.magnetize(mech2);
        mech.magnetize(mech3);
        assertEquals(new AccordoTron().getName(),mech.cloneMagnetized().get(0).getName());
        assertEquals(new AccordoTron().getName(),mech.cloneMagnetized().get(1).getName());
    }
    
    @Test
    void testMagnetizedOnStartTurn(){
        int moneyStart = player.getMoney();
        int moneyStep = 10;
        
        Mech mech = new Mech(){
            public void onStartTurn(Game game, Player player){
                super.onStartTurn(game,player);
                player.addMoney(moneyStep);
            }
        };
        
        Mech mech2 = new Mech(){
            public void onStartTurn(Game game, Player player){
                super.onStartTurn(game,player);
                player.addMoney(moneyStep);
            }
        };
        
        player.addToTable(mech, 0);
        mech.magnetize(mech2);
        
        game.processStartTurn(player);
        
        assertEquals(moneyStart + (moneyStep * 2), player.getMoney());
    }
    
    @Test
    void testMagnetizedOnEndTurn(){
        int moneyStart = player.getMoney();
        int moneyStep = 10;
        
        Mech mech = new Mech(){
            public void onEndTurn(Game game, Player player){
                super.onEndTurn(game,player);
                player.addMoney(moneyStep);
            }
        };
        
        Mech mech2 = new Mech(){
            public void onEndTurn(Game game, Player player){
                super.onEndTurn(game,player);
                player.addMoney(moneyStep);
            }
        };
        
        player.addToTable(mech, 0);
        mech.magnetize(mech2);
        
        game.processEndTurn(player);
        
        assertEquals(moneyStart + (moneyStep * 2), player.getMoney());
    }
    
    @Test
    void testMagnetizedOnStartFight(){
    
    }
    
    @Test
    void testMagnetizedOnEndFight(){
    
    }
    
    @Test
    void testMagnetizedOnAttacked(){
    
    }
    
    @Test
    void testMagnetizedOnAttack(){
    
    }
    
    @Test
    void testMagnetizedOnDead(){
    
    }
}
