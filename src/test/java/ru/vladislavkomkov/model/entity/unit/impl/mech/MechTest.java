package ru.vladislavkomkov.model.entity.unit.impl.mech;

import org.junit.jupiter.api.Test;
import ru.vladislavkomkov.GamePlayerTestCase;
import ru.vladislavkomkov.consts.Listeners;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.mech.fourth.AccordoTron;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MechTest extends GamePlayerTestCase
{
  @Test
  void testMagnetStats()
  {
    Unit unit = new AccordoTron();
    unit.magnetize(new AccordoTron());
    
    assertEquals(unit.newThis().getAttack() * 2, unit.getAttack());
  }

  @Test
  void testMagnetBuffGold()
  {
    testMagnetBuff(new AccordoTron(), new AccordoTron().buildGold());
  }

  @Test
  void testMagnetBuffDef()
  {
    testMagnetBuff(new AccordoTron(), new AccordoTron());
  }
  
  void testMagnetBuff(Unit unit, Unit unit2)
  {
    String testBuffName = "Test buff";
    
    for (int i = 0; i < 5; i++)
    {
      unit.addBuff(new Buff(
          unit1 -> {
          },
          unit1 -> {
          },
          testBuffName + i));
    }
    
    for (int i = 5; i < 10; i++)
    {
      unit2.addBuff(new Buff(
          unit1 -> {
          },
          unit1 -> {
          },
          testBuffName + i));
    }
    
    unit.magnetize(unit2);
    
    assertEquals(11, unit.getBuffs().size());
    
    for (int i = 0; i < unit.getBuffs().size() - 1; i++)
    {
      assertEquals(testBuffName + i, unit.getBuffs().get(i).getDescription());
    }
    
    assertEquals(unit2.getDescription(), unit.getBuffs().get(10).getDescription());
  }
  
  @Test
  void testMagnetOnAttack()
  {
    int moneyStep = 10;
    int initMoney = player.getMoney();

    Unit unit = new AccordoTron();
    unit.getListener().onAttackListeners.put(
            Listeners.KEY_CORE,
            (game1, player1, player3, unit1, attacked) -> player1.addMoney(moneyStep)
    );

    Unit unit2 = new AccordoTron();
    unit2.getListener().onAttackListeners.put(
            Listeners.KEY_CORE,
            (game1, player1, player3, unit1, attacked) -> player.addMoney(moneyStep)
    );

    unit.magnetize(unit2);

    unit.onAttack(game,player,player2,new Cat());
    assertEquals(moneyStep * 2 + initMoney, player.getMoney());
  }

  @Test
  void testMagnetOnStartTurn()
  {
    int moneyStep = 10;
    int initMoney = player.getMoney();

    Unit unit = new AccordoTron();
    unit.getListener().onStartTurnListeners.put(
            Listeners.KEY_CORE,
            (game1, player1) -> player1.addMoney(moneyStep)
    );

    player.addToTable(unit);

    Unit unit2 = new AccordoTron();
    unit2.getListener().onStartTurnListeners.put(
            Listeners.KEY_CORE,
            (game1, player1) -> player1.addMoney(moneyStep)
    );

    unit.magnetize(unit2);

    game.processStartTurn(player);
    assertEquals(moneyStep * 2 + initMoney, player.getMoney());
  }
}
