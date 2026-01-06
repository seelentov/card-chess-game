package ru.vladislavkomkov.model.entity.spell.impl.second;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.vladislavkomkov.model.card.Card.of;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.spell.SpellTestCase;

public class SaloonFinestTest extends SpellTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new SaloonFinest());
  }
  
  @Test
  protected void testOnPlayed()
  {
    player.addToHand(of(new SaloonFinest(player)));
    player.playCard(0);
    
    player.getTavern().getCards().forEach(slot -> {
      assertTrue(slot.getEntity() instanceof Spell);
      assertTrue(((Spell) slot.getEntity()).getLevel() <= player.getLevel());
    });
  }
}