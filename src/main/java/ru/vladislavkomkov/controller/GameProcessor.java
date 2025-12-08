package ru.vladislavkomkov.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vladislavkomkov.model.Game;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameProcessor
{
  private static final Logger log = LoggerFactory.getLogger(GameProcessor.class);
  
  final Map<String, Game> games;
  final ExecutorService executor = Executors.newCachedThreadPool();
  
  public GameProcessor(Map<String, Game> games)
  {
    this.games = games;
  }
  
  public void append(Game game)
  {
    games.put(game.getUUID(), game);
  }
  
  public void start(String uuid)
  {
    Game game = games.get(uuid);
    
    executor.submit(() -> {
      while (true)
      {
        try
        {
          if (game.calcFights())
          {
            break;
          }
          
          game.doPreFight();
          
          long preFightTimer = (game.getTurn() * 5000L) + 30000;
          
          game.sendPreFightTimer(preFightTimer);
          Thread.sleep(preFightTimer);
          
          game.doFight();
        }
        catch (Exception ex)
        {
          log.error("Error: ", ex);
        }
      }
    });
  }
}
