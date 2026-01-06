package ru.vladislavkomkov.model.entity.unit.impl.dragon.fourth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Alleycat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

public class GreenskeeperTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new Greenskeeper());
  }
  
  @Test
  protected void testOnPlayed()
  {
    int moneyStep = 10;
    int moneyInit = player.getMoney();
    
    Unit gk = new Greenskeeper();
    player.addToTable(gk);
    
    for (int i = 1; i < Player.TABLE_LIMIT; i++)
    {
      int finalI = i;
      Unit unit = new Unit(player)
      {
      };
      unit.getListener().onPlayedListeners.put(
          UUIDUtils.generateKey(),
          ((game1, fight, player1, entity, input, auto) -> player1.addMoney(finalI * moneyStep)));
      player.addToTable(unit);
    }
    
    gk.onAttack(game, null, player, player2, new Cat());
    
    assertEquals((moneyStep * (Player.TABLE_LIMIT - 1)) + moneyInit, player.getMoney());
  }
  
  @Test
  protected void testOnPlayedCat()
  {
    Unit gk = new Greenskeeper();
    
    player.addToTable(gk);
    player.addToTable(new Alleycat());
    player.addToTable(new Cat());
    player.addToTable(new Cat());
    player.addToTable(new Alleycat());
    
    gk.onAttack(game, null, player, player2, new Cat());
    
    assertEquals(new Greenskeeper().getName(), player.getTable().get(0).getName());
    assertEquals(new Alleycat().getName(), player.getTable().get(1).getName());
    assertEquals(new Cat().getName(), player.getTable().get(2).getName());
    assertEquals(new Cat().getName(), player.getTable().get(3).getName());
    assertEquals(new Alleycat().getName(), player.getTable().get(4).getName());
    assertEquals(new Cat().getName(), player.getTable().get(5).getName());
  }
}
