package ru.vladislavkomkov.model.entity.spell.impl.fourth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.vladislavkomkov.model.card.Card.of;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.spell.SpellTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

public class AzeriteEmpowermentTest extends SpellTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new AzeriteEmpowerment());
  }
  
  @Test
  protected void testOnPlayed()
  {
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    player.addToTable(new Cat(player));
    
    player.addToHand(of(new AzeriteEmpowerment(player)));
    player.playCard(0);
    
    player.getTable().forEach(unit -> {
      assertEquals(unit.newThis().getAttack() + (AzeriteEmpowerment.ATTACK_BOOST * 2), unit.getAttack());
      assertEquals(unit.newThis().getHealth() + (AzeriteEmpowerment.HEALTH_BOOST * 2), unit.getHealth());
    });
  }
}