package ru.vladislavkomkov.model.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.vladislavkomkov.consts.Listeners;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.Listener;
import ru.vladislavkomkov.model.fight.FightEvent;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

public abstract class Entity implements Cloneable
{
  public final static String F_ID = "id";
  public final static String F_NAME = "name";
  public final static String F_DESCRIPTION = "description";
  public final static String F_IS_GOLD = "is_gold";
  public final static String F_LVL = "lvl";
  
  protected Listener listener = new Listener();
  
  protected int ID = System.identityHashCode(this);
  protected String name = this.getClass().getSimpleName();
  protected String description = "";
  protected int level = 1;
  protected boolean isTavern = false;
  protected boolean isGold;
  
  public Entity()
  {
    this(false);
  }
  
  public Entity(boolean isGold)
  {
    this.isGold = isGold;
    
    listener.onPlayedListeners.put(
        UUIDUtils.generateKey(),
        (game, fight, player, entity, index, isTavernIndex, index2, isTavernIndex2, auto) -> processListeners(
            player.listener.onPlayedListeners,
            (action) -> action.process(game, null, player, entity, index, isTavernIndex, index2, isTavernIndex2, auto),
            player));
    
    listener.onHandledListeners.put(
        UUIDUtils.generateKey(),
        (game, fight, player, entity) -> processListeners(
            player.listener.onHandledListeners,
            (action) -> action.process(game, null, player, entity),
            player));
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
    listener.processOnHandledListeners(game, fight, player, this);
    if (fight != null)
    {
      fight.addToHistory(FightEvent.Type.ON_HANDLED, player, List.of(this));
    }
  }
  
  public void onPlayed(Game game, Fight fight, Player player)
  {
    onPlayed(game, fight, player, 0, false);
  }
  
  public void onPlayed(Game game, Fight fight, Player player, int index)
  {
    onPlayed(game, fight, player, index, false, 0);
  }
  
  public void onPlayed(Game game, Fight fight, Player player, int index, int index2)
  {
    onPlayed(game, fight, player, index, false, index2);
  }
  
  public void onPlayed(Game game, Fight fight, Player player, int index, boolean isTavernIndex)
  {
    onPlayed(game, fight, player, index, isTavernIndex, 0);
  }
  
  public void onPlayed(Game game, Fight fight, Player player, int index, boolean isTavernIndex, int index2)
  {
    onPlayed(game, fight, player, index, isTavernIndex, index2, false);
  }
  
  public void onPlayed(Game game, Fight fight, Player player, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2)
  {
    onPlayed(game, fight, player, index, isTavernIndex, index2, isTavernIndex2, false);
  }
  
  public void onPlayed(Game game, Fight fight, Player player, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2, boolean auto)
  {
    listener.processOnPlayedListeners(game, fight, player, this, index, isTavernIndex, index2, isTavernIndex2, auto);
    if (fight != null)
    {
      fight.addToHistory(FightEvent.Type.ON_PLAYED, player, List.of(this, index, isTavernIndex, index2, isTavernIndex2, auto));
    }
  }
  
  protected <T> void processListeners(Map<String, T> listeners, Consumer<T> actionMove, Player player)
  {
    new HashMap<>(listeners).forEach((key, action) -> {
      actionMove.accept(action);
      if (key.startsWith(Listeners.KEY_ONCE_PREFIX))
      {
        player.listener.removeListener(key);
      }
    });
  }
  
  @JsonProperty(F_NAME)
  public String getName()
  {
    return name;
  }
  
  @JsonProperty(F_ID)
  public int getID()
  {
    return ID;
  }
  
  @JsonProperty(F_DESCRIPTION)
  public String getDescription()
  {
    return getDescription(null);
  }
  
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  public String getDescription(Player player)
  {
    return description;
  }
  
  public Entity newThis()
  {
    Supplier<? extends Entity> supplier = () -> {
      try
      {
        return this.getClass().getDeclaredConstructor().newInstance();
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    };
    
    return supplier.get();
  }
  
  @JsonProperty(F_IS_GOLD)
  public boolean isGold()
  {
    return isGold;
  }
  
  public void setIsGold(boolean isGold)
  {
    this.isGold = isGold;
  }
  
  public Listener getListener()
  {
    return listener;
  }
  
  @Override
  public Entity clone()
  {
    try
    {
      Entity clonedEntity = (Entity) super.clone();
      clonedEntity.listener = this.listener.clone();
      return clonedEntity;
    }
    catch (CloneNotSupportedException e)
    {
      throw new AssertionError("Clone not supported", e);
    }
  }
}