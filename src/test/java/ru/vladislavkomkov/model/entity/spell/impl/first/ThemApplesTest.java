package ru.vladislavkomkov.model.entity.spell.impl.first;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.SpellTestCase;

public class ThemApplesTest extends SpellTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new ThemApples());
  }
  
  @Test
  protected void testOnPlayed()
  {
    player.addToHand(Card.of(new ThemApples(player)));
    player.playCard(0);
    
    player.getTavern().getUnits().forEach(unit -> {
      assertEquals(unit.newThis().getAttack() + ThemApples.ATTACK_BOOST, unit.getAttack());
      assertEquals(unit.newThis().getHealth() + ThemApples.HEALTH_BOOST, unit.getHealth());
    });
  }
}
