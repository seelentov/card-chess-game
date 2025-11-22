package ru.vladislavkomkov.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.RandUtils;

public class Fight implements Serializable
{
  static final int TURN_LIMIT = 10000;
  final Game game;
  final Player player1;
  final Player player2;
  List<Unit> player1Units;
  List<Unit> player2Units;
  
  int player1Turn = 0;
  int player2Turn = 0;
  
  int turn;
  
  public Fight(Game game, Player player1, Player player2)
  {
    this.player1 = player1;
    this.player2 = player2;
    
    this.game = game;
    
    setup();
  }
  
  public boolean doTurn()
  {
    if (turn >= TURN_LIMIT)
    {
      clearFightUnitList();
      return true;
    }
    
    if (player1Units.isEmpty() || player2Units.isEmpty())
    {
      if (player1Units.isEmpty() && player2Units.isEmpty())
      {
        clearFightUnitList();
        return true;
      }
      
      boolean isPlayer1Win = player2Units.isEmpty();
      Player loser = isPlayer1Win ? player2 : player1;
      
      int dmg;
      
      if (isPlayer1Win)
        dmg = calcPlayerDamage(player1, player1Units);
      else
        dmg = calcPlayerDamage(player2, player2Units);
      
      loser.applyDamage(dmg);
      
      clearFightUnitList();
      return true;
    }
    
    boolean isPlayer1Turn = turn % 2 == 0;
    
    Optional<Unit> attackerOpt = Optional.empty();
    
    int startTurn = isPlayer1Turn ? player1Turn : player2Turn;
    
    while (true)
    {
      Unit attacker = isPlayer1Turn ? player1Units.get(player1Turn) : player2Units.get(player2Turn);
      
      if (checkAttacker(attacker))
      {
        attackerOpt = Optional.of(attacker);
        break;
      }
      
      incTurn(isPlayer1Turn);
      turn += 2;
      
      if ((isPlayer1Turn ? player1Turn : player2Turn) == startTurn)
      {
        break;
      }
    }
    
    Optional<Unit> attackedOpt = isPlayer1Turn ? getRandAttackedUnit(player2Units) : getRandAttackedUnit(player1Units);
    
    if (attackedOpt.isPresent() && attackerOpt.isPresent())
    {
      Unit attacker = attackerOpt.get();
      Unit attacked = attackedOpt.get();
      
      Player turnPlayer1 = isPlayer1Turn ? player1 : player2;
      Player turnPlayer2 = isPlayer1Turn ? player2 : player1;
      
      attacker.onAttack(game, turnPlayer1, turnPlayer2, attacked);
      
      attacked.onAttacked(game, turnPlayer2, turnPlayer1, attacker);
      
      if (attacker.isDead())
      {
        attacker.onDead(game, turnPlayer1, turnPlayer2, attacked);
        if (attacker.isDead())
        {
          if (isPlayer1Turn)
            player1Units.removeIf(o -> o == attacker);
          else
            player2Units.removeIf(o -> o == attacker);
        }
      }
      
      if (attacked.isDead())
      {
        attacked.onDead(game, turnPlayer2, turnPlayer1, attacker);
        if (attacked.isDead())
        {
          if (isPlayer1Turn)
            player2Units.removeIf(o -> o == attacked);
          else
            player1Units.removeIf(o -> o == attacked);
        }
      }
    }
    
    incTurn(isPlayer1Turn);
    
    turn++;
    return false;
  }
  
  void setup()
  {
    this.player1Units = new ArrayList<>();
    this.player2Units = new ArrayList<>();
    
    for (Unit item : player1.cloneTable())
    {
      if (item != null)
      {
        this.player1Units.add(item);
      }
    }
    
    for (Unit item : player2.cloneTable())
    {
      if (item != null)
      {
        this.player2Units.add(item);
      }
    }
    
    int player1Attack = calcAttack(this.player1Units);
    int player2Attack = calcAttack(this.player2Units);
    
    turn = player1Attack > player2Attack ? 0 : player1Attack < player2Attack ? 1 : RandUtils.getRand(1);
    
    player1.inFightTable = player1Units;
    player2.inFightTable = player2Units;
  }
  
  int calcAttack(List<Unit> units)
  {
    return units.stream().reduce(0, (total, unit) -> total + unit.getAttack(), Integer::sum);
  }
  
  Optional<Unit> getRandAttackedUnit(List<Unit> units)
  {
    units = units.stream().filter(this::filterAttacked).toList();
    units = units.stream().noneMatch(Unit::getIsTaunt)
        ? units
        : units.stream().filter(Unit::getIsTaunt).toList();
    
    if (units.isEmpty())
    {
      return Optional.empty();
    }
    
    int i = RandUtils.getRand(units.size() - 1);
    return Optional.of(units.get(i));
  }
  
  boolean filterAttacked(Unit unit)
  {
    boolean isNotDisguise = !unit.getIsDisguise();
    return isNotDisguise;
  }
  
  boolean checkAttacker(Unit unit)
  {
    boolean attackMoreThanZero = unit.getAttack() > 0;
    return attackMoreThanZero;
  }
  
  void incTurn(boolean isPlayer1Turn)
  {
    if (isPlayer1Turn)
      player1Turn++;
    if (player1Turn >= player1Units.size())
      player1Turn = 0;
    
    else
      player2Turn++;
    if (player2Turn >= player2Units.size())
      player2Turn = 0;
  }
  
  int calcPlayerDamage(Player player, List<Unit> units)
  {
    int unitsDmg = units.stream().reduce(0, (total, unit) -> total + unit.getLevel(), Integer::sum);
    int playerDmg = player.getLevel();
    return playerDmg + unitsDmg;
  }
  
  public Player getPlayer1()
  {
    return player1;
  }
  
  public Player getPlayer2()
  {
    return player2;
  }
  
  void clearFightUnitList()
  {
    player1.inFightTable = null;
    player2.inFightTable = null;
  }
}
