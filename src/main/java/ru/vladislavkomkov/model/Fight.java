package ru.vladislavkomkov.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.RandUtils;

public class Fight
{
  static final int TURN_LIMIT = 10000;
  
  final Game game;
  final Player player1;
  final Player player2;
  
  List<Unit> player1Units;
  List<Unit> player2Units;
  
  int player1Turn = 0;
  int player2Turn = 0;
  int turn = 0;
  
  public Fight(Game game, Player player1, Player player2)
  {
    this.game = game;
    this.player1 = player1;
    this.player2 = player2;
    
    setup();
  }
  
  public Optional<Info> doTurn()
  {
    if (turn >= TURN_LIMIT)
    {
      afterFight();
      return Optional.of(new Info(player1, player2, Info.Result.DRAW, 0));
    }
    
    if (player1Units.isEmpty() || player2Units.isEmpty())
    {
      return handleFightEnd();
    }
    
    boolean isPlayer1Turn = turn % 2 == 0;
    return processTurn(isPlayer1Turn);
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
    
    if (player1Attack > player2Attack)
    {
      turn = 0;
    }
    else if (player1Attack < player2Attack)
    {
      turn = 1;
    }
    else
    {
      turn = RandUtils.getRand(1);
    }
    
    player1.inFightTable = player1Units;
    player2.inFightTable = player2Units;
  }
  
  Optional<Info> handleFightEnd()
  {
    if (player1Units.isEmpty() && player2Units.isEmpty())
    {
      afterFight();
      return Optional.of(new Info(player1, player2, Info.Result.DRAW, 0));
    }
    
    boolean isPlayer1Win = player2Units.isEmpty();
    Player winner = isPlayer1Win ? player1 : player2;
    Player loser = isPlayer1Win ? player2 : player1;
    List<Unit> winnerUnits = isPlayer1Win ? player1Units : player2Units;
    
    int damage = calcPlayerDamage(winner, winnerUnits);
    loser.applyDamage(damage);
    
    afterFight();
    
    Info.Result result = isPlayer1Win ? Info.Result.PLAYER1_WIN : Info.Result.PLAYER2_WIN;
    return Optional.of(new Info(player1, player2, result, damage));
  }
  
  Optional<Info> processTurn(boolean isPlayer1Turn)
  {
    Optional<Unit> attackerOpt = findAttacker(isPlayer1Turn);
    Optional<Unit> attackedOpt = getRandAttackedUnit(isPlayer1Turn ? player2Units : player1Units);
    
    if (attackerOpt.isPresent() && attackedOpt.isPresent())
    {
      executeAttack(isPlayer1Turn, attackerOpt.get(), attackedOpt.get());
    }
    
    incTurn(isPlayer1Turn);
    turn++;
    
    return Optional.empty();
  }
  
  Optional<Unit> findAttacker(boolean isPlayer1Turn)
  {
    List<Unit> units = isPlayer1Turn ? player1Units : player2Units;
    int currentTurn = isPlayer1Turn ? player1Turn : player2Turn;
    int startTurn = currentTurn;
    
    while (true)
    {
      Unit attacker = units.get(currentTurn);
      
      if (checkAttacker(attacker))
      {
        return Optional.of(attacker);
      }
      
      incTurn(isPlayer1Turn);
      turn += 2;
      
      int newTurn = isPlayer1Turn ? player1Turn : player2Turn;
      if (newTurn == startTurn)
      {
        break;
      }
      currentTurn = newTurn;
    }
    
    return Optional.empty();
  }
  
  void executeAttack(boolean isPlayer1Turn, Unit attacker, Unit attacked)
  {
    Player turnPlayer1 = isPlayer1Turn ? player1 : player2;
    Player turnPlayer2 = isPlayer1Turn ? player2 : player1;
    
    attacker.onAttack(game, turnPlayer1, turnPlayer2, attacked);
    attacked.onAttacked(game, turnPlayer2, turnPlayer1, attacker);
    
    handleUnitDeath(isPlayer1Turn, attacker, turnPlayer1, turnPlayer2, attacked);
    handleUnitDeath(!isPlayer1Turn, attacked, turnPlayer2, turnPlayer1, attacker);
  }
  
  void handleUnitDeath(boolean isPlayer1Unit, Unit unit, Player unitOwner, Player opponent, Unit otherUnit)
  {
    if (unit.isDead())
    {
      unit.onDead(game, unitOwner, opponent, otherUnit);
      
      if (unit.isDead())
      {
        if (isPlayer1Unit)
        {
          player1Units.removeIf(o -> o == unit);
        }
        else
        {
          player2Units.removeIf(o -> o == unit);
        }
      }
    }
  }
  
  int calcAttack(List<Unit> units)
  {
    return units.stream()
        .mapToInt(Unit::getAttack)
        .sum();
  }
  
  Optional<Unit> getRandAttackedUnit(List<Unit> units)
  {
    List<Unit> eligibleUnits = units.stream()
        .filter(this::filterAttacked)
        .toList();
    
    if (eligibleUnits.isEmpty())
    {
      return Optional.empty();
    }
    
    List<Unit> tauntUnits = eligibleUnits.stream()
        .filter(Unit::isTaunt)
        .toList();
    
    List<Unit> targetUnits = tauntUnits.isEmpty() ? eligibleUnits : tauntUnits;
    
    if (targetUnits.isEmpty())
    {
      return Optional.empty();
    }
    
    int index = RandUtils.getRand(targetUnits.size() - 1);
    return Optional.of(targetUnits.get(index));
  }
  
  boolean filterAttacked(Unit unit)
  {
    return !unit.isDisguise();
  }
  
  boolean checkAttacker(Unit unit)
  {
    return unit.getAttack() > 0;
  }
  
  void incTurn(boolean isPlayer1Turn)
  {
    if (isPlayer1Turn)
    {
      player1Turn = (player1Turn + 1) % player1Units.size();
    }
    else
    {
      player2Turn = (player2Turn + 1) % player2Units.size();
    }
  }
  
  int calcPlayerDamage(Player player, List<Unit> units)
  {
    int unitsDamage = units.stream()
        .mapToInt(Unit::getLevel)
        .sum();
    int playerDamage = player.getLevel();
    return playerDamage + unitsDamage;
  }
  
  void afterFight()
  {
    player1.inFightTable = null;
    player2.inFightTable = null;
  }
  
  public Player getPlayer1()
  {
    return player1;
  }
  
  public Player getPlayer2()
  {
    return player2;
  }
  
  public static class Info
  {
    public final Player player1;
    public final Player player2;
    public final Result result;
    public final int damage;
    
    public Info(Player player1, Player player2, Result result, int damage)
    {
      this.player1 = player1;
      this.player2 = player2;
      this.result = result;
      this.damage = damage;
    }
    
    public enum Result
    {
      PLAYER1_WIN,
      PLAYER2_WIN,
      DRAW
    }
  }
}