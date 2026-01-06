package ru.vladislavkomkov.model.entity.unit.impl.beast.third;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.unit.AttacksCount;
import ru.vladislavkomkov.model.entity.unit.ChoicerTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

public class SprightlyScarabTest extends ChoicerTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new SprightlyScarab());
  }
  
  @Test
  void testOnPlayedChoice1()
  {
    player.addToHand(Card.of(new SprightlyScarab()));
    player.addToTable(new Cat());
    
    player.playCard(0, List.of(1, 0, 0, 0));
    
    assertEquals(new Cat().getAttack() + SprightlyScarab.SprightlySprucing.ATTACK_BOOST, player.getTable().get(0).getAttack());
    assertEquals(new Cat().getHealth() + SprightlyScarab.SprightlySprucing.HEALTH_BOOST, player.getTable().get(0).getHealth());
    assertTrue(player.getTable().get(0).isRebirth());
  }
  
  @Test
  void testOnPlayedChoice2()
  {
    player.addToHand(Card.of(new SprightlyScarab()));
    player.addToTable(new Cat());
    
    player.playCard(0, List.of(1, 1, 0, 0));
    
    assertEquals(new Cat().getAttack() + SprightlyScarab.SprightlySupport.ATTACK_BOOST, player.getTable().get(0).getAttack());
    assertEquals(AttacksCount.DOUBLE, player.getTable().get(0).getAttacksCount());
  }
  
  @Test
  void testOnPlayedChoice1Gold()
  {
    player.addToHand(Card.of(new SprightlyScarab().buildGold()));
    player.addToTable(new Cat());
    
    player.playCard(0, List.of(1, 0, 0, 0));
    
    assertEquals(new Cat().getAttack() + (SprightlyScarab.SprightlySprucing.ATTACK_BOOST * 2), player.getTable().get(0).getAttack());
    assertEquals(new Cat().getHealth() + (SprightlyScarab.SprightlySprucing.HEALTH_BOOST * 2), player.getTable().get(0).getHealth());
    assertTrue(player.getTable().get(0).isRebirth());
  }
  
  @Test
  void testOnPlayedChoice2Gold()
  {
    player.addToHand(Card.of(new SprightlyScarab().buildGold()));
    player.addToTable(new Cat());
    
    player.playCard(0, List.of(1, 1, 0, 0));
    
    assertEquals(new Cat().getAttack() + (SprightlyScarab.SprightlySupport.ATTACK_BOOST * 2), player.getTable().get(0).getAttack());
    assertEquals(AttacksCount.DOUBLE, player.getTable().get(0).getAttacksCount());
  }
  
  @Test
  void testOnPlayerSame()
  {
    player.addToHand(Card.of(new SprightlyScarab()));
    player.addToTable(new Cat());
    player.addToTable(new Cat());
    
    player.playCard(0, List.of(1, 1, 1, 0));
    
    assertEquals(new Cat().getAttack() + SprightlyScarab.SprightlySupport.ATTACK_BOOST, player.getTable().get(2).getAttack());
    assertEquals(AttacksCount.DOUBLE, player.getTable().get(2).getAttacksCount());
  }
  
  @Test
  void testOnPlayerPrev()
  {
    player.addToHand(Card.of(new SprightlyScarab()));
    player.addToTable(new Cat());
    player.addToTable(new Cat());
    
    player.playCard(0, List.of(1, 1, 0, 0));
    
    assertEquals(new Cat().getAttack() + SprightlyScarab.SprightlySupport.ATTACK_BOOST, player.getTable().get(0).getAttack());
    assertEquals(AttacksCount.DOUBLE, player.getTable().get(0).getAttacksCount());
  }
  
  @Test
  void testOnPlayerNext()
  {
    player.addToHand(Card.of(new SprightlyScarab()));
    player.addToTable(new Cat());
    player.addToTable(new Cat());
    player.addToTable(new Cat());
    
    player.playCard(0, List.of(1, 1, 2, 0));
    
    assertEquals(new Cat().getAttack() + SprightlyScarab.SprightlySupport.ATTACK_BOOST, player.getTable().get(3).getAttack());
    assertEquals(AttacksCount.DOUBLE, player.getTable().get(3).getAttacksCount());
  }
}
