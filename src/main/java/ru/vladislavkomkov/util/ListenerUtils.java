package ru.vladislavkomkov.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import ru.vladislavkomkov.consts.Listeners;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.action.FightAction;
import ru.vladislavkomkov.model.action.GlobalAction;
import ru.vladislavkomkov.model.player.Player;

public class ListenerUtils
{
  public static <T> void processActionListeners(Map<String, T> listeners, Player player, Consumer<T> consumer)
  {
    new HashMap<>(listeners).forEach((key, action) -> {
      consumer.accept(action);
      if (key.startsWith(Listeners.KEY_ONCE_PREFIX))
      {
        player.listener.removeListener(key);
      }
    });
  }
  
  public static void processFightActionListeners(Map<String, ? extends FightAction> listeners, Game game, Fight fight, Player player, Player player2)
  {
    new HashMap<>(listeners).forEach((k, v) -> processFightAction(game, k, v, fight, player, player2));
  }
  
  public static void processGlobalActionListeners(Map<String, ? extends GlobalAction> listeners, Game game, Fight fight,Player player)
  {
    new HashMap<>(listeners).forEach((k, v) -> processStartEndAction(game, k, v, fight, player));
  }
  static void processFightAction(Game game, String key, FightAction action, Fight fight, Player player, Player player2)
  {
    action.process(game, fight, player, player2);
    if (key.startsWith(Listeners.KEY_ONCE_PREFIX))
    {
      player.listener.removeListener(key);
    }
  }
  
  static void processStartEndAction(Game game, String key, GlobalAction action, Fight fight, Player player)
  {
    action.process(game, fight, player);
    if (key.startsWith(Listeners.KEY_ONCE_PREFIX))
    {
      player.listener.removeListener(key);
    }
  }
}
