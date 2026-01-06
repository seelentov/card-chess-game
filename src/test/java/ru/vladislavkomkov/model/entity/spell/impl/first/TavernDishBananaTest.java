package ru.vladislavkomkov.model.entity.spell.impl.first;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.SpellTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

public class TavernDishBananaTest extends SpellTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new TavernDishBanana());
  }
  
  @Test
  protected void testOnPlayed()
  {
    player.addToTable(new Cat(player));
    player.addToHand(Card.of(new TavernDishBanana(player)));
    player.playCard(0, 0);
    
    assertEquals(new Cat().getAttack() + TavernDishBanana.ATTACK_BOOST, player.getTable().get(0).getAttack());
    assertEquals(new Cat().getHealth() + TavernDishBanana.HEALTH_BOOST, player.getTable().get(0).getHealth());
  }
}
