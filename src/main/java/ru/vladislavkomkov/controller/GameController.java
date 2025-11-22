package ru.vladislavkomkov.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

public class GameController
{
  private static final Logger log = LoggerFactory.getLogger(GameController.class);
  private static Map<String, Game> games = new HashMap<>();
  
  private void accept(String gameUUID, String playerUUID, BiConsumer<Game, String> consumer)
  {
    consumer.accept(
        games.get(gameUUID),
        playerUUID);
  }
  
  private String create(Map<String, Player> players)
  {
    String key = UUIDUtils.generateKey();
    
    games.put(
        UUIDUtils.generateKey(),
        new Game(players));
    
    return key;
  }
  
  private void remove(String gameUUID)
  {
    try
    {
      games.get(gameUUID).close();
    }
    catch (Exception ex)
    {
      log.error("Error on game close: ", ex);
    }
    finally
    {
      games.remove(gameUUID);
    }
  }
}
