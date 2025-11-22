package ru.vladislavkomkov.models.entity.unit.impl.naga;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.vladislavkomkov.models.entity.unit.UnitTestCase;

public class SpellCrafterTestCase extends UnitTestCase
{
  protected void testDefault(SpellCrafter spellCrafter)
  {
    onPlayed(spellCrafter);
    onStartTurn(spellCrafter);
    super.testDefault(spellCrafter);
  }
  
  void onStartTurn(SpellCrafter spellCrafter)
  {
    setUp();
    
    player.addToTable(spellCrafter, 0);
    game.processStartTurn(player);
    assertEquals(player.cloneHand().get(0).get().getName(), spellCrafter.getSpellcraft().getName());
    
    tearDown();
  }
  
  void onPlayed(SpellCrafter spellCrafter)
  {
    setUp();
    
    spellCrafter.onPlayed(game, player, 0);
    assertEquals(player.cloneHand().get(0).get().getName(), spellCrafter.getSpellcraft().getName());
    
    tearDown();
  }
}
