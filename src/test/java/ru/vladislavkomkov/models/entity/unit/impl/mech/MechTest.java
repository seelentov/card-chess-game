package ru.vladislavkomkov.models.entity.unit.impl.mech;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.GamePlayerTestCase;
import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.entity.unit.impl.mech.fourth.AccordoTron;
import ru.vladislavkomkov.models.player.Player;

public class MechTest extends GamePlayerTestCase
{
  @Test
  void testMagnetize()
  {
    Mech mech = new AccordoTron();
    Mech mech2 = new AccordoTron();
    Mech mech3 = new AccordoTron();
    mech.magnetize(mech2);
    mech.magnetize(mech3);
    assertEquals(new AccordoTron().getName(), mech.cloneMagnetized().get(0).getName());
    assertEquals(new AccordoTron().getName(), mech.cloneMagnetized().get(1).getName());
  }
  
  @Test
  void testMagnetizedOnStartTurn()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Mech mech = new Mech()
    {
      public void onStartTurn(Game game, Player player)
      {
        super.onStartTurn(game, player);
        player.addMoney(moneyStep);
      }
    };
    
    Mech mech2 = new Mech()
    {
      public void onStartTurn(Game game, Player player)
      {
        super.onStartTurn(game, player);
        player.addMoney(moneyStep);
      }
    };
    
    player.addToTable(mech, 0);
    mech.magnetize(mech2);
    
    game.processStartTurn(player);
    
    assertEquals(moneyStart + (moneyStep * 2), player.getMoney());
  }
  
  @Test
  void testMagnetizedOnEndTurn()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Mech mech = new Mech()
    {
      public void onEndTurn(Game game, Player player)
      {
        super.onEndTurn(game, player);
        player.addMoney(moneyStep);
      }
    };
    
    Mech mech2 = new Mech()
    {
      public void onEndTurn(Game game, Player player)
      {
        super.onEndTurn(game, player);
        player.addMoney(moneyStep);
      }
    };
    
    player.addToTable(mech, 0);
    mech.magnetize(mech2);
    
    game.processEndTurn(player);
    
    assertEquals(moneyStart + (moneyStep * 2), player.getMoney());
  }
  
  @Test
  void testMagnetizedOnStartFight()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Mech mech = new Mech()
    {
      public void onStartFight(Game game, Player player, Player player2)
      {
        super.onStartFight(game, player, player2);
        player.addMoney(moneyStep);
      }
    };
    
    Mech mech2 = new Mech()
    {
      public void onStartFight(Game game, Player player, Player player2)
      {
        super.onStartFight(game, player, player2);
        player.addMoney(moneyStep);
      }
    };
    
    player.addToTable(mech, 0);
    mech.magnetize(mech2);
    
    game.processStartFight(player, player2);
    
    assertEquals(moneyStart + (moneyStep * 2), player.getMoney());
  }
  
  @Test
  void testMagnetizedOnEndFight()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Mech mech = new Mech()
    {
      public void onEndFight(Game game, Player player, Player player2)
      {
        super.onEndFight(game, player, player2);
        player.addMoney(moneyStep);
      }
    };
    
    Mech mech2 = new Mech()
    {
      public void onEndFight(Game game, Player player, Player player2)
      {
        super.onEndFight(game, player, player2);
        player.addMoney(moneyStep);
      }
    };
    
    player.addToTable(mech, 0);
    mech.magnetize(mech2);
    
    game.processEndFight(player, player2);
    
    assertEquals(moneyStart + (moneyStep * 2), player.getMoney());
  }
  
  @Test
  void testMagnetizedOnAttacked()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Mech mech = new Mech()
    {
      public void onAttacked(Game game, Player player, Player player2, Unit attacker)
      {
        super.onAttacked(game, player, player2, attacker);
        player.addMoney(moneyStep);
      }
    };
    
    Mech mech2 = new Mech()
    {
      public void onAttacked(Game game, Player player, Player player2, Unit attacker)
      {
        super.onAttacked(game, player, player2, attacker);
        player.addMoney(moneyStep);
      }
    };
    
    Unit enemy = new Unit()
    {
    };
    
    player.addToTable(mech, 0);
    mech.magnetize(mech2);
    
    mech.onAttacked(game, player, player2, enemy);
    
    assertEquals(moneyStart + (moneyStep * 2), player.getMoney());
  }
  
  @Test
  void testMagnetizedOnAttack()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Mech mech = new Mech()
    {
      public void onAttack(Game game, Player player, Player player2, Unit attacked)
      {
        super.onAttack(game, player, player2, attacked);
        player.addMoney(moneyStep);
      }
    };
    
    Mech mech2 = new Mech()
    {
      public void onAttack(Game game, Player player, Player player2, Unit attacked)
      {
        super.onAttack(game, player, player2, attacked);
        player.addMoney(moneyStep);
      }
    };
    
    Unit enemy = new Unit()
    {
    };
    
    player.addToTable(mech, 0);
    mech.magnetize(mech2);
    
    mech.onAttack(game, player, player2, enemy);
    
    assertEquals(moneyStart + (moneyStep * 2), player.getMoney());
  }
  
  @Test
  void testMagnetizedOnDead()
  {
    int moneyStart = player.getMoney();
    int moneyStep = 10;
    
    Mech mech = new Mech()
    {
      public void onDead(Game game, Player player, Player player2, Unit attacker)
      {
        super.onDead(game, player, player2, attacker);
        player.addMoney(moneyStep);
      }
    };
    
    Mech mech2 = new Mech()
    {
      public void onDead(Game game, Player player, Player player2, Unit attacker)
      {
        super.onDead(game, player, player2, attacker);
        player.addMoney(moneyStep);
      }
    };
    Unit enemy = new Unit()
    {
    };
    
    player.addToTable(mech, 0);
    mech.magnetize(mech2);
    
    mech.onDead(game, player, player2, enemy);
    
    assertEquals(moneyStart + (moneyStep * 2), player.getMoney());
  }
}
