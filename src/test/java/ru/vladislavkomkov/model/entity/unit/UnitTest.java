package ru.vladislavkomkov.model.entity.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.GamePlayerTestCase;
import ru.vladislavkomkov.model.Fight;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.player.Player;

public class UnitTest extends GamePlayerTestCase
{
  @Test
  void testOnStartTurn()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Unit unit = new Unit()
    {
      @Override
      public void onStartTurn(Game game, Fight fight, Player player)
      {
        super.onStartTurn(game,null,  player);
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
      @Override
      public void onEndTurn(Game game, Fight fight, Player player)
      {
        super.onEndTurn(game, null, player);
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
      @Override
      public void onStartFight(Game game, Fight fight, Player player, Player player2)
      {
        super.onStartFight(game, null, player, player2);
        player.addMoney(moneyStep);
      }
    };
    
    player.addToTable(unit, 0);
    game.processStartFight(null, player, player2);
    
    assertEquals(moneyStart + (moneyStep), player.getMoney());
  }
  
  @Test
  void testOnEndFight()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Unit unit = new Unit()
    {
      @Override
      public void onEndFight(Game game, Fight fight, Player player, Player player2)
      {
        super.onEndFight(game, null, player, player2);
        player.addMoney(moneyStep);
      }
    };
    
    player.addToTable(unit, 0);
    game.processEndFight(null, player, player2);
    
    assertEquals(moneyStart + (moneyStep), player.getMoney());
  }
  
  @Test
  void testOnAttacked()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Unit unit = new Unit()
    {
      @Override
      public void onAttacked(Game game, Fight fight, Player player, Player player2, Unit attacker)
      {
        super.onAttacked(game, null, player, player2, attacker);
        player.addMoney(moneyStep);
      }
    };
    
    Unit unit2 = new Unit()
    {
    };
    
    player.addToTable(unit, 0);
    unit.onAttacked(game, null, player, player2, unit2);
    
    assertEquals(moneyStart + (moneyStep), player.getMoney());
  }
  
  @Test
  void testOnAttack()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Unit unit = new Unit()
    {
      @Override
      public void onAttack(Game game, Fight fight, Player player, Player player2, Unit attacked)
      {
        super.onAttack(game,null, player, player2, attacked);
        player.addMoney(moneyStep);
      }
    };
    
    Unit unit2 = new Unit()
    {
    };
    
    player.addToTable(unit, 0);
    unit.onAttack(game, null, player, player2, unit2);
    
    assertEquals(moneyStart + (moneyStep), player.getMoney());
  }
  
  @Test
  void testOnDead()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Unit unit = new Unit()
    {
      @Override
      public void onDead(Game game, Fight fight, Player player, Player player2, Unit attacker)
      {
        super.onDead(game, null, player, player2, attacker);
        player.addMoney(moneyStep);
      }
    };
    
    Unit unit2 = new Unit()
    {
    };
    
    player.addToTable(unit, 0);
    unit.onDead(game, null, player, player2, unit2);
    
    assertEquals(moneyStart + (moneyStep), player.getMoney());
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
      int attack = health;
      
      @Override
      public int getAttack()
      {
        return attack;
      }
    };
    
    assertFalse(unit.isDead());
    unit.onAttacked(game, null, player, player2, unit2);
    assertTrue(unit.isDead());
  }
}
