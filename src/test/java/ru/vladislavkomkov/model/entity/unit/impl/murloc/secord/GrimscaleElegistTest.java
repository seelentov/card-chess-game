package ru.vladislavkomkov.model.entity.unit.impl.murloc.secord;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

public class GrimscaleElegistTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    testDefault(new GrimscaleElegist());
  }
  
  @Test
  void testOnEndTurn()
  {
    player.addToTable(new GrimscaleElegist(player));
    
    player.addToHand(Card.of(new Cat(player)));
    
    game.processEndTurn(player);
    
    assertEquals(new GrimscaleElegist().getAttack() + GrimscaleElegist.ATTACK_BOOST, player.getTable().get(0).getAttack());
    assertEquals(new GrimscaleElegist().getHealth() + GrimscaleElegist.HEALTH_BOOST, player.getTable().get(0).getHealth());
    
    assertEquals(new Cat().getAttack() + GrimscaleElegist.ATTACK_BOOST, ((Unit) player.getHand().get(0).getEntity()).getAttack());
    assertEquals(new Cat().getHealth() + GrimscaleElegist.HEALTH_BOOST, ((Unit) player.getHand().get(0).getEntity()).getHealth());
  }
}
