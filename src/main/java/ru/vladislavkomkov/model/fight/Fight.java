package ru.vladislavkomkov.model.fight;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import ru.vladislavkomkov.model.ActionEvent;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.RandUtils;

public class Fight
{
  public static final int TURN_LIMIT = 10000;
  final Game game;
  final FightPlayer fightPlayer1;
  final FightPlayer fightPlayer2;
  int damageCap = Integer.MAX_VALUE;
  int turn = 0;
  
  List<ActionEvent> history = new ArrayList<>();
  
  public Fight(Game game, Player player1, Player player2)
  {
    this(game, player1, player2, true, Integer.MAX_VALUE);
  }
  
  public Fight(Game game, Player player1, Player player2, int damageCap)
  {
    this(game, player1, player2, true, damageCap);
  }
  
  public Fight(Game game, Player player1, Player player2, boolean isSetup)
  {
    this(game, player1, player2, isSetup, Integer.MAX_VALUE);
  }
  
  public Fight(Game game, Player player1, Player player2, boolean isSetup, int damageCap)
  {
    this.game = game;
    this.fightPlayer1 = new FightPlayer(player1, new ArrayList<>());
    this.fightPlayer2 = new FightPlayer(player2, new ArrayList<>());
    this.damageCap = damageCap;
    
    if (isSetup)
    {
      setup();
    }
  }
  
  public void addToHistory(ActionEvent.Type event, Player player, List<Object> data)
  {
    List<Unit> playerUnits = fightPlayer1.player == player ? fightPlayer1.units : fightPlayer2.units;
    List<Unit> enemyUnits = fightPlayer1.player == player ? fightPlayer2.units : fightPlayer1.units;
    
    history.add(new ActionEvent(
        event,
        playerUnits.stream()
            .filter(Objects::nonNull)
            .map(Unit::clone)
            .collect(Collectors.toCollection(ArrayList::new)),
        enemyUnits.stream()
            .filter(Objects::nonNull)
            .map(Unit::clone)
            .collect(Collectors.toCollection(ArrayList::new)),
        data,
        player));
  }
  
  public void addToFightTable(Player player, Unit unit, Unit parent, boolean withoutOne)
  {
    int index = -1;
    List<Unit> table = getFightTable(player);
    int parentIndex = table.indexOf(parent);
    if (parentIndex != -1)
    {
      index = parentIndex + 1;
    }
    addToFightTableInternal(player, unit, index, withoutOne);
  }
  
  public void addToFightTable(Player player, Unit unit, int index, boolean withoutOne)
  {
    addToFightTableInternal(player, unit, index, withoutOne);
  }
  
  private void addToFightTableInternal(Player player, Unit unit, int index, boolean withoutOne)
  {
    List<Unit> table = getFightTable(player);
    
    if ((table.size() - (withoutOne ? 1 : 0)) == Player.TABLE_LIMIT)
    {
      return;
    }
    
    if (index >= table.size() || index == -1)
    {
      table.add(unit);
    }
    else
    {
      table.add(index, unit);
    }
    
    unit.onAppear(game, this, player);
    unit.onSummoned(game, this, player);
  }
  
  public void removeFromFightTable(Player player, Unit unit)
  {
    getFightTable(player).removeIf(unit1 -> unit1 == unit);
    unit.onDisappear(game, this, player);
  }
  
  public void removeFromFightTable(Player player, int index)
  {
    Unit unit = getFightTable(player).get(index);
    removeFromFightTable(player, unit);
  }
  
  public Optional<Unit> getFightUnit(Player player, String ID)
  {
    return getFightTable(player).stream().filter(unit -> unit.getID().equals(ID)).findFirst();
  }
  
  public List<Unit> getFightTable(Player player)
  {
    if (player == fightPlayer1.player)
    {
      return fightPlayer1.units;
    }
    else if (player == fightPlayer2.player)
    {
      return fightPlayer2.units;
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
      return Optional.of(new FightInfo(fightPlayer1.player, fightPlayer2.player, FightInfo.Result.DRAW, 0, history));
    }
    
    if (fightPlayer1.units.isEmpty() || fightPlayer2.units.isEmpty())
    {
      return handleFightEnd();
    }
    
    boolean isPlayer1Turn = turn % 2 == 0;
    return processTurn(isPlayer1Turn);
  }
  
  public void setup()
  {
    fightPlayer1.units.clear();
    fightPlayer2.units.clear();
    
    addToHistory(ActionEvent.Type.START_FIGHT, fightPlayer1.player, null);
    
    for (Unit item : fightPlayer1.player.cloneTable())
    {
      if (item != null)
      {
        fightPlayer1.units.add(item);
        item.onAppear(game, this, fightPlayer1.getPlayer());
      }
    }
    
    for (Unit item : fightPlayer2.player.cloneTable())
    {
      if (item != null)
      {
        fightPlayer2.units.add(item);
        item.onAppear(game, this, fightPlayer2.getPlayer());
      }
    }
    
    int player1Attack = calcAttack(fightPlayer1.units);
    int player2Attack = calcAttack(fightPlayer2.units);
    
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
    if (fightPlayer1.units.isEmpty() && fightPlayer2.units.isEmpty())
    {
      afterFight();
      return Optional.of(new FightInfo(fightPlayer1.player, fightPlayer2.player, FightInfo.Result.DRAW, 0, history));
    }
    
    boolean isPlayer1Win = fightPlayer2.units.isEmpty();
    Player winner = isPlayer1Win ? fightPlayer1.player : fightPlayer2.player;
    Player loser = isPlayer1Win ? fightPlayer2.player : fightPlayer1.player;
    List<Unit> winnerUnits = isPlayer1Win ? fightPlayer1.units : fightPlayer2.units;
    
    int damage = calcPlayerDamage(winner, winnerUnits);
    loser.applyDamage(damage);
    
    afterFight();
    
    FightInfo.Result result = isPlayer1Win ? FightInfo.Result.PLAYER1_WIN : FightInfo.Result.PLAYER2_WIN;
    return Optional.of(new FightInfo(fightPlayer1.player, fightPlayer2.player, result, damage, history));
  }
  
  Optional<FightInfo> processTurn(boolean isPlayer1Turn)
  {
    Optional<Unit> attackerOpt = findAttacker(isPlayer1Turn);
    Optional<Unit> attackedOpt = getRandAttackedUnit(isPlayer1Turn);
    
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
    FightPlayer currentFightPlayer = isPlayer1Turn ? fightPlayer1 : fightPlayer2;
    int currentTurn = isPlayer1Turn ? fightPlayer1.turn : fightPlayer2.turn;
    
    if (currentTurn >= currentFightPlayer.units.size())
    {
      currentTurn = 0;
      if (isPlayer1Turn)
      {
        fightPlayer1.turn = 0;
      }
      else
      {
        fightPlayer2.turn = 0;
      }
    }
    
    int startTurn = currentTurn;
    
    while (true)
    {
      if (currentTurn >= currentFightPlayer.units.size())
      {
        currentTurn = 0;
        if (isPlayer1Turn)
        {
          fightPlayer1.turn = 0;
        }
        else
        {
          fightPlayer2.turn = 0;
        }
      }
      Unit attacker = currentFightPlayer.units.get(currentTurn);
      
      if (checkAttacker(attacker))
      {
        return Optional.of(attacker);
      }
      
      incTurn(isPlayer1Turn);
      turn += 2;
      
      int newTurn = isPlayer1Turn ? fightPlayer1.turn : fightPlayer2.turn;
      int normalizedNewTurn = newTurn % currentFightPlayer.units.size();
      int normalizedStartTurn = startTurn % currentFightPlayer.units.size();
      
      currentTurn = isPlayer1Turn ? fightPlayer1.turn : fightPlayer2.turn;
      
      if (normalizedNewTurn == normalizedStartTurn)
      {
        break;
      }
    }
    
    return Optional.empty();
  }
  
  void executeAttack(boolean isPlayer1Turn, Unit attacker, Unit attacked)
  {
    Player turnPlayer1 = isPlayer1Turn ? fightPlayer1.player : fightPlayer2.player;
    Player turnPlayer2 = isPlayer1Turn ? fightPlayer2.player : fightPlayer1.player;
    
    int attackedAttackerIndex = (isPlayer1Turn ? fightPlayer2.turn : fightPlayer1.turn) % getFightTable(turnPlayer2).size();
    Unit attackedAttacker = getFightTable(turnPlayer2).get(attackedAttackerIndex);
    
    int attacks = attacker.getAttacksCount().toInt();
    
    for (int i = 0; i < attacks; i++)
    {
      attacker.onAttack(game, this, turnPlayer1, turnPlayer2, attacked);
      attacked.onAttacked(game, this, turnPlayer2, turnPlayer1, attacker);
      
      if (attacker.isDead())
      {
        break;
      }
    }
    
    int attackerIndex = getFightTable(turnPlayer1).indexOf(attacker);
    
    if (attackerIndex != -1)
    {
      if (isPlayer1Turn)
      {
        fightPlayer1.turn = attackerIndex;
      }
      else
      {
        fightPlayer2.turn = attackerIndex;
      }
    }
    else
    {
      if (isPlayer1Turn)
      {
        fightPlayer1.turn -= 1;
      }
      else
      {
        fightPlayer2.turn -= 1;
      }
    }
    
    int attackedAttackerIndexAfter = getFightTable(turnPlayer2).indexOf(attackedAttacker);
    
    if (attackedAttackerIndexAfter != -1)
    {
      if (!isPlayer1Turn)
      {
        fightPlayer1.turn = attackedAttackerIndexAfter;
      }
      else
      {
        fightPlayer2.turn = attackedAttackerIndexAfter;
      }
    }
  }
  
  int calcAttack(List<Unit> units)
  {
    return units.stream()
        .mapToInt(Unit::getAttack)
        .sum();
  }
  
  public Optional<Unit> getRandAttackedUnit(Player attacker)
  {
    return getRandAttackedUnit(fightPlayer1.player == attacker);
  }
  
  Optional<Unit> getRandAttackedUnit(boolean isPlayer1Turn)
  {
    return getRandAttackedUnit(isPlayer1Turn ? fightPlayer2.units : fightPlayer1.units);
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
    FightPlayer fightPlayer = isPlayer1Turn ? fightPlayer1 : fightPlayer2;
    if (!fightPlayer.units.isEmpty())
    {
      if (isPlayer1Turn)
      {
        fightPlayer1.turn = (fightPlayer1.turn + 1) % fightPlayer1.units.size();
      }
      else
      {
        fightPlayer2.turn = (fightPlayer2.turn + 1) % fightPlayer2.units.size();
      }
    }
  }
  
  int calcPlayerDamage(Player player, List<Unit> units)
  {
    int unitsDamage = units.stream()
        .mapToInt(Unit::getLevel)
        .sum();
    int playerDamage = player.getLevel();
    return Math.min(damageCap, playerDamage + unitsDamage);
  }
  
  void afterFight()
  {
    fightPlayer1.units.forEach(unit -> unit.onDisappear(game, this, fightPlayer1.player));
    fightPlayer2.units.forEach(unit -> unit.onDisappear(game, this, fightPlayer2.player));
  }
  
  public int getTurn()
  {
    return turn;
  }
  
  public FightPlayer getFightPlayer(Player player)
  {
    if (fightPlayer1.getPlayer() == player)
    {
      return fightPlayer1;
    }
    
    if (fightPlayer2.getPlayer() == player)
    {
      return fightPlayer2;
    }
    
    throw new RuntimeException("Player not found");
  }
  
  public Player getPlayer1()
  {
    return fightPlayer1.player;
  }
  
  public Player getPlayer2()
  {
    return fightPlayer2.player;
  }
  
  public List<Unit> getPlayer1Units()
  {
    return fightPlayer1.units;
  }
  
  public List<Unit> getPlayer2Units()
  {
    return fightPlayer2.units;
  }
}