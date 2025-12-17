package ru.vladislavkomkov.model.entity.spell.impl.second;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.spell.SpellTestCase;
import ru.vladislavkomkov.model.entity.spell.impl.first.TavernCoin;
import ru.vladislavkomkov.model.player.Player;

public class StrikeOilTest extends SpellTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new StrikeOil());
  }
  
  @Test
  protected void testOnPlayed()
  {
    int money = player.getMaxMoney();
    new StrikeOil().onPlayed(game, player, 0);
    assertEquals(money + 1, player.getMaxMoney());
  }
  
  @Test
  protected void testOnPlayedIfMax()
  {
    player.setMaxMoney(Player.MAX_MONEY);
    new StrikeOil().onPlayed(game, player, 0);
    assertEquals(Player.MAX_MONEY + 1, player.getMaxMoney());
  }
}
