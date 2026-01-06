package ru.vladislavkomkov.model.entity.spell.impl.first;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.SpellTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

public class PointyArrowTest extends SpellTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new PointyArrow());
  }
  
  @Test
  protected void testOnPlayed()
  {
    player.addToTable(new Cat(player));
    player.addToHand(Card.of(new PointyArrow(player)));
    player.playCard(0, 0);
    
    assertEquals(new Cat().getAttack() + PointyArrow.ATTACK_BOOST, player.getTable().get(0).getAttack());
  }
}
