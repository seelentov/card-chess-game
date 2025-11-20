package ru.vladislavkomkov.models;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.vladislavkomkov.models.actions.*;
import ru.vladislavkomkov.models.entity.Entity;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.util.ListenerUtils;

public class Listener implements Serializable
{
  public Map<String, OnPlayedAction> onPlayedListeners = new HashMap<>();
  public Map<String, OnHandledAction> onHandledListeners = new HashMap<>();
  public Map<String, OnAttackAction> onAttackListeners = new HashMap<>();
  public Map<String, OnAttackedAction> onAttackedListeners = new HashMap<>();
  public Map<String, OnDeadAction> onDeadListeners = new HashMap<>();
  public Map<String, OnSellAction> onSellListeners = new HashMap<>();
  public Map<String, OnStartTurnAction> onStartTurnListeners = new HashMap<>();
  public Map<String, OnEndTurnAction> onEndTurnListeners = new HashMap<>();
  public Map<String, OnStartFightAction> onStartFightListeners = new HashMap<>();
  public Map<String, OnEndFightAction> onEndFightListeners = new HashMap<>();
  public Map<String, OnResetTavernAction> onResetTavernListeners = new HashMap<>();
  public Map<String, OnIncTavernLevel> onIncLevelListeners = new HashMap<>();
  public List<Map> listeners = List.of(
      onPlayedListeners,
      onHandledListeners,
      onAttackListeners,
      onAttackedListeners,
      onDeadListeners,
      onSellListeners,
      onStartTurnListeners,
      onEndTurnListeners,
      onStartFightListeners,
      onEndFightListeners,
      onResetTavernListeners,
      onIncLevelListeners);
  
  public Listener()
  {
  }
  
  public void removeCoreListener()
  {
    listeners.forEach(listener -> {
      listener.remove(KEY_CORE);
    });
  }
  
  public void removeListener(Unit unit)
  {
    removeListener(ListenerUtils.generateKeyTemp(unit.getID()));
  }
  
  public void removeListener(String i)
  {
    listeners.forEach(listener -> listener.remove(i));
  }
  
  public void processOnPlayedListeners(
      Game game,
      Player player,
      Entity entity,
      int index,
      boolean isTavernIndex,
      int index2,
      boolean isTavernIndex2)
  {
    onPlayedListeners.forEach((s, action) -> action.process(game, player, entity, index, isTavernIndex, index2, isTavernIndex2));
  }
  
  public void processOnHandledListeners(
      Game game,
      Player player,
      Entity entity)
  {
    onHandledListeners.forEach((s, action) -> action.process(game, player, entity));
  }
  
  public void processOnAttackListeners(
      Game game,
      Player player1,
      Player player2,
      Unit unit,
      Unit attacked)
  {
    onAttackListeners.forEach((s, action) -> action.process(game, player1, player2, unit, attacked));
  }
  
  public void processOnAttackedListeners(
      Game game,
      Player player1,
      Player player2,
      Unit unit,
      Unit attacker)
  {
    onAttackedListeners.forEach((s, action) -> action.process(game, player1, player2, unit, attacker));
  }
  
  public void processOnDeadListeners(
      Game game,
      Player player1,
      Player player2,
      Unit unit,
      Unit attacker)
  {
    onDeadListeners.forEach((s, action) -> action.process(game, player1, player2, unit, attacker));
  }
  
  public void processOnSellListeners(
      Game game,
      Player player1,
      Entity entity)
  {
    onSellListeners.forEach((s, action) -> action.process(game, player1, entity));
  }
  
  public void processOnStartTurnListeners(
      Game game,
      Player player)
  {
    processGlobalListeners(onStartTurnListeners, game, player);
  }
  
  public void processOnEndTurnListeners(
      Game game,
      Player player)
  {
    processGlobalListeners(onEndTurnListeners, game, player);
  }
  
  public void processOnStartFightListeners(
      Game game,
      Player player)
  {
    processGlobalListeners(onStartFightListeners, game, player);
  }
  
  public void processOnEndFightListeners(
      Game game,
      Player player)
  {
    processGlobalListeners(onEndFightListeners, game, player);
  }
  
  public void processOnResetTavernListeners(
      Game game,
      Player player)
  {
    processGlobalListeners(onResetTavernListeners, game, player);
  }
  
  public void processOnIncTavernLevelListener(
      Game game,
      Player player)
  {
    processGlobalListeners(onIncLevelListeners, game, player);
  }
  
  private <T extends GlobalAction> void processGlobalListeners(Map<String, T> listeners, Game game, Player player)
  {
    listeners.forEach((s, t) -> t.process(game, player));
  }
}
