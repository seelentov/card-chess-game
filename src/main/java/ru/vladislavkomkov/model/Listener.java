package ru.vladislavkomkov.model;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.vladislavkomkov.model.action.GlobalAction;
import ru.vladislavkomkov.model.action.OnAppearAction;
import ru.vladislavkomkov.model.action.OnAttackAction;
import ru.vladislavkomkov.model.action.OnAttackedAction;
import ru.vladislavkomkov.model.action.OnDeadAction;
import ru.vladislavkomkov.model.action.OnDisappearAction;
import ru.vladislavkomkov.model.action.OnEndFightAction;
import ru.vladislavkomkov.model.action.OnEndTurnAction;
import ru.vladislavkomkov.model.action.OnHandledAction;
import ru.vladislavkomkov.model.action.OnIncTavernLevel;
import ru.vladislavkomkov.model.action.OnPlayedAction;
import ru.vladislavkomkov.model.action.OnResetTavernAction;
import ru.vladislavkomkov.model.action.OnSellAction;
import ru.vladislavkomkov.model.action.OnStartFightAction;
import ru.vladislavkomkov.model.action.OnStartTurnAction;
import ru.vladislavkomkov.model.action.OnSummonedAction;
import ru.vladislavkomkov.model.action.PrepareAction;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.ListenerUtils;
import ru.vladislavkomkov.util.UUIDUtils;

public class Listener implements Cloneable
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
  public Map<String, OnSummonedAction> onSummonedListeners = new HashMap<>();
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
      onSummonedListeners,
      onDisappearListeners);
  
  public Listener()
  {
  }
  
  public void push(Listener listener)
  {
    push(listener, false);
  }
  
  public void push(Listener listener, boolean convertCore)
  {
    for (int i = 0; i < listeners.size(); i++)
    {
      Map<String, ?> sourceMap = listener.listeners.get(i);
      Map<String, ?> targetMap = listeners.get(i);
      
      if (convertCore)
      {
        for (Map.Entry<String, ?> entry : sourceMap.entrySet())
        {
          String key = entry.getKey();
          Object value = entry.getValue();
          if (KEY_CORE.equals(key))
          {
            String newKey = UUIDUtils.generateKeyCore();
            ((Map<String, Object>) targetMap).put(newKey, value);
          }
          else
          {
            ((Map<String, Object>) targetMap).put(key, value);
          }
        }
      }
      else
      {
        ((Map<String, Object>) targetMap).putAll(sourceMap);
      }
    }
  }
  
  public void removeListener(Unit unit)
  {
    removeListener(UUIDUtils.generateKeyTemp(unit.getID()));
  }
  
  public void removeListener(String id)
  {
    listeners.forEach(listener -> listener.remove(id));
  }
  
  public void removeCoreListener()
  {
    listeners.forEach(listener -> listener.remove(KEY_CORE));
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
        listener.listeners.get(i).put(
            isDeduplication ? KEY_CORE : UUIDUtils.generateKeyCore(),
            l.get(KEY_CORE));
      }
    }
    
    return listener;
  }
  
  public void processOnPlayedListeners(
      Game game,
      Fight fight,
      Player player,
      Entity entity,
      List<Integer> input,
      boolean auto)
  {
    ListenerUtils.processActionListeners(
        onPlayedListeners,
        player,
        (action) -> action.process(game, fight, player, entity, input, auto));
  }
  
  public void processOnHandledListeners(Game game, Fight fight, Player player, Entity entity)
  {
    ListenerUtils.processActionListeners(
        onHandledListeners,
        player,
        (action) -> action.process(game, fight, player, entity));
  }
  
  public void processOnAttackListeners(
      Game game,
      Fight fight,
      Player player1,
      Player player2,
      Unit unit,
      Unit attacked)
  {
    ListenerUtils.processActionListeners(
        onAttackListeners,
        player1,
        (action) -> action.process(game, fight, player1, player2, unit, attacked));
  }
  
  public void processOnAttackedListeners(
      Game game,
      Fight fight,
      Player player1,
      Player player2,
      Unit unit,
      Unit attacker)
  {
    ListenerUtils.processActionListeners(
        onAttackedListeners,
        player1,
        (action) -> action.process(game, fight, player1, player2, unit, attacker));
  }
  
  public void processOnDeadListeners(
      Game game,
      Fight fight,
      Player player1,
      Player player2,
      Unit unit,
      Unit attacker)
  {
    ListenerUtils.processActionListeners(
        onDeadListeners,
        player1,
        (action) -> action.process(game, fight, player1, player2, unit, attacker));
  }
  
  public void processOnSellListeners(Game game, Fight fight, Player player, Unit entity)
  {
    ListenerUtils.processActionListeners(
        onSellListeners,
        player,
        (action) -> action.process(game, fight, player, entity));
  }
  
  public void processOnStartTurnListeners(Game game, Fight fight, Player player)
  {
    processGlobalListeners(onStartTurnListeners, game, fight, player);
  }
  
  public void processOnEndTurnListeners(Game game, Fight fight, Player player)
  {
    processGlobalListeners(onEndTurnListeners, game, fight, player);
  }
  
  public void processOnStartFightListeners(Game game, Fight fight, Player player, Player player2)
  {
    ListenerUtils.processActionListeners(
        onStartFightListeners,
        player,
        (action) -> action.process(game, fight, player, player2));
  }
  
  public void processOnEndFightListeners(Game game, Fight fight, Player player, Player player2)
  {
    ListenerUtils.processActionListeners(
        onEndFightListeners,
        player,
        (action) -> action.process(game, fight, player, player2));
  }
  
  public void processOnResetTavernListeners(Game game, Fight fight, Player player)
  {
    processGlobalListeners(onResetTavernListeners, game, fight, player);
  }
  
  public void processOnIncTavernLevelListener(Game game, Fight fight, Player player)
  {
    processGlobalListeners(onIncLevelListeners, game, fight, player);
  }
  
  public void processOnAppearListeners(Game game, Fight fight, Player player, Unit entity)
  {
    processPrepareListeners(onAppearListeners, game, fight, player, entity);
  }
  
  public void processOnSummonedListeners(Game game, Fight fight, Player player, Unit entity)
  {
    processPrepareListeners(onSummonedListeners, game, fight, player, entity);
  }
  
  public void processOnDisappearListeners(Game game, Fight fight, Player player, Unit entity)
  {
    processPrepareListeners(onDisappearListeners, game, fight, player, entity);
  }
  
  <T extends GlobalAction> void processGlobalListeners(
      Map<String, T> listeners,
      Game game,
      Fight fight,
      Player player)
  {
    ListenerUtils.processActionListeners(
        listeners,
        player,
        (action) -> action.process(game, fight, player));
  }
  
  <T extends PrepareAction> void processPrepareListeners(
      Map<String, T> listeners,
      Game game,
      Fight fight,
      Player player,
      Unit entity)
  {
    ListenerUtils.processActionListeners(
        listeners,
        player,
        (action) -> action.process(game, fight, player, entity));
  }
  
  @Override
  public Listener clone()
  {
    try
    {
      Listener clonedInstance = (Listener) super.clone();
      
      clonedInstance.onPlayedListeners = new HashMap<>(this.onPlayedListeners);
      clonedInstance.onHandledListeners = new HashMap<>(this.onHandledListeners);
      clonedInstance.onAttackListeners = new HashMap<>(this.onAttackListeners);
      clonedInstance.onAttackedListeners = new HashMap<>(this.onAttackedListeners);
      clonedInstance.onDeadListeners = new HashMap<>(this.onDeadListeners);
      clonedInstance.onSellListeners = new HashMap<>(this.onSellListeners);
      clonedInstance.onStartTurnListeners = new HashMap<>(this.onStartTurnListeners);
      clonedInstance.onEndTurnListeners = new HashMap<>(this.onEndTurnListeners);
      clonedInstance.onStartFightListeners = new HashMap<>(this.onStartFightListeners);
      clonedInstance.onEndFightListeners = new HashMap<>(this.onEndFightListeners);
      clonedInstance.onResetTavernListeners = new HashMap<>(this.onResetTavernListeners);
      clonedInstance.onIncLevelListeners = new HashMap<>(this.onIncLevelListeners);
      clonedInstance.onAppearListeners = new HashMap<>(this.onAppearListeners);
      clonedInstance.onDisappearListeners = new HashMap<>(this.onDisappearListeners);
      
      clonedInstance.listeners = List.of(
          clonedInstance.onPlayedListeners,
          clonedInstance.onHandledListeners,
          clonedInstance.onAttackListeners,
          clonedInstance.onAttackedListeners,
          clonedInstance.onDeadListeners,
          clonedInstance.onSellListeners,
          clonedInstance.onStartTurnListeners,
          clonedInstance.onEndTurnListeners,
          clonedInstance.onStartFightListeners,
          clonedInstance.onEndFightListeners,
          clonedInstance.onResetTavernListeners,
          clonedInstance.onIncLevelListeners,
          clonedInstance.onAppearListeners,
          clonedInstance.onDisappearListeners);
      
      return clonedInstance;
    }
    catch (CloneNotSupportedException e)
    {
      throw new AssertionError("Clone not supported", e);
    }
  }
}