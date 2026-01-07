package ru.vladislavkomkov.model.entity;

import java.util.*;
import java.util.function.Consumer;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.vladislavkomkov.consts.Listeners;
import ru.vladislavkomkov.model.ActionEvent;
import ru.vladislavkomkov.model.GObject;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.Listener;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.ListenerUtils;
import ru.vladislavkomkov.util.RandUtils;
import ru.vladislavkomkov.util.ReflectUtils;
import ru.vladislavkomkov.util.UUIDUtils;

public abstract class Entity extends GObject
{
  public final static String F_IS_SPELL = "is_spell";
  
  public final static String F_LVL = "lvl";
  
  public final static String F_PLAY_TYPE = "play_type";
  
  protected Listener listener = new Listener();
  
  protected int level = 1;
  protected boolean isTavern = false;
  
  protected List<PlayPair> playType = List.of();
  
  protected Player playerLink;
  
  public Entity(Player playerLink)
  {
    this(playerLink, false);
  }
  
  public Entity(Player playerLink, boolean isGold)
  {
    this.isGold = isGold;
    
    ID = UUIDUtils.generateKey();
    name = this.getClass().getSimpleName();
    this.playerLink = playerLink;
  }
  
  @JsonProperty(F_PLAY_TYPE)
  public List<PlayPair> getPlayUnitType()
  {
    return playType;
  }
  
  @JsonProperty(F_LVL)
  public int getLevel()
  {
    return level;
  }
  
  public boolean isTavern()
  {
    return isTavern;
  }
  
  public void onHandled(Game game, Fight fight, Player player)
  {
    processListeners(
        ListenerUtils.getPlayerListener(fight, player).onHandledListeners,
        (action) -> action.process(game, fight, player, this),
        player);
    
    listener.processOnHandledListeners(game, fight, player, this);
    if (fight != null)
    {
      fight.addToHistory(ActionEvent.Type.ON_HANDLED, player, List.of(this));
    }
  }
  
  public void onPlayed(Game game, Fight fight, Player player)
  {
    onPlayed(game, fight, player, 0);
  }
  
  public void onPlayed(Game game, Fight fight, Player player, int index)
  {
    onPlayed(game, fight, player, index, false);
  }
  
  public void onPlayed(Game game, Fight fight, Player player, int index, int index2)
  {
    onPlayed(game, fight, player, List.of(index, 0, index2, 0));
  }
  
  public void onPlayed(Game game, Fight fight, Player player, int index, boolean isTavernIndex)
  {
    onPlayed(game, fight, player, List.of(index, isTavernIndex ? 1 : 0));
  }
  
  public void onPlayed(Game game, Fight fight, Player player, List<Integer> input)
  {
    onPlayed(game, fight, player, input, false);
  }
  
  public void onPlayed(Game game, Fight fight, Player player, List<Integer> input, boolean auto)
  {
    processListeners(
        ListenerUtils.getPlayerListener(fight, player).onPlayedListeners,
        (action) -> action.process(game, fight, player, this, input, auto),
        player);
    
    listener.processOnPlayedListeners(game, fight, player, this, input, auto);
    if (fight != null)
    {
      fight.addToHistory(ActionEvent.Type.ON_PLAYED, player, List.of(input, auto));
    }
  }
  
  protected <T> void processListeners(Map<String, T> listeners, Consumer<T> actionMove, Player player)
  {
    new HashMap<>(listeners).forEach((key, action) -> {
      actionMove.accept(action);
      if (key.startsWith(Listeners.KEY_ONCE_PREFIX))
      {
        player.getListener().removeListener(key);
      }
    });
  }
  
  public Entity newBase()
  {
    return ReflectUtils.getInstance(this.getClass(), playerLink);
  }
  
  public Entity newGold()
  {
    Entity u = this.newBase();
    u.setIsGold(true);
    
    return u;
  }
  
  public Entity newThis()
  {
    return isGold ? newGold() : newBase();
  }
  
  public Listener getListener()
  {
    return listener;
  }
  
  @Override
  public Entity clone()
  {
    Entity clonedEntity = (Entity) super.clone();
    
    clonedEntity.playerLink = playerLink;
    clonedEntity.listener = this.listener.clone();
    return clonedEntity;
  }
  
  @JsonProperty(F_IS_SPELL)
  public abstract boolean isSpell();
  
  protected Optional<Unit> getUnitFromTavernFriendlyInput(Fight fight, Player player, List<Integer> input)
  {
    return getUnitFromTavernFriendlyInput(fight, player, input, false);
  }
  
  protected Optional<Unit> getUnitFromTavernFriendlyInput(Fight fight, Player player, List<Integer> input, boolean onlyFriends)
  {
    boolean isTavernIndex;
    int index;
    if (input.size() < 2)
    {
      isTavernIndex = false;
      
      int unitsCount = fight != null ? fight.getFightTable(player).size() : player.getTable().size();
      index = RandUtils.getRand(unitsCount);
    }
    else
    {
      isTavernIndex = input.get(1) == 1;
      index = input.get(0);
    }
    
    if (onlyFriends)
    {
      isTavernIndex = false;
    }
    
    Unit unit = null;
    if (!isTavernIndex)
    {
      unit = fight != null
          ? fight.getFightTable(player).get(index)
          : player.getTable().get(index);
    }
    else
    {
      Entity entity1 = player.getTavern().getCards().get(index).getEntity();
      if (entity1 instanceof Unit unit1)
      {
        unit = unit1;
      }
    }
    
    return Optional.ofNullable(unit);
  }
  
  private static final int EXCAVATION_LIMIT = 3;
  
  protected void excavation(Player player, List<Class<? extends Unit>> allUnits)
  {
    if (allUnits.isEmpty())
    {
      return;
    }
    
    List<Class<? extends Unit>> classes;
    
    if (allUnits.size() < EXCAVATION_LIMIT)
    {
      classes = allUnits;
    }
    else
    {
      Set<Integer> setInts = new HashSet<>();
      
      while (setInts.size() < EXCAVATION_LIMIT)
      {
        setInts.add(RandUtils.getRand(allUnits.size()));
      }
      
      List<Integer> ints = setInts.stream().toList();
      
      classes = List.of(
          allUnits.get(ints.get(0)),
          allUnits.get(ints.get(1)),
          allUnits.get(ints.get(2)));
    }
    
    List<Card> units = classes.stream()
        .map(unitClass -> ReflectUtils.getInstance(unitClass, playerLink))
        .map(unit -> (Card) Card.of(unit))
        .toList();
    
    player.putSenderWaiter((param) -> {
      if (param < 0 || param > 3)
      {
        param = RandUtils.getRand(1, 3);
      }
      
      try
      {
        player.addToHand(units.get(param));
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    }, units);
  }
}