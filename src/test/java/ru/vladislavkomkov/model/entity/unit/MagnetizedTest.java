package ru.vladislavkomkov.model.entity.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.GamePlayerTestCase;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.unit.impl.mech.fourth.AccordoTron;

public class MagnetizedTest extends GamePlayerTestCase
{
  @Test
  void testEraseOnPlayer()
  {
    player.addToHand(Card.of(new AccordoTron()));
    player.addToHand(Card.of(new AccordoTron()));
    
    player.playCard(0, 0);
    player.playCard(0, 0);
    
    assertEquals(1, player.getTable().size());
    assertEquals(new AccordoTron().getName(), player.getTable().get(0).getName());
  }
  
  @Test
  void testOnPlayed()
  {
    player.addToHand(Card.of(new AccordoTron()));
    player.addToHand(Card.of(new AccordoTron()));
    
    player.playCard(0, 0);
    player.playCard(0, 0);
    
    assertEquals(new AccordoTron().getAttack() * 2, player.getTable().get(0).getAttack());
    assertEquals(new AccordoTron().getHealth() * 2, player.getTable().get(0).getHealth());
    assertEquals(new AccordoTron().getMaxHealth() * 2, player.getTable().get(0).getMaxHealth());
  }
  
  @Test
  void testOnPlayedWithBuffs()
  {
    int buffIncAttack = 1;
    
    String buff1Name = "test1";
    String buff2Name = "test2";
    
    Unit unit = new AccordoTron();
    unit.addBuff(new Buff(u -> u.incBaseAttack(buffIncAttack), null, buff1Name));
    
    Unit unit2 = new AccordoTron();
    unit2.addBuff(new Buff(u -> u.incBaseAttack(buffIncAttack), null, buff2Name));
    
    player.addToHand(Card.of(unit));
    player.addToHand(Card.of(unit2));
    
    player.playCard(0, 0);
    player.playCard(0, 0);
    
    assertEquals(2, player.getTable().get(0).getBuffs().size());
    
    assertEquals(buff1Name, player.getTable().get(0).getBuffs().get(0).getDescription());
    assertEquals(buff2Name, player.getTable().get(0).getBuffs().get(1).getDescription());
  }
  
  @Test
  void testOnPlayedWithBooleans()
  {
    Unit unit = new AccordoTron();
    unit.setIsTaunt(false);
    unit.setIsBubbled(false);
    unit.setIsRebirth(false);
    unit.setIsMagnet(true);
    unit.setIsDisguise(true);
    unit.setIsAnswerOnPlayed(true);
    unit.setIsAnswerOnDead(true);
    
    Unit unit2 = new AccordoTron();
    unit2.setIsTaunt(true);
    unit2.setIsBubbled(true);
    unit2.setIsRebirth(true);
    unit2.setIsMagnet(false);
    unit2.setIsDisguise(false);
    unit2.setIsAnswerOnPlayed(false);
    unit2.setIsAnswerOnDead(false);
    
    player.addToHand(Card.of(unit));
    player.addToHand(Card.of(unit2));
    
    player.playCard(0, 0);
    player.playCard(0, 0);
    
    assertTrue(player.getTable().get(0).isTaunt);
    assertTrue(player.getTable().get(0).isBubbled);
    assertTrue(player.getTable().get(0).isRebirth);
    assertTrue(player.getTable().get(0).isMagnet);
    assertTrue(player.getTable().get(0).isDisguise);
    assertTrue(player.getTable().get(0).isAnswerOnPlayed);
    assertTrue(player.getTable().get(0).isAnswerOnDead);
  }
  
  @Test
  void testOnPlayedWithGold()
  {
    Unit unit = new AccordoTron();
    
    Unit unit2 = new AccordoTron().buildGold();
    
    player.addToHand(Card.of(unit));
    player.addToHand(Card.of(unit2));
    
    player.playCard(0, 0);
    player.playCard(0, 0);
    
    Unit playerUnit = player.getTable().get(0);
    
    assertEquals((new AccordoTron().buildGold().getAttack() + new AccordoTron().getAttack()), playerUnit.getAttack());
    assertEquals((new AccordoTron().buildGold().getHealth() + new AccordoTron().getHealth()), playerUnit.getHealth());
    assertEquals((new AccordoTron().buildGold().getMaxHealth() + new AccordoTron().getMaxHealth()), playerUnit.getMaxHealth());
  }
  
  @Test
  void testOnPlayedListeners()
  {
    int goldIncrement = 10;
    
    Unit unit = new AccordoTron();
    Unit unit2 = new AccordoTron();
    
    Stream.of(unit, unit2)
        .forEach((unit1) -> unit1.getListener().onStartTurnListeners.put(KEY_CORE, (game, fight, player) -> player.addMoney(goldIncrement)));
    
    player.addToHand(Card.of(unit));
    player.addToHand(Card.of(unit2));
    
    player.playCard(0, 0);
    player.playCard(0, 0);
    
    player.setMoney(0);
    
    game.processStartTurn(player);
    
    assertEquals(goldIncrement * 2, player.getMoney());
  }
  
  @Test
  void testOnPlayedNotMagnetize()
  {
    player.addToHand(Card.of(new AccordoTron()));
    player.addToHand(Card.of(new AccordoTron()));
    
    player.playCard(0, 0);
    player.playCard(0, 1);
    
    assertEquals(2, player.getTable().size());
  }
}
