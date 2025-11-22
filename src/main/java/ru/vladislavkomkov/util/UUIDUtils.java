package ru.vladislavkomkov.util;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE_PREFIX;
import static ru.vladislavkomkov.consts.Listeners.KEY_ONCE_PREFIX;
import static ru.vladislavkomkov.consts.Listeners.KEY_TEMP_PREFIX;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ru.vladislavkomkov.consts.Listeners;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.action.GlobalAction;
import ru.vladislavkomkov.model.player.Player;

public class UUIDUtils
{
  public static String generateKeyOnce()
  {
    return generateKey(KEY_ONCE_PREFIX);
  }
  
  public static String generateKeyCore()
  {
    return generateKey(KEY_CORE_PREFIX);
  }
  
  public static String generateKeyTemp()
  {
    return generateKey(KEY_TEMP_PREFIX);
  }
  
  public static String generateKeyTemp(Integer uuid)
  {
    return generateKeyTemp(uuid.toString());
  }
  
  public static String generateKeyTemp(String uuid)
  {
    return generateKey(KEY_TEMP_PREFIX) + uuid;
  }
  
  public static String generateKey()
  {
    return generateKey("");
  }
  
  public static String generateKey(String prefix)
  {
    return prefix + UUID.randomUUID();
  }
  
  public static void processGlobalActionListeners(Map<String, ? extends GlobalAction> listeners, Game game, Player player)
  {
    new HashMap<>(listeners).forEach((k, v) -> processStartEndAction(game, k, v, player));
  }
  
  static void processStartEndAction(Game game, String key, GlobalAction action, Player player)
  {
    action.process(game, player);
    if (key.startsWith(Listeners.KEY_ONCE_PREFIX))
    {
      player.listener.removeListener(key);
    }
  }
}
