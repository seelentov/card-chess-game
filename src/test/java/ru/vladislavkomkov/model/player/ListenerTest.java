package ru.vladislavkomkov.model.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.GamePlayerTestCase;
import ru.vladislavkomkov.consts.Listeners;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.Listener;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.util.UUIDUtils;

public class ListenerTest extends GamePlayerTestCase
{
  @Test
  void testPush()
  {
    Listener listener = new Listener();
    listener.onAttackedListeners.put(Listeners.KEY_CORE, (game1, fight, player1, player3, unit, attacker) -> {
    });
    listener.onAttackedListeners.put(UUIDUtils.generateKey(), (game1, fight, player1, player3, unit, attacker) -> {
    });
    listener.onAttackListeners.put(Listeners.KEY_CORE, (game1, fight, player1, player3, unit, attacker) -> {
    });
    listener.onAttackListeners.put(UUIDUtils.generateKey(), (game1, fight, player1, player3, unit, attacker) -> {
    });
    listener.onAttackListeners.put(UUIDUtils.generateKey(), (game1, fight, player1, player3, unit, attacker) -> {
    });
    
    assertEquals(2, listener.onAttackedListeners.size());
    assertEquals(3, listener.onAttackListeners.size());
    
    Listener listener2 = new Listener();
    
    listener.onAttackedListeners.put(Listeners.KEY_CORE, (game1, fight, player1, player3, unit, attacker) -> {
    });
    listener.onAttackedListeners.put(UUIDUtils.generateKey(), (game1, fight, player1, player3, unit, attacker) -> {
    });
    listener.onAttackListeners.put(Listeners.KEY_CORE, (game1, fight, player1, player3, unit, attacker) -> {
    });
    listener.onAttackListeners.put(UUIDUtils.generateKey(), (game1, fight, player1, player3, unit, attacker) -> {
    });
    listener.onAttackListeners.put(UUIDUtils.generateKey(), (game1, fight, player1, player3, unit, attacker) -> {
    });
    
    listener2.push(listener);
    
    assertEquals(3, listener.onAttackedListeners.size());
    assertEquals(5, listener.onAttackListeners.size());
  }
  
  @Test
  void testPushDuplicates()
  {
    Listener listener = new Listener();
    
    listener.onAttackedListeners.put(Listeners.KEY_CORE, (game1, fight, player1, player3, unit, attacker) -> {
    });
    listener.onAttackListeners.put(Listeners.KEY_CORE, (game1, fight, player1, player3, unit, attacker) -> {
    });
    
    assertEquals(1, listener.onAttackedListeners.size());
    assertEquals(1, listener.onAttackListeners.size());
    
    Listener listener2 = listener.newCoreListener(false);
    
    listener.push(listener2);
    
    assertEquals(2, listener.onAttackedListeners.size());
    assertEquals(2, listener.onAttackListeners.size());
  }
  
  @Test
  void testGetCore()
  {
    Listener listener = new Listener();
    listener.onAttackedListeners.put(Listeners.KEY_CORE, (game1, fight, player1, player3, unit, attacker) -> {
    });
    listener.onAttackedListeners.put(UUIDUtils.generateKey(), (game1, fight, player1, player3, unit, attacker) -> {
    });
    listener.onAttackListeners.put(Listeners.KEY_CORE, (game1, fight, player1, player3, unit, attacker) -> {
    });
    listener.onAttackListeners.put(UUIDUtils.generateKey(), (game1, fight, player1, player3, unit, attacker) -> {
    });
    listener.onAttackListeners.put(UUIDUtils.generateKey(), (game1, fight, player1, player3, unit, attacker) -> {
    });
    
    assertEquals(2, listener.onAttackedListeners.size());
    assertEquals(3, listener.onAttackListeners.size());
    
    Listener coreListener = listener.newCoreListener();
    
    assertEquals(1, coreListener.onAttackedListeners.size());
    assertEquals(1, coreListener.onAttackListeners.size());
    
    assertTrue(coreListener.onAttackedListeners.containsKey(Listeners.KEY_CORE));
    assertTrue(coreListener.onAttackListeners.containsKey(Listeners.KEY_CORE));
  }
  
  @Test
  void testOnceOnPlayed()
  {
    testOnPlayed(true);
  }
  
  @Test
  void testManyOnPlayed()
  {
    testOnPlayed(false);
  }
  
  void testOnPlayed(boolean once)
  {
    Unit unit = new Unit()
    {
    };
    
    testListener(
        player.listener.onPlayedListeners,
        () -> unit.onPlayed(game, null, player, 0),
        (Game game, Fight fight, Player player, Entity entity, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2, boolean auto) -> testAction(),
        once);
  }
  
  @Test
  void testOnceOnHandled()
  {
    testOnHandled(true);
  }
  
  @Test
  void testManyOnHandled()
  {
    testOnHandled(false);
  }
  
  void testOnHandled(boolean once)
  {
    Unit unit = new Unit()
    {
    };
    
    testListener(
        player.listener.onHandledListeners,
        () -> unit.onHandled(game, null, player),
        (game1, fight, player1, entity) -> testAction(),
        once);
  }
  
  @Test
  void testOnceOnAttack()
  {
    testOnAttack(true);
  }
  
  @Test
  void testManyOnAttack()
  {
    testOnAttack(false);
  }
  
  void testOnAttack(boolean once)
  {
    Unit unit = new Unit()
    {
    };
    Unit unit2 = new Unit()
    {
    };
    
    testListener(
        player.listener.onAttackListeners,
        () -> unit.onAttack(game, null, player, player2, unit2),
        (game1, fight, player1, player3, unit1, attacked) -> testAction(),
        once);
  }
  
  @Test
  void testOnceOnAttacked()
  {
    testOnAttacked(true);
  }
  
  @Test
  void testManyOnAttacked()
  {
    testOnAttacked(false);
  }
  
  void testOnAttacked(boolean once)
  {
    Unit unit = new Unit()
    {
    };
    Unit unit2 = new Unit()
    {
    };
    
    testListener(
        player.listener.onAttackedListeners,
        () -> unit.onAttacked(game, null, player, player2, unit2),
        (game1, fight, player1, player3, unit1, attacked) -> testAction(),
        once);
  }
  
  @Test
  void testOnceOnDead()
  {
    testOnDead(true);
  }
  
  @Test
  void testManyOnDead()
  {
    testOnDead(false);
  }
  
  void testOnDead(boolean once)
  {
    Unit unit = new Unit()
    {
    };
    Unit unit2 = new Unit()
    {
    };
    
    testListener(
        player.listener.onDeadListeners,
        () -> unit.onDead(game, null, player, player2, unit2),
        (game1, fight, player1, player3, unit1, attacked) -> testAction(),
        once);
  }
  
  @Test
  void testOnceOnSell()
  {
    testOnSell(true);
  }
  
  @Test
  void testManyOnSell()
  {
    testOnSell(false);
  }
  
  void testOnSell(boolean once)
  {
    Unit unit = new Unit()
    {
    };
    
    testListener(
        player.listener.onSellListeners,
        () -> {
          unit.onSell(game, null, player);
          player.decMoney(1);
        },
        (game1, fight, player1, entity) -> testAction(),
        once);
  }
  
  @Test
  void testOnceOnStartTurn()
  {
    testOnStartTurn(true);
  }
  
  @Test
  void testManyOnStartTurn()
  {
    testOnStartTurn(false);
  }
  
  void testOnStartTurn(boolean once)
  {
    testListener(
        player.listener.onStartTurnListeners,
        () -> game.processStartTurn(player),
        (game1, fight, player1) -> testAction(),
        once);
  }
  
  @Test
  void testOnceOnEndTurn()
  {
    testOnEndTurn(true);
  }
  
  @Test
  void testManyOnEndTurn()
  {
    testOnEndTurn(false);
  }
  
  void testOnEndTurn(boolean once)
  {
    testListener(
        player.listener.onEndTurnListeners,
        () -> game.processEndTurn(player),
        (game1, fight, player1) -> testAction(),
        once);
  }
  
  @Test
  void testOnceOnStartFight()
  {
    testOnStartFight(true);
  }
  
  @Test
  void testManyOnStartFight()
  {
    testOnStartFight(false);
  }
  
  void testOnStartFight(boolean once)
  {
    testListener(
        player.listener.onStartFightListeners,
        () -> game.processStartFight(null, player, player2),
        (game1, fight, player1, player2) -> testAction(),
        once);
  }
  
  @Test
  void testOnceOnEndFight()
  {
    testOnEndFight(true);
  }
  
  @Test
  void testManyOnEndFight()
  {
    testOnEndFight(false);
  }
  
  void testOnEndFight(boolean once)
  {
    testListener(
        player.listener.onEndFightListeners,
        () -> game.processEndFight(null, player, player2),
        (game1, fight, player1, player2) -> testAction(),
        once);
  }
  
  @Test
  void testOnceOnResetTavern()
  {
    testOnResetTavern(true);
  }
  
  @Test
  void testManyOnResetTavern()
  {
    testOnResetTavern(false);
  }
  
  void testOnResetTavern(boolean once)
  {
    testListener(
        player.listener.onResetTavernListeners,
        () -> player.resetTavern(),
        (game1, fight, player1) -> testAction(),
        once);
  }
  
  @Test
  void testOnceOnIncLevel()
  {
    testOnIncLevel(true);
  }
  
  @Test
  void testManyOnIncLevel()
  {
    testOnIncLevel(false);
  }
  
  void testOnIncLevel(boolean once)
  {
    testListener(
        player.listener.onIncLevelListeners,
        () -> player.incLevel(true),
        (game1, fight, player1) -> testAction(),
        once);
  }
  
  void testAction()
  {
    player.addMoney(STEP_MONEY);
  }
}
