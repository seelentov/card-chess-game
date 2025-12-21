package ru.vladislavkomkov.controller;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.vladislavkomkov.model.Game;

public class GameProcessor implements AutoCloseable
{
  static final Logger log = LoggerFactory.getLogger(GameProcessor.class);
  
  final Map<String, Game> games;
  final ExecutorService executor = Executors.newCachedThreadPool();
  
  final int preFightTimer;

  volatile AtomicBoolean isStoped = new AtomicBoolean(false);

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
      while (!isStoped.get())
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
  
  boolean process() throws Exception
  {
    Optional<Game> game = games.values().stream().findFirst();
    if (game.isPresent())
    {
      return process(game.get());
    }
    return false;
  }
  
  boolean process(String uuid) throws Exception
  {
    Game game = games.get(uuid);
    return process(game);
  }
  
  boolean process(Game game) throws Exception
  {
    if (processPreFight(game))
    {
      return true;
    }

    processFight(game);
    
    return false;
  }
  
  boolean processPreFight() throws Exception
  {
    Optional<Game> game = games.values().stream().findFirst();
    if (game.isPresent())
    {
      return processPreFight(game.get());
    }
    return false;
  }
  
  boolean processPreFight(String uuid) throws Exception
  {
    Game game = games.get(uuid);
    return processPreFight(game);
  }
  
  boolean processPreFight(Game game) throws Exception
  {
    if (game.calcFights())
    {
      return true;
    }
    
    game.doTurnBegin();
    
    int preFightTimer = this.preFightTimer == Integer.MAX_VALUE ? (game.getTurn() * 5000) + 30000 : this.preFightTimer;
    
    game.sendPreFightTimer(preFightTimer);
    
    if (preFightTimer > 0)
    {
      Thread.sleep(preFightTimer);
    }

    game.doTurnEnd();

    return false;
  }
  
  void processFight()
  {
    Optional<Game> game = games.values().stream().findFirst();
    game.ifPresent(this::processFight);
  }
  
  void processFight(String uuid)
  {
    Game game = games.get(uuid);
    processFight(game);
  }
  
  void processFight(Game game)
  {
    game.doFight();
    game.incTurn();
  }

  @Override
  public void close() throws Exception {
    isStoped.set(true);
  }
}
