package ru.vladislavkomkov.controller;

import ru.vladislavkomkov.model.Game;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameProcessor
{
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
      
    executor.submit(()->{
        while (game.state.equals()){

        }
    })
  }
}
