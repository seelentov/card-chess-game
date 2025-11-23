package ru.vladislavkomkov.util;

import ru.vladislavkomkov.consts.Listeners;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.action.GlobalAction;
import ru.vladislavkomkov.model.player.Player;

import java.util.HashMap;
import java.util.Map;

public class ListenerUtils {
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
