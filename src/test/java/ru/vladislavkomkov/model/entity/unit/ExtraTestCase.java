package ru.vladislavkomkov.model.entity.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.unit.impl.Extra;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Alleycat;
import ru.vladislavkomkov.model.entity.unit.impl.beast.second.SewerRat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.HalfShell;

public class ExtraTestCase extends UnitTestCase
{
  protected void testDefault(Extra extra)
  {
    testOnPlayedTwice((Extra) extra.newThis());
    testOnDeadExtra((Extra) extra.newThis());
    
    super.testDefault(extra);
  }
  
  void testOnPlayedTwice(Extra extra)
  {
    if (extra.getOnPlayed().getIsTwice() == 0)
    {
      return;
    }
    
    setUp();
    
    player.addToTable(extra);
    player.addToHand(Card.of(new Alleycat(player)));
    player.playCard(0, 1);
    
    for (int i = 2; i < extra.getOnPlayed().getIsTwice() + 3; i++)
    {
      assertEquals(player.getTable().get(i).getName(), new Cat().getName());
    }
    
    tearDown();
  }
  
  void testOnDeadExtra(Extra extra)
  {
    if (extra.getOnDead().getIsExtra() == 0)
    {
      return;
    }
    
    setUp();
    
    Unit unit = new SewerRat(player);
    
    player.addToTable(extra);
    player.addToTable(unit);
    
    unit.onDead(game, null, player, null, null, false);
    
    for (int i = 2; i < extra.getOnDead().getIsExtra() + 3; i++)
    {
      assertEquals(player.getTable().get(i).getName(), new HalfShell().getName());
    }
    
    tearDown();
  }
}
