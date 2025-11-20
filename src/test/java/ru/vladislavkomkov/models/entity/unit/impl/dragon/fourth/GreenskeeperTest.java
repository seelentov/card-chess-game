package ru.vladislavkomkov.models.entity.unit.impl.dragon.fourth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.entity.unit.UnitTestCase;
import ru.vladislavkomkov.models.entity.unit.impl.beast.first.Alleycat;
import ru.vladislavkomkov.models.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.models.player.Player;

public class GreenskeeperTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new Alleycat());
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
      player.addToTable(new Unit()
      {
        @Override
        public boolean isAnswerOnPlayed()
        {
          return true;
        }
        
        @Override
        public void onPlayed(Game game, Player player, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2)
        {
          player.addMoney(finalI * moneyStep);
        }
      });
    }
    
    gk.onAttack(game, player, player2, new Cat());
    
    assertEquals((moneyStep * (Player.TABLE_LIMIT - 1)) + moneyInit, player.getMoney());
  }
}
