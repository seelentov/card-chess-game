package ru.vladislavkomkov.model.entity.spell.impl.second;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.SpellTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.demon.first.IckyImp;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

public class LostStaffofHamuulTest extends SpellTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new LostStaffofHamuul());
  }
  
  @Test
  protected void testOnPlayed()
  {
    player.addToTable(new Cat(player));
    player.addToTable(new IckyImp(player));

    player.addToHand(Card.of(new LostStaffofHamuul(player)));
    player.addToHand(Card.of(new LostStaffofHamuul(player)));

    player.playCard(0, 0);
    
    player.getTavern().getUnits().forEach(unit -> {
      assertTrue(unit.isType(new Cat().getType()));
    });
    
    player.playCard(0, 1);
    
    player.getTavern().getUnits().forEach(unit -> {
      assertTrue(unit.isType(new IckyImp().getType()));
      assertTrue(unit.getLevel() <= player.getLevel());
    });
  }
}
