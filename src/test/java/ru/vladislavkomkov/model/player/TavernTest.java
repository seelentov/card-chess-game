package ru.vladislavkomkov.model.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;

public class TavernTest
{
  @Test
  void testAllLevels()
  {
    Tavern tavern = new Tavern();
    for (int i = 1; i < 7; i++)
    {
      tavern.reset(i);
      assertEquals(Tavern.getCountByLevel(i), tavern.cards.size() - 1);
      for (Card card : tavern.cards)
      {
        assertTrue(card.getEntity().getLevel() <= i);
      }
    }
  }
}
