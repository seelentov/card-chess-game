package ru.vladislavkomkov.model.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import ru.vladislavkomkov.consts.Listeners;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.Listener;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.SerializationUtils;
import ru.vladislavkomkov.util.UUIDUtils;

public abstract class Entity implements Serializable, Cloneable
{
  protected final Listener listener = new Listener();
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
        (game, player, entity, index, isTavernIndex, index2, isTavernIndex2) -> processListeners(player.listener.onPlayedListeners,
            (action) -> action.process(game, player, this, index, isTavernIndex, index2, isTavernIndex2), player));
    
    listener.onHandledListeners.put(
        UUIDUtils.generateKey(),
        (game, player, entity) -> processListeners(player.listener.onHandledListeners, (action) -> action.process(game, player, this), player)
    
    );
  }
  
  public int getLevel()
  {
    return level;
  }
  
  public boolean isTavern()
  {
    return isTavern;
  }
  
  public void onHandled(Game game, Player player)
  {
    listener.processOnHandledListeners(game, player, this);
  };
  
  public void onPlayed(Game game, Player player)
  {
    onPlayed(game, player, 0);
  }
  
  public void onPlayed(Game game, Player player, int index)
  {
    onPlayed(game, player, index, false, 0);
  }
  
  public void onPlayed(Game game, Player player, int index, int index2)
  {
    onPlayed(game, player, index, false, index2);
  }
  
  public void onPlayed(Game game, Player player, int index, boolean isTavernIndex)
  {
    onPlayed(game, player, index, isTavernIndex, 0);
  }
  
  public void onPlayed(Game game, Player player, int index, boolean isTavernIndex, int index2)
  {
    onPlayed(game, player, index, isTavernIndex, index2, false);
  }
  
  public void onPlayed(Game game, Player player, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2)
  {
    listener.processOnPlayedListeners(game, player, this, index, isTavernIndex, index2, isTavernIndex2);
  };
  
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
  
  public String getName()
  {
    return name;
  }
  
  public int getID()
  {
    return ID;
  }
  
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
  
  @Override
  public Unit clone()
  {
    try
    {
      Unit unit = (Unit) super.clone();
      return SerializationUtils.deepCopy(unit);
    }
    catch (CloneNotSupportedException e)
    {
      throw new RuntimeException(e);
    }
  }
  
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
}
