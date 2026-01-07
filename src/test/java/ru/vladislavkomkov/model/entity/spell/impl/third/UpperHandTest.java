package ru.vladislavkomkov.model.entity.spell.impl.third;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.SpellTestCase;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.fight.Fight;

public class UpperHandTest extends SpellTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new UpperHand());
  }
  
  @Test
  void testOnPlayed()
  {
    player.addToHand(Card.of(new UpperHand(player)));
    player.playCard(0);
    
    Unit unit21 = new Cat(player2);
    unit21.setHealth(20);
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    
    game.processStartFight(fight, player, player2);
    
    assertEquals(UpperHand.HEALTH, fight.getFightTable(player2).get(0).getHealth());
  }
}
