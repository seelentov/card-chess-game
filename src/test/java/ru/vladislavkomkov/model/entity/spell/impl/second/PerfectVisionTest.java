package ru.vladislavkomkov.model.entity.spell.impl.second;

import org.junit.jupiter.api.Test;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.SpellTestCase;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.demon.first.IckyImp;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.entity.unit.impl.undead.first.RisenRider;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PerfectVisionTest extends SpellTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new PerfectVision());
  }
  
  @Test
  void testOnPlayedLess()
  {
    player.addToTable(new Cat());
    player.addToHand(Card.of(new PerfectVision(player)));
    
    player.playCard(0, 0);
    
    assertEquals(PerfectVision.ATTACK, player.getTable().get(0).getAttack());
    assertEquals(PerfectVision.HEALTH, player.getTable().get(0).getHealth());
  }
  
  @Test
  void testOnPlayedMore()
  {
    Unit unit11 = new Cat(player);
    unit11.addBuff(new Buff(
        unit -> {
          unit.incBaseAttack(30);
          unit.incHealth(30);
        },
        null,
        null));
    
    player.addToTable(unit11);
    player.addToHand(Card.of(new PerfectVision(player)));
    
    player.playCard(0, 0);
    
    assertEquals(PerfectVision.ATTACK, player.getTable().get(0).getAttack());
    assertEquals(PerfectVision.HEALTH, player.getTable().get(0).getHealth());
  }
  
  @Test
  void testOnPlayedTriplet()
  {
    int attackBoostTest = 30;
    int healthBoostTest = 30;
    
    Unit unit11 = new RisenRider(player);
    unit11.addBuff(new Buff(
        unit -> {
          unit.incBaseAttack(attackBoostTest);
          unit.incHealth(healthBoostTest);
        },
        null,
        null));
    
    Unit unit12 = new RisenRider(player);
    unit12.addBuff(new Buff(
            unit -> {
              unit.incBaseAttack(attackBoostTest);
              unit.incHealth(healthBoostTest);
            },
            null,
            null));

    player.addToTable(unit11);
    player.addToTable(unit12);
    player.addToHand(Card.of(new PerfectVision(player)));
    
    player.playCard(0, 1);
    
    player.addToHand(Card.of(new RisenRider(player)));
    
    player.playCard(0, 0);
    
    assertEquals(
        attackBoostTest + (PerfectVision.ATTACK - new RisenRider().getAttack()) + new RisenRider().buildGold().getAttack(),
        player.getTable().get(0).getAttack());
  }
}
