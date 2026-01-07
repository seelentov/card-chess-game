package ru.vladislavkomkov.model.entity.spell.impl.third;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.SpellTestCase;

public class ArmorStashTest extends SpellTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new ArmorStash());
  }
  
  @Test
  void testOnPlayed()
  {
    player.setArmor(0);

    player.addToHand(Card.of(new ArmorStash(player)));
    player.playCard(0);

    assertEquals(ArmorStash.ARMOR, player.getArmor());
  }
}
