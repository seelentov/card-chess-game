package ru.vladislavkomkov.model.entity.spell.impl.first;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.SpellTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

public class FortifyTest extends SpellTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new Fortify());
  }
  
  @Test
  protected void testOnPlayed()
  {
    player.addToTable(new Cat(player));
    player.addToHand(Card.of(new Fortify(player)));
    player.playCard(0, 0);
    
    assertEquals(new Cat().getHealth() + Fortify.HEALTH_BOOST, player.getTable().get(0).getHealth());
    assertTrue(player.getTable().get(0).isTaunt());
  }
}
