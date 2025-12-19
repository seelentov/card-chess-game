package ru.vladislavkomkov.model.fight;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.RandUtils;

public class Fight
{
  public static final int TURN_LIMIT = 10000;
  
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
  
  public void addToFightTable(Player player, Unit unit, Unit parent)
  {
    addToFightTable(player, unit, parent, false);
  }
  
  public void addToFightTable(Player player, Unit unit, Unit parent, boolean withoutOne)
  {
    List<Unit> table = getFightTable(player);
    
    if ((table.size() - (withoutOne ? 1 : 0)) == Player.TABLE_LIMIT)
    {
      return;
    }
    
    int indexParent = table.indexOf(parent);
    if ((indexParent + 1) > table.size())
    {
      table.add(unit);
    }
    else
    {
      table.add(indexParent + 1, unit);
    }
  }
  
  public List<Unit> getFightTable(Player player)
  {
    if (player == player1)
    {
      return player1Units;
    }
    else if (player == player2)
    {
      return player2Units;
    }
    else
    {
      throw new RuntimeException("Player not found in this fight");
    }
  }
  
  public Optional<FightInfo> doTurn()
  {
    if (turn >= TURN_LIMIT)
    {
      afterFight();
      return Optional.of(new FightInfo(player1, player2, FightInfo.Result.DRAW, 0));
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
  }
  
  Optional<FightInfo> handleFightEnd()
  {
    if (player1Units.isEmpty() && player2Units.isEmpty())
    {
      afterFight();
      return Optional.of(new FightInfo(player1, player2, FightInfo.Result.DRAW, 0));
    }
    
    boolean isPlayer1Win = player2Units.isEmpty();
    Player winner = isPlayer1Win ? player1 : player2;
    Player loser = isPlayer1Win ? player2 : player1;
    List<Unit> winnerUnits = isPlayer1Win ? player1Units : player2Units;
    
    int damage = calcPlayerDamage(winner, winnerUnits);
    loser.applyDamage(damage);
    
    afterFight();
    
    FightInfo.Result result = isPlayer1Win ? FightInfo.Result.PLAYER1_WIN : FightInfo.Result.PLAYER2_WIN;
    return Optional.of(new FightInfo(player1, player2, result, damage));
  }
  
  Optional<FightInfo> processTurn(boolean isPlayer1Turn)
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
    
    if (currentTurn >= units.size())
    {
      currentTurn = 0;
    }
    
    int startTurn = currentTurn;
    
    while (true)
    {
      if (currentTurn >= units.size())
      {
        currentTurn = 0;
      }
      Unit attacker = units.get(currentTurn);
      
      if (checkAttacker(attacker))
      {
        return Optional.of(attacker);
      }
      
      incTurn(isPlayer1Turn);
      turn += 2;
      
      int newTurn = isPlayer1Turn ? player1Turn : player2Turn;
      int normalizedNewTurn = newTurn % units.size();
      int normalizedStartTurn = startTurn % units.size();
      
      currentTurn = isPlayer1Turn ? player1Turn : player2Turn;
      
      if (normalizedNewTurn == normalizedStartTurn)
      {
        break;
      }
    }
    
    return Optional.empty();
  }
  
  void executeAttack(boolean isPlayer1Turn, Unit attacker, Unit attacked)
  {
    Player turnPlayer1 = isPlayer1Turn ? player1 : player2;
    Player turnPlayer2 = isPlayer1Turn ? player2 : player1;
    
    attacker.onAttack(game, this, turnPlayer1, turnPlayer2, attacked);
    attacked.onAttacked(game, this, turnPlayer2, turnPlayer1, attacker);
    
    handleUnitDeath(isPlayer1Turn, attacker, turnPlayer1, turnPlayer2, attacked);
    handleUnitDeath(!isPlayer1Turn, attacked, turnPlayer2, turnPlayer1, attacker);
  }
  
  void handleUnitDeath(boolean isPlayer1Unit, Unit unit, Player unitOwner, Player opponent, Unit otherUnit)
  {
    if (unit.isDead())
    {
      unit.onDead(game, this, unitOwner, opponent, otherUnit);
      
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
    List<Unit> unitsForTurn = isPlayer1Turn ? player1Units : player2Units;
    if (!unitsForTurn.isEmpty())
    {
      if (isPlayer1Turn)
      {
        player1Turn = (player1Turn + 1) % unitsForTurn.size();
      }
      else
      {
        player2Turn = (player2Turn + 1) % unitsForTurn.size();
      }
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
  }
  
  public int getTurn()
  {
    return turn;
  }
  
  public Player getPlayer1()
  {
    return player1;
  }
  
  public Player getPlayer2()
  {
    return player2;
  }
  
  public List<Unit> getPlayer1Units()
  {
    return player1Units;
  }
  
  public List<Unit> getPlayer2Units()
  {
    return player2Units;
  }
}