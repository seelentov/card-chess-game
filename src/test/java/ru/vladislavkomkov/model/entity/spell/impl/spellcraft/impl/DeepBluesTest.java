package ru.vladislavkomkov.model.entity.spell.impl.spellcraft.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.spell.impl.spellcraft.SpellCraftTestCase;
import ru.vladislavkomkov.model.entity.unit.Unit;

public class DeepBluesTest extends SpellCraftTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new DeepBlues());
  }
  
  @Test
  void testIncBoostAfterUse()
  {
    testIncBoostAfterUseIter(false);
  }
  
  @Test
  void testIncBoostAfterUseGold()
  {
    testIncBoostAfterUseIter(true);
  }
  
  void testIncBoostAfterUseIter(boolean isGold)
  {
    int unitIndex = 0;
    
    Unit unit = new Unit()
    {
    };
    
    player.addToTable(unit, unitIndex);
    
    int attack = isGold ? DeepBlues.ATTACK_BOOST_GOLD : DeepBlues.ATTACK_BOOST;
    int health = isGold ? DeepBlues.HEALTH_BOOST_GOLD : DeepBlues.HEALTH_BOOST;
    
    for (int i = 1; i <= 100; i++)
    {
      Spell blues = new DeepBlues();
      blues.setIsGold(isGold);
      
      player.addToHand(Card.of(blues));
      
      int tempAttack = unit.getAttack();
      int tempHealth = unit.getHealth();
      
      String description = player.cloneHand().get(0).get().getDescription(player);
      assertTrue(description.contains("+" + (attack * i) + "/+" + (health * i)));
      
      player.playCard(game, 0, unitIndex);
      assertEquals(tempAttack + (attack * i), unit.getAttack());
      assertEquals(tempHealth + (health * i), unit.getHealth());
    }
  }
  
  @Test
  void testDisappearOnStartTurnOnce()
  {
    int unitIndex = 0;
    
    Unit unit = new Unit()
    {
    };
    
    player.addToTable(unit, unitIndex);
    
    player.addToHand(Card.of(new DeepBlues()));
    
    int tempAttack = unit.getAttack();
    int tempHealth = unit.getHealth();
    
    player.playCard(game, 0, unitIndex);
    
    assertEquals(tempAttack + (DeepBlues.ATTACK_BOOST), unit.getAttack());
    assertEquals(tempHealth + (DeepBlues.HEALTH_BOOST), unit.getHealth());
    
    game.processStartTurn(player);
    
    assertEquals(tempAttack, unit.getAttack());
    assertEquals(tempHealth, unit.getHealth());
  }
}
