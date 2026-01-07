package ru.vladislavkomkov.model.entity.spell.impl.third;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.SpellTestCase;
import ru.vladislavkomkov.model.entity.unit.Unit;

public class HiredHeadhunterTest extends SpellTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new HiredHeadhunter());
  }
  
  @Test
  void testOnPlayed()
  {
    player.addToHand(Card.of(new HiredHeadhunter(player)));
    player.playCard(0);
    
    AtomicReference<String> k = new AtomicReference<>();
    player.getSenderWaiters().keySet().forEach(k::set);
    
    player.doSenderWaiter(k.get(), 0);
    
    assertInstanceOf(Unit.class, player.getHand().get(0).getEntity());
    assertTrue(((Unit) player.getHand().get(0).getEntity()).isAnswerOnPlayed());
  }
}
