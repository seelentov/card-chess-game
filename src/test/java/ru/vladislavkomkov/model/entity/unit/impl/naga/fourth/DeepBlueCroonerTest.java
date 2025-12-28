package ru.vladislavkomkov.model.entity.unit.impl.naga.fourth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.impl.spellcraft.impl.DeepBlues;
import ru.vladislavkomkov.model.entity.unit.impl.naga.SpellCrafterTestCase;

public class DeepBlueCroonerTest extends SpellCrafterTestCase
{
  @Test
  void testDefault()
  {
    testDefault(new DeepBlueCrooner());
  }
  
  @Test
  void testOnStartTurn()
  {
    player.addToTable(new DeepBlueCrooner());
    game.processStartTurn(player);
    
    assertEquals(new DeepBlues().getName(), player.getHand().get(0).getEntity().getName());
  }
  
  @Test
  void testOnPlayed()
  {
    player.addToHand(Card.of(new DeepBlueCrooner()));
    player.playCard(0, 0);
    
    assertEquals(new DeepBlues().getName(), player.getHand().get(0).getEntity().getName());
  }
  
  @Test
  void testIfGold()
  {
    player.addToHand(Card.of(new DeepBlueCrooner().buildGold()));
    player.playCard(0, 0);
    
    assertEquals(new DeepBlues().getName(), player.getHand().get(1).getEntity().getName());
    assertTrue(player.getHand().get(1).getEntity().isGold());
  }
}
