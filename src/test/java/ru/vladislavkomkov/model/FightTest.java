package ru.vladislavkomkov.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.GamePlayerTestCase;
import ru.vladislavkomkov.model.entity.unit.AttacksCount;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Alleycat;
import ru.vladislavkomkov.model.entity.unit.impl.demon.first.IckyImp;
import ru.vladislavkomkov.model.entity.unit.impl.dragon.fourth.Greenskeeper;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.demon.first.Imp;
import ru.vladislavkomkov.model.entity.unit.impl.undead.first.RisenRider;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.fight.FightInfo;
import ru.vladislavkomkov.model.player.Player;

public class FightTest extends GamePlayerTestCase
{
  @Test
  void testFightDraw()
  {
    Unit unit = new Cat();
    unit.incBaseAttack(2);
    unit.incHealth(6);
    
    Unit unit2 = new Cat();
    unit2.incBaseAttack(2);
    unit2.incHealth(6);
    
    player.addToTable(unit, 0);
    player2.addToTable(unit2, 0);
    
    Fight fight = new Fight(game, player, player2);
    
    unit = fight.getFightTable(player).get(0);
    unit2 = fight.getFightTable(player2).get(0);
    
    for (int i = 1; i <= 3; i++)
    {
      assertFalse(fight.doTurn().isPresent());
      
      assertEquals(unit.getMaxHealth() - (unit2.getAttack() * i), unit.getHealth());
      assertEquals(unit2.getMaxHealth() - (unit.getAttack() * i), unit2.getHealth());
    }
    
    assertTrue(fight.doTurn().isPresent());
  }
  
  @Test
  void testFightWin()
  {
    Unit unit = new Cat();
    unit.incBaseAttack(2);
    unit.incHealth(6);
    
    Unit unit2 = new Cat();
    unit2.incBaseAttack(2);
    unit2.incHealth(5);
    
    player.addToTable(unit, 0);
    player2.addToTable(unit2, 0);
    
    Fight fight = new Fight(game, player, player2);
    
    unit = fight.getFightTable(player).get(0);
    unit2 = fight.getFightTable(player2).get(0);
    
    for (int i = 1; i <= 2; i++)
    {
      assertFalse(fight.doTurn().isPresent());
      
      assertEquals(unit.getMaxHealth() - (unit2.getAttack() * i), unit.getHealth());
      assertEquals(unit2.getMaxHealth() - (unit.getAttack() * i), unit2.getHealth());
    }
    
    assertTrue(fight.doTurn().isPresent());
    
    assertEquals(player2.getMaxHealth() - (unit.getLevel() + player.getLevel()), player2.getHealth());
  }
  
  @Test
  void testFightDestruction()
  {
    for (int i = 0; i < 3; i++)
    {
      player.addToTable(new Cat(), -1);
    }
    
    for (int i = 0; i < 3; i++)
    {
      player2.addToTable(new Cat(), 0);
    }
    
    Fight fight = new Fight(game, player, player2);
    
    for (int i = 2; i >= 0; i--)
    {
      assertFalse(fight.doTurn().isPresent());
      
      assertEquals(i, fight.getPlayer1Units().size());
      assertEquals(i, fight.getPlayer2Units().size());
    }
    
    assertTrue(fight.doTurn().isPresent());
  }
  
  @Test
  void testFightIsBubbled()
  {
    Unit unit = new Cat();
    Unit unit2 = new Cat();
    unit.setIsBubbled(true);
    unit2.setIsBubbled(true);
    
    player.addToTable(unit, -1);
    player2.addToTable(unit2, -1);
    
    Fight fight = new Fight(game, player, player2);
    
    assertFalse(fight.doTurn().isPresent());
    
    assertEquals(1, fight.getPlayer1Units().size());
    assertEquals(1, fight.getPlayer2Units().size());
  }
  
  @Test
  void testFightOneIsBubbled()
  {
    Unit unit = new Cat();
    Unit unit2 = new Cat();
    
    unit.setIsBubbled(true);
    
    player.addToTable(unit, -1);
    player2.addToTable(unit2, -1);
    
    Fight fight = new Fight(game, player, player2);
    
    assertFalse(fight.doTurn().isPresent());
    
    assertEquals(1, fight.getPlayer1Units().size());
    assertEquals(0, fight.getPlayer2Units().size());
    
    assertTrue(fight.doTurn().isPresent());
    
    assertEquals(player2.getMaxHealth() - (unit.getLevel() + player.getLevel()), player2.getHealth());
  }
  
  @Test
  void testFightIsRebirth()
  {
    Unit unit = new RisenRider();
    Unit unit2 = new Cat();
    Unit unit3 = new Cat();
    
    player.addToTable(unit, -1);
    player2.addToTable(unit2, -1);
    player2.addToTable(unit3, -1);
    
    Fight fight = new Fight(game, player, player2);
    
    assertTrue(fight.getPlayer1Units().get(0).isRebirth());
    
    assertEquals(1, fight.getPlayer1Units().size());
    assertEquals(2, fight.getPlayer2Units().size());
    
    assertFalse(fight.doTurn().isPresent());
    
    assertEquals(1, fight.getPlayer1Units().size());
    assertEquals(1, fight.getPlayer2Units().size());
    
    assertFalse(fight.getPlayer1Units().get(0).isRebirth());
    
    assertFalse(fight.doTurn().isPresent());
    
    assertEquals(0, fight.getPlayer1Units().size());
    assertEquals(0, fight.getPlayer2Units().size());
    
    assertTrue(fight.doTurn().isPresent());
  }
  
  @Test
  void testFightOnDead()
  {
    Unit unit = new IckyImp();
    Unit unit2 = new Cat();
    
    player.addToTable(unit, -1);
    player2.addToTable(unit2, -1);
    
    Fight fight = new Fight(game, player, player2);
    
    assertEquals(1, fight.getPlayer1Units().size());
    assertEquals(1, fight.getPlayer2Units().size());
    
    assertFalse(fight.doTurn().isPresent());
    
    assertEquals(2, fight.getPlayer1Units().size());
    assertEquals(0, fight.getPlayer2Units().size());
    
    assertEquals(new Imp().getName(), fight.getPlayer1Units().get(0).getName());
    assertEquals(new Imp().getName(), fight.getPlayer1Units().get(1).getName());
    
    assertEquals(1, player.getUnitsCount());
    
    assertEquals(new IckyImp().getName(), player.cloneTable().get(0).getName());
    
    assertTrue(fight.doTurn().isPresent());
  }
  
  @Test
  void testFightOnDeadSummonInPlace()
  {
    Unit unit = new Cat();
    unit.setIsDisguise(true);
    player2.addToTable(unit, -1);
    
    player2.addToTable(new IckyImp(), -1);
    
    for (int i = 0; i < 4; i++)
    {
      Unit u = new Cat();
      u.setIsDisguise(true);
      player2.addToTable(u, -1);
    }
    
    Unit enUnit = new Cat();
    enUnit.incBaseAttack(99);
    player.addToTable(enUnit, -1);
    
    Fight fight = new Fight(game, player, player2);
    
    assertFalse(fight.doTurn().isPresent());
    
    assertEquals(0, fight.getPlayer1Units().size());
    
    assertEquals(7, fight.getPlayer2Units().size());
    assertEquals(new Cat().getName(), fight.getPlayer2Units().get(0).getName());
    
    for (int i = 1; i < 3; i++)
    {
      assertEquals(new Imp().getName(), fight.getPlayer2Units().get(i).getName());
    }
    
    for (int i = 3; i < 7; i++)
    {
      assertEquals(new Cat().getName(), fight.getPlayer2Units().get(i).getName());
    }
  }
  
  @Test
  void testFightOnDeadSummonInPlaceIfOverflow()
  {
    player2.addToTable(new Cat(), -1);
    
    Unit unit = new IckyImp();
    unit.setIsTaunt(true);
    player2.addToTable(unit, -1);
    
    for (int i = 0; i < 6; i++)
    {
      Unit u = new Cat();
      player2.addToTable(u, -1);
    }
    
    Unit enUnit = new Cat();
    enUnit.incBaseAttack(99);
    player.addToTable(enUnit, -1);
    
    Fight fight = new Fight(game, player, player2);
    
    assertFalse(fight.doTurn().isPresent());
    
    assertEquals(0, fight.getPlayer1Units().size());
    
    assertEquals(7, fight.getPlayer2Units().size());
    assertEquals(new Cat().getName(), fight.getPlayer2Units().get(0).getName());
    
    assertEquals(new Imp().getName(), fight.getPlayer2Units().get(1).getName());
    
    for (int i = 2; i < 7; i++)
    {
      assertEquals(new Cat().getName(), fight.getPlayer2Units().get(i).getName());
    }
  }
  
  @Test
  void testFightOnDeadSummonInPlaceAndSummonedAttacker()
  {
  }
  
  @Test
  void testAttackOnlyTaunt()
  {
    int tauntHealth = 1000;
    
    for (int i = 0; i < 6; i++)
    {
      Unit u = new Cat();
      u.setBaseAttack(0);
      player.addToTable(u, -1);
    }
    Unit unit = new Cat();
    unit.setBaseAttack(0);
    unit.setHealth(tauntHealth);
    unit.setIsTaunt(true);
    player.addToTable(unit, -1);
    
    player2.addToTable(new Cat(), -1);
    
    Fight fight = new Fight(game, player, player2);
    
    for (int i = 0; i < tauntHealth; i++)
    {
      assertFalse(fight.doTurn().isPresent());
    }
    
    assertEquals(7, fight.getFightTable(player).size());
    
    for (int i = 0; i < 6; i++)
    {
      assertEquals(new Cat().getName(), fight.getFightTable(player).get(i).getName());
      assertEquals(new Cat().getHealth(), fight.getFightTable(player).get(i).getHealth());
    }
    
    assertEquals(tauntHealth / 2, fight.getFightTable(player).get(6).getHealth());
  }
  
  @Test
  void testFightActivateOnPlayedInPlace()
  {
    player.addToTable(new Greenskeeper());
    player.addToTable(new Imp());
    player.addToTable(new Alleycat());
    player.addToTable(new Imp());
    player.addToTable(new Imp());
    player.addToTable(new Imp());
    
    player2.addToTable(new Imp());
    
    Fight fight = new Fight(game, player, player2);
    
    assertFalse(fight.doTurn().isPresent());
    
    assertEquals(7, fight.getFightTable(player).size());
    
    assertEquals(fight.getFightTable(player).get(0).getName(), new Greenskeeper().getName());
    assertEquals(fight.getFightTable(player).get(1).getName(), new Imp().getName());
    assertEquals(fight.getFightTable(player).get(2).getName(), new Alleycat().getName());
    assertEquals(fight.getFightTable(player).get(3).getName(), new Cat().getName());
    assertEquals(fight.getFightTable(player).get(4).getName(), new Imp().getName());
    assertEquals(fight.getFightTable(player).get(5).getName(), new Imp().getName());
    assertEquals(fight.getFightTable(player).get(6).getName(), new Imp().getName());
    
    assertTrue(fight.getFightTable(player2).isEmpty());
  }
  
  @Test
  void testFightActivateOnPlayedOverflow()
  {
    player.addToTable(new Greenskeeper());
    player.addToTable(new Imp());
    player.addToTable(new Alleycat());
    player.addToTable(new Imp());
    player.addToTable(new Imp());
    player.addToTable(new Imp());
    player.addToTable(new Imp());
    
    player2.addToTable(new Imp());
    
    Fight fight = new Fight(game, player, player2);
    
    assertFalse(fight.doTurn().isPresent());
    
    assertEquals(7, fight.getFightTable(player).size());
    
    assertEquals(fight.getFightTable(player).get(0).getName(), new Greenskeeper().getName());
    assertEquals(fight.getFightTable(player).get(1).getName(), new Imp().getName());
    assertEquals(fight.getFightTable(player).get(2).getName(), new Alleycat().getName());
    assertEquals(fight.getFightTable(player).get(3).getName(), new Imp().getName());
    assertEquals(fight.getFightTable(player).get(4).getName(), new Imp().getName());
    assertEquals(fight.getFightTable(player).get(5).getName(), new Imp().getName());
    assertEquals(fight.getFightTable(player).get(6).getName(), new Imp().getName());
    
    assertTrue(fight.getFightTable(player2).isEmpty());
  }
  
  @Test
  void testAllNullAttack()
  {
    for (int i = 0; i < 7; i++)
    {
      Unit unit = new Cat();
      unit.setBaseAttack(0);
      player.addToTable(unit);
    }
    
    for (int i = 0; i < 7; i++)
    {
      Unit unit = new Cat();
      unit.setBaseAttack(0);
      player2.addToTable(unit);
    }
    
    Fight fight = new Fight(game, player, player2);
    
    for (int i = 0; i < Integer.MAX_VALUE; i++)
    {
      if (fight.getTurn() >= Fight.TURN_LIMIT)
      {
        assertTrue(fight.doTurn().isPresent());
        break;
      }
      
      assertFalse(fight.doTurn().isPresent());
    }
  }
  
  @Test
  void testAllIsDisguise()
  {
    for (int i = 0; i < 7; i++)
    {
      Unit unit = new Cat();
      unit.setIsDisguise(true);
      player.addToTable(unit);
    }
    
    for (int i = 0; i < 7; i++)
    {
      Unit unit = new Cat();
      unit.setIsDisguise(true);
      player2.addToTable(unit);
    }
    
    Fight fight = new Fight(game, player, player2);
    
    for (int i = 0; i < Integer.MAX_VALUE; i++)
    {
      if (fight.getTurn() >= Fight.TURN_LIMIT)
      {
        assertTrue(fight.doTurn().isPresent());
        break;
      }
      
      assertFalse(fight.doTurn().isPresent());
    }
    
    assertEquals(player.getHealth(), player2.getHealth());
  }
  
  @Test
  void testFightsOrder() throws Exception
  {
    Map<String, Player> players = new HashMap<>();
    players.put("1", new Player("1", game));
    players.put("2", new Player("2", game));
    players.put("3", new Player("3", game));
    players.put("4", new Player("4", game));
    players.put("5", new Player("5", game));
    players.put("6", new Player("6", game));
    players.put("7", new Player("7", game));
    players.put("8", new Player("8", game));
    
    Game game = new Game(players);
    
    for (int i = 1; i <= 20; i++)
    {
      while (game.fightHistory.size() < 28)
      {
        game.fights.clear();
        game.calcFights();
        
        for (Fight fight : game.fights)
        {
          game.fightHistory.add(new FightInfo(fight.getPlayer1(), fight.getPlayer2(), FightInfo.Result.DRAW, 0, new ArrayList<>()));
        }
      }
      
      Map<String, Integer> fightCounter = new HashMap<>();
      
      for (FightInfo info : game.fightHistory)
      {
        String key = (info.player1.getUUID() + info.player2.getUUID());
        String key2 = (info.player2.getUUID() + info.player1.getUUID());
        
        fightCounter.merge(key, 1, Integer::sum);
        fightCounter.merge(key2, 1, Integer::sum);
      }
      
      List<Map.Entry<String, Integer>> fightCounterList = fightCounter.entrySet().stream()
          .sorted(Map.Entry.<String, Integer> comparingByValue())
          .limit(28)
          .toList();
      
      int finalI = i;
      assertEquals(
          0,
          fightCounterList.stream().filter(c -> c.getValue() > finalI).count(),
          fightCounterList.toString());
    }
    
    game.close();
  }
  
  /*
    Cat(1/1)
    Cat(2/2)
    Cat(3/3)
    Cat(4/4)
    Cat(5/5)
    Cat(6/6)
    Cat(10/99999)(taunt)
  
    Cat(1/1)
    Cat(2/2)
    Cat(3/3)
    Cat(4/4)
    Cat(5/5)
    Cat(6/6)
    Cat(10/99999)(taunt)
  
    Должны разбиться все в таунт друг друга по очереди: 1,2,3..6
  */
  @Test
  void testFightAttackOrder()
  {
    for (int i = 1; i < 7; i++)
    {
      Unit unit = new Cat();
      unit.setBaseAttack(i);
      unit.setHealth(i);
      player.addToTable(unit);
    }
    
    Unit taunt = new Cat();
    taunt.setIsTaunt(true);
    taunt.setBaseAttack(10);
    taunt.setHealth(99999);
    player.addToTable(taunt);
    
    for (int i = 1; i < 7; i++)
    {
      Unit unit = new Cat();
      unit.setBaseAttack(i);
      unit.setHealth(i);
      player2.addToTable(unit);
    }
    
    Unit taunt2 = new Cat();
    taunt2.setIsTaunt(true);
    taunt2.setBaseAttack(10);
    taunt2.setHealth(99999);
    player2.addToTable(taunt2);
    
    Fight fight = new Fight(game, player, player2);
    
    for (int i = 1; i < 7; i++)
    {
      assertEquals(i, fight.getFightTable(player).get(0).getAttack());
      assertEquals(i, fight.getFightTable(player2).get(0).getHealth());
      
      fight.doTurn();
      fight.doTurn();
    }
  }
  
  /*
  Cat(1/1)
  Icky Imp(2/2)(taunt)
  Cat(30/30)
  
  Cat(2/2)
  Cat(2/2)(taunt)
  Cat(3/3)
  
  Cat 1.1 разбивается об таунт. Cat 2.1 разивает таунт и 1 бьется призванными импами. Таунт 2 ломается и бьет 2.3
  */
  
  @Test
  void testFightAttackOrderWithOnDead()
  {
    Unit unit11 = new Cat();
    unit11.setBaseAttack(1);
    unit11.setHealth(1);
    player.addToTable(unit11);
    
    Unit unit12 = new IckyImp();
    unit12.setBaseAttack(2);
    unit12.setHealth(2);
    unit12.setIsTaunt(true);
    player.addToTable(unit12);
    
    Unit unit13 = new Cat();
    unit13.setBaseAttack(30);
    unit13.setHealth(30);
    player.addToTable(unit13);
    
    Unit unit21 = new Cat();
    unit21.setBaseAttack(2);
    unit21.setHealth(2);
    player2.addToTable(unit21);
    
    Unit unit22 = new Cat();
    unit22.setBaseAttack(2);
    unit22.setHealth(2);
    unit22.setIsTaunt(true);
    player2.addToTable(unit22);
    
    Unit unit23 = new Cat();
    unit23.setBaseAttack(3);
    unit23.setHealth(3);
    unit23.setIsTaunt(true);
    player2.addToTable(unit23);
    
    Fight fight = new Fight(game, player, player2);
    
    fight.doTurn();
    
    assertEquals(new IckyImp().getName(), fight.getFightTable(player).get(0).getName());
    assertEquals(2, fight.getFightTable(player).get(0).getAttack());
    assertEquals(2, fight.getFightTable(player).get(0).getHealth());
    assertTrue(fight.getFightTable(player).get(0).isTaunt());
    
    assertEquals(new Cat().getName(), fight.getFightTable(player).get(1).getName());
    assertEquals(30, fight.getFightTable(player).get(1).getAttack());
    assertEquals(30, fight.getFightTable(player).get(1).getHealth());
    
    assertEquals(new Cat().getName(), fight.getFightTable(player2).get(0).getName());
    assertEquals(2, fight.getFightTable(player2).get(0).getAttack());
    assertEquals(2, fight.getFightTable(player2).get(0).getHealth());
    
    assertEquals(new Cat().getName(), fight.getFightTable(player2).get(1).getName());
    assertEquals(2, fight.getFightTable(player2).get(1).getAttack());
    assertEquals(1, fight.getFightTable(player2).get(1).getHealth());
    assertTrue(fight.getFightTable(player2).get(1).isTaunt());
    
    assertEquals(new Cat().getName(), fight.getFightTable(player2).get(2).getName());
    assertEquals(3, fight.getFightTable(player2).get(2).getAttack());
    assertEquals(3, fight.getFightTable(player2).get(2).getHealth());
    
    fight.doTurn();
    
    assertEquals(new Imp().getName(), fight.getFightTable(player).get(0).getName());
    assertEquals(1, fight.getFightTable(player).get(0).getAttack());
    assertEquals(1, fight.getFightTable(player).get(0).getHealth());
    
    assertEquals(new Imp().getName(), fight.getFightTable(player).get(1).getName());
    assertEquals(1, fight.getFightTable(player).get(1).getAttack());
    assertEquals(1, fight.getFightTable(player).get(1).getHealth());
    
    assertEquals(new Cat().getName(), fight.getFightTable(player).get(2).getName());
    assertEquals(30, fight.getFightTable(player).get(2).getAttack());
    assertEquals(30, fight.getFightTable(player).get(2).getHealth());
    
    assertEquals(new Cat().getName(), fight.getFightTable(player2).get(0).getName());
    assertEquals(2, fight.getFightTable(player2).get(0).getAttack());
    assertEquals(1, fight.getFightTable(player2).get(0).getHealth());
    assertTrue(fight.getFightTable(player2).get(1).isTaunt());
    
    assertEquals(new Cat().getName(), fight.getFightTable(player2).get(1).getName());
    assertEquals(3, fight.getFightTable(player2).get(1).getAttack());
    assertEquals(3, fight.getFightTable(player2).get(1).getHealth());
    
    fight.doTurn();
    
    assertEquals(new Imp().getName(), fight.getFightTable(player).get(0).getName());
    assertEquals(1, fight.getFightTable(player).get(0).getAttack());
    assertEquals(1, fight.getFightTable(player).get(0).getHealth());
    
    assertEquals(new Cat().getName(), fight.getFightTable(player).get(1).getName());
    assertEquals(30, fight.getFightTable(player).get(1).getAttack());
    assertEquals(30, fight.getFightTable(player).get(1).getHealth());
    
    assertEquals(new Cat().getName(), fight.getFightTable(player2).get(0).getName());
    assertEquals(3, fight.getFightTable(player2).get(0).getAttack());
    assertEquals(3, fight.getFightTable(player2).get(0).getHealth());
  }
  
  @Test
  void testDoubleAttack()
  {
    Unit unit11 = new Cat();
    unit11.setAttacksCount(AttacksCount.DOUBLE);
    unit11.setBaseAttack(10);
    unit11.setHealth(10);
    player.addToTable(unit11);
    
    Unit unit21 = new Cat();
    unit21.setBaseAttack(1);
    unit21.setHealth(1);
    player2.addToTable(unit21);
    
    Unit unit22 = new Cat();
    unit22.setIsTaunt(true);
    unit22.setBaseAttack(1);
    unit22.setHealth(21);
    player2.addToTable(unit22);
    
    Fight fight = new Fight(game, player, player2);
    
    fight.doTurn();
    
    assertEquals(unit11.getHealth() - (unit22.getAttack() * 2), fight.getFightTable(player).get(0).getHealth());
    
    assertEquals(unit21.getHealth(), fight.getFightTable(player2).get(0).getHealth());
    assertEquals(unit22.getHealth() - (unit11.getAttack() * 2), fight.getFightTable(player2).get(1).getHealth());
  }
  
  @Test
  void testRebirth()
  {
    Unit unit11 = new Cat();
    unit11.setIsRebirth(true);
    unit11.setBaseAttack(10);
    unit11.setHealth(10);
    player.addToTable(unit11);
    
    Unit unit12 = new Cat();
    unit12.setBaseAttack(12);
    unit12.setHealth(100);
    unit12.setIsTaunt(true);
    player.addToTable(unit12);
    
    Unit unit21 = new Cat();
    unit21.setBaseAttack(11);
    unit21.setHealth(100);
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    
    fight.doTurn();
    
    assertEquals(1, fight.getFightTable(player).get(0).getHealth());
    assertFalse(fight.getFightTable(player).get(0).isRebirth());
    assertEquals(unit12.getHealth(), fight.getFightTable(player).get(1).getHealth());
    
    fight.doTurn();
    
    assertEquals(1, fight.getFightTable(player).get(0).getHealth());
    assertEquals(unit12.getHealth() - unit21.getAttack(), fight.getFightTable(player).get(1).getHealth());
    
    fight.doTurn();
    
    assertEquals(unit12.getHealth() - unit21.getAttack(), fight.getFightTable(player).get(0).getHealth());
  }
  
  @Test
  void testRebirthWithBuffs()
  {
    String buff1Description = "TEST";
    String buff2Description = "TEST2";
    
    Unit unit11 = new Cat();
    unit11.setIsRebirth(true);
    unit11.addBuff(new Buff(buff1Description));
    player.addToTable(unit11);
    
    Unit unit21 = new Cat();
    unit21.setBaseAttack(100);
    unit21.setHealth(100);
    
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    fight.getFightTable(player).get(0).getBuffs().add(new Buff(buff2Description));
    
    fight.doTurn();
    
    assertEquals(1, fight.getFightTable(player).get(0).getBuffs().size());
    assertEquals(buff1Description, fight.getFightTable(player).get(0).getBuffs().get(0).getDescription());
  }
  
  @Test
  void testOnDeadSummon()
  {
    player.addToTable(new IckyImp(player));
    player.addToTable(new IckyImp(player));
    player.addToTable(new IckyImp(player));
    player.addToTable(new IckyImp(player));
    player.addToTable(new IckyImp(player));
    player.addToTable(new IckyImp(player));
    
    Unit unit21 = new Cat(player2);
    unit21.setHealth(9999);
    unit21.setBaseAttack(9999);
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    
    fight.doTurn();
    
    assertEquals(2, fight.getFightTable(player).stream().filter(unit -> unit.getName().equals(new Imp().getName())).count());
  }
  
  @Test
  void testOnDeadSummonOverflow()
  {
    player.addToTable(new IckyImp(player));
    player.addToTable(new IckyImp(player));
    player.addToTable(new IckyImp(player));
    player.addToTable(new IckyImp(player));
    player.addToTable(new IckyImp(player));
    player.addToTable(new IckyImp(player));
    player.addToTable(new IckyImp(player));
    
    Unit unit21 = new Cat(player2);
    unit21.setHealth(9999);
    unit21.setBaseAttack(9999);
    player2.addToTable(unit21);
    
    Fight fight = new Fight(game, player, player2);
    
    fight.doTurn();
    
    assertEquals(1, fight.getFightTable(player).stream().filter(unit -> unit.getName().equals(new Imp().getName())).count());
  }
}
