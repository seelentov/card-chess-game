package ru.vladislavkomkov.model.entity.spell.impl.fourth;

import org.junit.jupiter.api.Test;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.SpellTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.beast.sixth.GoldrinnTheGreatWolf;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EyesoftheEarthMotherTest extends SpellTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new EyesoftheEarthMother());
  }
  
  @Test
  void testOnPlayedBelowMax()
  {
    player.addToHand(Card.of(new EyesoftheEarthMother(player)));
    player.addToTable(new Cat());
    
    player.playCard(0, 0);
    
    assertTrue(player.getTable().get(0).isGold());
  }
  
  @Test
  void testOnPlayedHigherMax()
  {
    player.addToHand(Card.of(new EyesoftheEarthMother(player)));
    player.addToTable(new GoldrinnTheGreatWolf());
    
    player.playCard(0, 0);
    
    assertFalse(player.getTable().get(0).isGold());
  }
}
