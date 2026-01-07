package ru.vladislavkomkov.model.entity.spell.impl;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.SpellTestCase;
import ru.vladislavkomkov.model.entity.unit.Unit;

public class TripleRewardTest extends SpellTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new TripleReward());
  }
  
  @Test
  void testOnPlayed()
  {
    player.addToHand(Card.of(new TripleReward(player, 1)));
    player.playCard(0);
    
    AtomicReference<String> k = new AtomicReference<>();
    player.getSenderWaiters().keySet().forEach(k::set);
    
    player.doSenderWaiter(k.get(), 0);
    
    assertInstanceOf(Unit.class, player.getHand().get(0).getEntity());
  }
}
