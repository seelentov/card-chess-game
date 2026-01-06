package ru.vladislavkomkov.model.entity.unit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.GamePlayerTestCase;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.player.Player;

public class UnitTest extends GamePlayerTestCase
{
  @Test
  void testOnStartTurn()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Unit unit = new Unit(player)
    {
      @Override
      public void onStartTurn(Game game, Fight fight, Player player)
      {
        super.onStartTurn(game, null, player);
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
    
    Unit unit = new Unit(player)
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
    
    Unit unit = new Unit(player)
    {
      @Override
      public void onStartFight(Game game, Fight fight, Player player, Player player2)
      {
        super.onStartFight(game, null, player, player2);
        player.addMoney(moneyStep);
      }
    };
    
    player.addToTable(unit, 0);
    game.processStartFight(new Fight(game, player, player2), player, player2);
    
    assertEquals(moneyStart + (moneyStep), player.getMoney());
  }
  
  @Test
  void testOnEndFight()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Unit unit = new Unit(player)
    {
      @Override
      public void onEndFight(Game game, Fight fight, Player player, Player player2)
      {
        super.onEndFight(game, null, player, player2);
        player.addMoney(moneyStep);
      }
    };
    
    player.addToTable(unit, 0);
    game.processEndFight(new Fight(game, player, player2), player, player2);
    
    assertEquals(moneyStart + (moneyStep), player.getMoney());
  }
  
  @Test
  void testOnAttacked()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Unit unit = new Unit(player)
    {
      @Override
      public void onAttacked(Game game, Fight fight, Player player, Player player2, Unit attacker, boolean processDamage)
      {
        super.onAttacked(game, null, player, player2, attacker, processDamage);
        player.addMoney(moneyStep);
      }
    };
    
    Unit unit2 = new Unit(player)
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
    
    Unit unit = new Unit(player)
    {
      @Override
      public void onAttack(Game game, Fight fight, Player player, Player player2, Unit attacked)
      {
        super.onAttack(game, null, player, player2, attacked);
        player.addMoney(moneyStep);
      }
    };
    
    Unit unit2 = new Unit(player)
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
    
    Unit unit = new Unit(player)
    {
      @Override
      public void onDead(Game game, Fight fight, Player player, Player player2, Unit attacker)
      {
        super.onDead(game, null, player, player2, attacker);
        player.addMoney(moneyStep);
      }
    };
    
    Unit unit2 = new Unit(player)
    {
    };
    
    player.addToTable(unit, 0);
    unit.onDead(game, null, player, player2, unit2);
    
    assertEquals(moneyStart + (moneyStep), player.getMoney());
  }
  
  @Test
  void testIsDead()
  {
    Unit unit = new Unit(player)
    {
    };
    unit.setHealth(1);
    int health = unit.getHealth();
    
    Unit unit2 = new Unit(player)
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
