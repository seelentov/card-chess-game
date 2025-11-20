package ru.vladislavkomkov.models.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import ru.vladislavkomkov.consts.Listeners;
import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.Listener;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.util.ListenerUtils;
import ru.vladislavkomkov.util.SerializationUtils;

public abstract class Entity implements Serializable, Cloneable
{
  protected final Listener listener = new Listener();
  protected int ID = System.identityHashCode(this);
  protected String name = this.getClass().getSimpleName();
  protected String description = "";
  protected int level = 1;
  protected boolean isTavern = false;
  protected boolean isGold = false;
  
  public Entity()
  {
    listener.onPlayedListeners.put(
        ListenerUtils.generateKey(),
        (game, player, entity, index, isTavernIndex, index2, isTavernIndex2) -> processListeners(player.listener.onPlayedListeners,
            (action) -> action.process(game, player, this, index, isTavernIndex, index2, isTavernIndex2), player));
    
    listener.onHandledListeners.put(
        ListenerUtils.generateKey(),
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
}
