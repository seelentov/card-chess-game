package ru.vladislavkomkov.models.player;

import org.junit.jupiter.api.Test;
import ru.vladislavkomkov.GamePlayerTestCase;
import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.entity.Entity;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.util.ListenerUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListenerTest extends GamePlayerTestCase {
    static final int STEP_MONEY = 10;

    @Test
    void testOnceOnPlayed(){
        testOnPlayed(true);
    }

    @Test
    void testManyOnPlayed(){
        testOnPlayed(false);
    }

    void testOnPlayed(boolean once){
        Unit unit = new Unit(){};

        testListener(
                player.listener.onPlayedListeners,
                ()->unit.onPlayed(game,player,0),
                (Game game, Player player, Entity entity, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2) -> testAction(),
                once
        );
    }

    @Test
    void testOnceOnHandled(){
        testOnHandled(true);
    }

    @Test
    void testManyOnHandled(){
        testOnHandled(false);
    }

    void testOnHandled(boolean once){
        Unit unit = new Unit(){};

        testListener(
                player.listener.onHandledListeners,
                ()->unit.onHandled(game,player),
                (game1, player1, entity) -> testAction(),
                once
        );
    }

    @Test
    void testOnceOnAttack(){
        testOnAttack(true);
    }

    @Test
    void testManyOnAttack(){
        testOnAttack(false);
    }

    void testOnAttack(boolean once){
        Unit unit = new Unit(){};
        Unit unit2 = new Unit(){};

        testListener(
                player.listener.onAttackListeners,
                ()->unit.onAttack(game,player,player2,unit2),
                (game1, player1, player3, unit1, attacked) -> testAction(),
                once
        );
    }

    @Test
    void testOnceOnAttacked(){
        testOnAttacked(true);
    }

    @Test
    void testManyOnAttacked(){
        testOnAttacked(false);
    }

    void testOnAttacked(boolean once){
        Unit unit = new Unit(){};
        Unit unit2 = new Unit(){};

        testListener(
                player.listener.onAttackedListeners,
                ()->unit.onAttacked(game,player,player2,unit2),
                (game1, player1, player3, unit1, attacked) -> testAction(),
                once
        );
    }

    @Test
    void testOnceOnDead(){
        testOnDead(true);
    }

    @Test
    void testManyOnDead(){
        testOnDead(false);
    }

    void testOnDead(boolean once){
        Unit unit = new Unit(){};
        Unit unit2 = new Unit(){};

        testListener(
                player.listener.onDeadListeners,
                ()->unit.onDead(game,player,player2,unit2),
                (game1, player1, player3, unit1, attacked) -> testAction(),
                once
        );
    }

    @Test
    void testOnceOnSell(){
        testOnSell(true);
    }

    @Test
    void testManyOnSell(){
        testOnSell(false);
    }

    void testOnSell(boolean once){
        Unit unit = new Unit(){};

        testListener(
                player.listener.onSellListeners,
                ()->{
                    unit.onSell(game,player);
                    player.decMoney(1);
                },
                (game1, player1, entity) -> testAction(),
                once
        );
    }

    @Test
    void testOnceOnStartTurn(){
        testOnStartTurn(true);
    }

    @Test
    void testManyOnStartTurn(){
        testOnStartTurn(false);
    }

    void testOnStartTurn(boolean once){
        testListener(
                player.listener.onStartTurnListeners,
                ()->game.processStartTurn(player),
                (game1, player1) -> testAction(),
                once
        );
    }

    @Test
    void testOnceOnEndTurn(){
        testOnEndTurn(true);
    }

    @Test
    void testManyOnEndTurn(){
        testOnEndTurn(false);
    }

    void testOnEndTurn(boolean once){
        testListener(
                player.listener.onEndTurnListeners,
                ()->game.processEndTurn(player),
                (game1, player1) -> testAction(),
                once
        );
    }

    @Test
    void testOnceOnStartFight(){
        testOnStartFight(true);
    }

    @Test
    void testManyOnStartFight(){
        testOnStartFight(false);
    }

    void testOnStartFight(boolean once){
        testListener(
                player.listener.onStartFightListeners,
                ()->game.processStartFight(player, player2),
                (game1, player1) -> testAction(),
                once
        );
    }
    
    @Test
    void testOnceOnEndFight(){
        testOnEndFight(true);
    }
    
    @Test
    void testManyOnEndFight(){
        testOnEndFight(false);
    }
    
    void testOnEndFight(boolean once){
        testListener(
                player.listener.onEndFightListeners,
                ()->game.processEndFight(player, player2),
                (game1, player1) -> testAction(),
                once
        );
    }
    
    @Test
    void testOnceOnResetTavern(){
        testOnResetTavern(true);
    }
    
    @Test
    void testManyOnResetTavern(){
        testOnResetTavern(false);
    }
    
    void testOnResetTavern(boolean once){
        testListener(
                player.listener.onResetTavernListeners,
                ()->player.resetTavern(game),
                (game1, player1) -> testAction(),
                once
        );
    }
    
    @Test
    void testOnceOnIncLevel(){
        testOnIncLevel(true);
    }
    
    @Test
    void testManyOnIncLevel(){
        testOnIncLevel(false);
    }
    
    void testOnIncLevel(boolean once){
        testListener(
                player.listener.onIncLevelListeners,
                ()->player.incLevel(game),
                (game1, player1) -> testAction(),
                once
        );
    }

    void testAction(){
        player.addMoney(STEP_MONEY);
    }

    <T> void testListener(Map<String, T> listeners, Runnable processListener, T action, boolean once){
        int initMoney = player.getMoney();

        listeners.put(once ? ListenerUtils.generateKeyOnce() : ListenerUtils.generateKey(), action);

        processListener.run();

        assertEquals(initMoney + STEP_MONEY, player.getMoney());

        processListener.run();

        assertEquals(initMoney + (STEP_MONEY * (once ? 1 : 2)), player.getMoney());
    }
}
