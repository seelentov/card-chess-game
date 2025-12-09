package ru.vladislavkomkov.controller;

import ru.vladislavkomkov.model.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server
{
  public static void main(String[] args)
  {
    Map<String, Game> games = new HashMap<>();
    
    int portHttp = 8080;
    int portWS = 8081;
    
    if (args.length > 0)
    {
      portHttp = Integer.parseInt(args[0]);
    }
    
    if (args.length > 1)
    {
      portWS = Integer.parseInt(args[1]);
    }

    GameProcessor processor = new GameProcessor(games);
    HTTPDataHandler httpDataHandler = new HTTPDataHandler(portHttp, games,processor);
    WSEventHandler wsEventHandler = new WSEventHandler(portWS, games);
    
    ExecutorService executor = Executors.newFixedThreadPool(2);
    
    executor.submit(() -> {
      Thread.currentThread().setName("HTTP-Server");
      httpDataHandler.start();
    });
    
    executor.submit(() -> {
      Thread.currentThread().setName("WS-Server");
      wsEventHandler.start();
    });
  }
}
