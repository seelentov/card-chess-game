package ru.vladislavkomkov.model.entity.spell.impl.first;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.SpellTestCase;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

public class SanctifyTest extends SpellTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new Sanctify());
  }
  
  @Test
  protected void testOnPlayed()
  {
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    
    Unit unit13 = new Cat(player);
    unit13.setIsBubbled(true);
    player.addToTable(unit13);
    
    Unit unit14 = new Cat(player);
    unit14.setIsBubbled(true);
    player.addToTable(unit14);
    
    player.addToHand(Card.of(new Sanctify(player)));
    
    player.playCard(0);
    
    player.getTable().forEach(unit -> {
      if (unit.isBubbled())
      {
        assertEquals(unit.newThis().getAttack() + Sanctify.ATTACK_BOOST, unit.getAttack());
      }
      else
      {
        assertEquals(unit.newThis().getAttack(), unit.getAttack());
      }
    });
  }
}