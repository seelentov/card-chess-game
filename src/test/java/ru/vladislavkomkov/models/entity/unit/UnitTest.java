package ru.vladislavkomkov.models.entity.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.GamePlayerTestCase;
import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.entity.unit.impl.beast.first.Alleycat;
import ru.vladislavkomkov.models.entity.unit.impl.mech.fourth.AccordoTron;
import ru.vladislavkomkov.models.player.Player;

public class UnitTest extends GamePlayerTestCase
{
  @Test
  void testOnStartTurn()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Unit unit = new Unit()
    {
      public void onStartTurn(Game game, Player player)
      {
        super.onStartTurn(game, player);
        player.addMoney(moneyStep);
      }
    };
    
    player.addToTable(unit, 0);
    game.processStartTurn(player);
    
    assertEquals(moneyStart + (moneyStep), player.getMoney());
  }
  
  @Test
  void testOnEndTurn()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Unit unit = new Unit()
    {
      public void onEndTurn(Game game, Player player)
      {
        super.onEndTurn(game, player);
        player.addMoney(moneyStep);
      }
    };
    
    player.addToTable(unit, 0);
    game.processEndTurn(player);
    
    assertEquals(moneyStart + (moneyStep), player.getMoney());
  }
  
  @Test
  void testOnStartFight()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Unit unit = new Unit()
    {
      public void onStartFight(Game game, Player player, Player player2)
      {
        super.onStartFight(game, player, player2);
        player.addMoney(moneyStep);
      }
    };
    
    player.addToTable(unit, 0);
    game.processStartFight(player, player2);
    
    assertEquals(moneyStart + (moneyStep), player.getMoney());
  }
  
  @Test
  void testOnEndFight()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Unit unit = new Unit()
    {
      public void onEndFight(Game game, Player player, Player player2)
      {
        super.onEndFight(game, player, player2);
        player.addMoney(moneyStep);
      }
    };
    
    player.addToTable(unit, 0);
    game.processEndFight(player, player2);
    
    assertEquals(moneyStart + (moneyStep), player.getMoney());
  }
  
  @Test
  void testOnAttacked()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Unit unit = new Unit()
    {
      public void onAttacked(Game game, Player player, Player player2, Unit attacker)
      {
        super.onAttacked(game, player, player2, attacker);
        player.addMoney(moneyStep);
      }
    };
    
    Unit unit2 = new Unit()
    {
    };
    
    player.addToTable(unit, 0);
    unit.onAttacked(game, player, player2, unit2);
    
    assertEquals(moneyStart + (moneyStep), player.getMoney());
  }
  
  @Test
  void testOnAttack()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Unit unit = new Unit()
    {
      public void onAttack(Game game, Player player, Player player2, Unit attacked)
      {
        super.onAttack(game, player, player2, attacked);
        player.addMoney(moneyStep);
      }
    };
    
    Unit unit2 = new Unit()
    {
    };
    
    player.addToTable(unit, 0);
    unit.onAttack(game, player, player2, unit2);
    
    assertEquals(moneyStart + (moneyStep), player.getMoney());
  }
  
  @Test
  void testOnDead()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Unit unit = new Unit()
    {
      public void onDead(Game game, Player player, Player player2, Unit attacker)
      {
        super.onDead(game, player, player2, attacker);
        player.addMoney(moneyStep);
      }
    };
    
    Unit unit2 = new Unit()
    {
    };
    
    player.addToTable(unit, 0);
    unit.onDead(game, player, player2, unit2);
    
    assertEquals(moneyStart + (moneyStep), player.getMoney());
  }
  
  @Test
  void testClone()
  {
    Unit unit = new AccordoTron();
    Alleycat enemy = new Alleycat();
    unit.onAttacked(game, player, player2, enemy);
    
    assertEquals(unit.maxHealth - enemy.getAttack(), unit.getHealth());
    
    Unit unitClone = unit.clone();
    assertEquals(unit.getHealth(), unitClone.getHealth());
  }
  
  @Test
  void testIsDead()
  {
    Unit unit = new Unit()
    {
    };
    int health = unit.getHealth();
    
    Unit unit2 = new Unit()
    {
      private int attack = health;
      
      @Override
      public int getAttack()
      {
        return attack;
      }
    };
    
    assertFalse(unit.isDead());
    unit.onAttacked(game, player, player2, unit2);
    assertTrue(unit.isDead());
  }
}
