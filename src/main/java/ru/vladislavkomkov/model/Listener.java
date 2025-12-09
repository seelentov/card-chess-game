package ru.vladislavkomkov.model;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.vladislavkomkov.model.action.*;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.ListenerUtils;
import ru.vladislavkomkov.util.UUIDUtils;

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
  
  public Map<String, OnAppearAction> onAppearListeners = new HashMap<>();
  public Map<String, OnDisappearAction> onDisappearListeners = new HashMap<>();
  
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
      onIncLevelListeners,
      onAppearListeners,
      onDisappearListeners);
  
  public Listener()
  {
  }
  
  public void removeCoreListener()
  {
    listeners.forEach(listener -> {
      listener.remove(KEY_CORE);
    });
  }
  
  public Listener newCoreListener()
  {
    return newCoreListener(true);
  }
  
  public Listener newCoreListener(boolean isDeduplication)
  {
    Listener listener = new Listener();
    
    for (int i = 0; i < listeners.size(); i++)
    {
      Map l = listeners.get(i);
      if (l.containsKey(KEY_CORE))
      {
        listener.listeners.get(i).put(isDeduplication ? KEY_CORE : UUIDUtils.generateKeyCore(), l.get(KEY_CORE));
      }
    }
    
    return listener;
  }
  
  public void push(Listener listener)
  {
    push(listener, false);
  }
  
  public void push(Listener listener, boolean isDeduplication)
  {
    for (int i = 0; i < listeners.size(); i++)
    {
      listeners.get(i).putAll(listener.listeners.get(i));
    }
  }
  
  public void removeListener(Unit unit)
  {
    removeListener(UUIDUtils.generateKeyTemp(unit.getID()));
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
      boolean isTavernIndex2,
      boolean auto)
  {
    ListenerUtils.processActionListeners(
        onPlayedListeners,
        player,
        (action) -> action.process(game, player, entity, index, isTavernIndex, index2, isTavernIndex2, auto));
  }
  
  public void processOnHandledListeners(
      Game game,
      Player player,
      Entity entity)
  {
    ListenerUtils.processActionListeners(
        onHandledListeners,
        player,
        (action) -> action.process(game, player, entity));
  }
  
  public void processOnAttackListeners(
      Game game,
      Player player1,
      Player player2,
      Unit unit,
      Unit attacked)
  {
    ListenerUtils.processActionListeners(
        onAttackListeners,
        player1,
        (action) -> action.process(game, player1, player2, unit, attacked));
  }
  
  public void processOnAttackedListeners(
      Game game,
      Player player1,
      Player player2,
      Unit unit,
      Unit attacker)
  {
    ListenerUtils.processActionListeners(
        onAttackedListeners,
        player1,
        (action) -> action.process(game, player1, player2, unit, attacker));
  }
  
  public void processOnDeadListeners(
      Game game,
      Player player1,
      Player player2,
      Unit unit,
      Unit attacker)
  {
    ListenerUtils.processActionListeners(
        onDeadListeners,
        player1,
        (action) -> action.process(game, player1, player2, unit, attacker));
  }
  
  public void processOnSellListeners(
      Game game,
      Player player,
      Entity entity)
  {
    ListenerUtils.processActionListeners(
        onSellListeners,
        player,
        (action) -> action.process(game, player, entity));
  }
  
  public void processOnAppearListeners(
      Game game,
      Player player,
      Entity entity)
  {
    processPrepareListeners(onAppearListeners, game, player, entity);
  }
  
  public void processOnDisappearListeners(
      Game game,
      Player player,
      Entity entity)
  {
    processPrepareListeners(onDisappearListeners, game, player, entity);
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
    ListenerUtils.processActionListeners(
        listeners,
        player,
        (action) -> action.process(game, player));
  }
  
  private <T extends PrepareAction> void processPrepareListeners(Map<String, T> listeners, Game game, Player player, Entity entity)
  {
    ListenerUtils.processActionListeners(
        listeners,
        player,
        (action) -> action.process(game, player, entity));
  }
}
