package ru.vladislavkomkov.model.entity.spell.impl.spellcraft;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.SpellTestCase;

public class SpellCraftTestCase extends SpellTestCase
{
  protected void testDefault(SpellCraft spellCraft)
  {
    super.testDefault(spellCraft);
    
    onEndTurn(spellCraft);
  }
  
  void onEndTurn(SpellCraft spellCraft)
  {
    setUp();
    
    player.addToHand(Card.of(spellCraft));
    game.processEndTurn(player);
    
    assertTrue(player.cloneHand().isEmpty());
    
    tearDown();
  }
}