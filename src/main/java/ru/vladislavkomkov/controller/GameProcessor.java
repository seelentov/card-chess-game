package ru.vladislavkomkov.controller;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.vladislavkomkov.model.Game;

public class GameProcessor
{
  static final Logger log = LoggerFactory.getLogger(GameProcessor.class);
  
  final Map<String, Game> games;
  final ExecutorService executor = Executors.newCachedThreadPool();
  
  final int preFightTimer;
  
  public GameProcessor(Map<String, Game> games)
  {
    this(games, Integer.MAX_VALUE);
  }
  
  public GameProcessor(Map<String, Game> games, int preFightTimer)
  {
    this.games = games;
    this.preFightTimer = preFightTimer;
  }
  
  public void start()
  {
    Optional<Game> game = games.values().stream().findFirst();
    game.ifPresent(this::start);
  }
  
  public void start(String uuid)
  {
    Game game = games.get(uuid);
    start(game);
  }
  
  public void start(Game game)
  {
    game.sendStartGame();
    
    executor.submit(() -> {
      while (true)
      {
        try
        {
          if (process(game))
          {
            break;
          }
        }
        catch (Exception ex)
        {
          log.error("Error: ", ex);
        }
      }
    });
  }
  
  public void process() throws Exception
  {
    Optional<Game> game = games.values().stream().findFirst();
    if (game.isPresent())
    {
      process(game.get());
    }
  }
  
  public void process(String uuid) throws Exception
  {
    Game game = games.get(uuid);
    process(game);
  }
  
  boolean process(Game game) throws Exception
  {
    if (game.calcFights())
    {
      return true;
    }
    
    game.doPreFight();
    
    int preFightTimer = this.preFightTimer == Integer.MAX_VALUE ? (game.getTurn() * 5000) + 30000 : this.preFightTimer;
    
    game.sendPreFightTimer(preFightTimer);
    
    if (preFightTimer > 0)
    {
      Thread.sleep(preFightTimer);
    }
    
    game.doFight();
    
    game.incTurn();
    
    return false;
  }
}
