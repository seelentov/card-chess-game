package ru.vladislavkomkov.model.entity.spell.impl.first;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.spell.SpellTestCase;

public class TavernCoinTest extends SpellTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new TavernCoin());
  }
  
  @Test
  protected void testOnPlayed()
  {
    int money = player.getMoney();
    new TavernCoin().onPlayed(game, null, player, 0);
    assertEquals(money + 1, player.getMoney());
  }
}
