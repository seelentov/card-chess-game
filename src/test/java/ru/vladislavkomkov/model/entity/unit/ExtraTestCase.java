package ru.vladislavkomkov.model.entity.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.unit.impl.Extra;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Alleycat;
import ru.vladislavkomkov.model.entity.unit.impl.beast.second.SewerRat;
import ru.vladislavkomkov.model.entity.unit.impl.mech.fourth.AccordoTron;
import ru.vladislavkomkov.model.entity.unit.impl.murloc.secord.GrimscaleElegist;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.HalfShell;

public class ExtraTestCase extends UnitTestCase
{
  protected void testDefault(Extra extra)
  {
    testOnPlayedTwice((Extra) extra.newThis());
    testOnDeadExtra((Extra) extra.newThis());
    testOnEndTurnTwice((Extra) extra.newThis());
    
    super.testDefault(extra);
  }
  
  void testOnPlayedTwice(Extra extra)
  {
    if (extra.getOnPlayedExtra().getIsTwice() == 0)
    {
      return;
    }
    
    setUp();
    
    player.addToTable(extra);
    player.addToHand(Card.of(new Alleycat(player)));
    player.playCard(0, 1);
    
    for (int i = 2; i < extra.getOnPlayedExtra().getIsTwice() + 3; i++)
    {
      assertEquals(player.getTable().get(i).getName(), new Cat().getName());
    }
    
    tearDown();
  }
  
  void testOnDeadExtra(Extra extra)
  {
    if (extra.getOnDeadExtra().getIsExtra() == 0)
    {
      return;
    }
    
    setUp();
    
    Unit unit = new SewerRat(player);
    
    player.addToTable(extra);
    player.addToTable(unit);
    
    unit.onDead(game, null, player, null, null, false);
    
    for (int i = 2; i < extra.getOnDeadExtra().getIsExtra() + 3; i++)
    {
      assertEquals(player.getTable().get(i).getName(), new HalfShell().getName());
    }
    
    tearDown();
  }
  
  void testOnEndTurnTwice(Extra extra)
  {
    if (extra.getOnEndTurnExtra().getIsTwice() == 0)
    {
      return;
    }
    
    setUp();
    
    player.addToTable(extra);
    player.addToTable(new GrimscaleElegist(player));
    player.addToHand(Card.of(new Cat(player)));
    
    player.setMoney(0);
    
    game.processEndTurn(player);
    
    assertEquals(
        new Cat().getAttack() + (GrimscaleElegist.ATTACK_BOOST * 2),
        ((Unit) player.getHand().get(0).getEntity()).getAttack());
    
    tearDown();
  }
}
