package ru.vladislavkomkov.model.entity.unit.impl.beast.third;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.beast.second.SewerRat;
import ru.vladislavkomkov.model.entity.unit.impl.dragon.fourth.Greenskeeper;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.HalfShell;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

import java.util.List;

public class MonstrousMacawTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    testDefault(new MonstrousMacaw());
  }
  
  @Test
  void testOnAttack()
  {
    int moneyStep = 10;
    int moneyInit = player.getMoney();
    
    Unit gk = new MonstrousMacaw();
    player.addToTable(gk);
    
    for (int i = 1; i < Player.TABLE_LIMIT; i++)
    {
      int finalI = i;
      Unit unit = new Unit(player)
      {
      };
      unit.getListener().onDeadListeners.put(
          UUIDUtils.generateKey(),
          ((game1, fight, player1, entity, input, auto) -> player1.addMoney(finalI * moneyStep)));
      player.addToTable(unit);
    }
    
    gk.onAttack(game, null, player, player2, new Cat());
    
    assertEquals((moneyStep) + moneyInit, player.getMoney());
  }
  
  @Test
  void testOnAttackGold()
  {
    int moneyStep = 10;
    int moneyInit = player.getMoney();
    
    Unit gk = new MonstrousMacaw(player).buildGold();
    player.addToTable(gk);
    
    for (int i = 1; i < Player.TABLE_LIMIT; i++)
    {
      int finalI = i;
      Unit unit = new Unit(player)
      {
      };
      unit.getListener().onDeadListeners.put(
          UUIDUtils.generateKey(),
          ((game1, fight, player1, entity, input, auto) -> player1.addMoney(finalI * moneyStep)));
      player.addToTable(unit);
    }
    
    gk.onAttack(game, null, player, player2, new Cat());
    
    assertEquals((moneyStep * 2) + moneyInit, player.getMoney());
  }
  
  @Test
  void testOnAttackOnDeadSpawn()
  {
    player.addToTable(new MonstrousMacaw(player));
    player.addToTable(new SewerRat(player));

    player2.addToTable(new Cat(player2));

    Fight fight = new Fight(game, player, player2);

    fight.doTurn();

    List<Unit> units = fight.getFightTable(player);
    assertEquals(new HalfShell().getName(), units.get(2).getName());
  }
}
