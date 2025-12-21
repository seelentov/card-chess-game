package ru.vladislavkomkov.controller;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.util.JWTUtils;
import ru.vladislavkomkov.util.UUIDUtils;

public class HTTPDataHandler
{
  static final String UUID_KEY = "UUID";
  static final Logger log = LoggerFactory.getLogger(HTTPDataHandler.class);
  
  final Map<String, Game> games;
  
  Javalin app;
  int port;
  GameProcessor processor;
  
  public HTTPDataHandler(int port, Map<String, Game> games, GameProcessor processor)
  {
    this.games = games;
    this.port = port;
    this.processor = processor;
    
    app = Javalin.create();
    
    app.get("/games", this::getAllGames);
    app.get("/games/{key}", this::getGameByKey);
    app.post("/games/{key}/start", this::startGame);
    app.post("/games", this::createGame);
  }
  
  public void start()
  {
    app.start(port);
    log.info("Server started on port {}", port);
  }
  
  public void stop()
  {
    app.stop();
  }
  
  void getAllGames(Context context)
  {
    context.json(games);
  }
  
  void getGameByKey(Context context)
  {
    String key = context.pathParam("key");
    log.debug("Getting game with key: {}", key);
    
    Game game = games.get(key);
    
    if (game == null)
    {
      throw new NotFoundResponse("Game not found with key: " + key);
    }
    
    context.json(game);
  }
  
  void createGame(Context context)
  {
    String uuid = UUIDUtils.generateKey();
    
    JWTUtils.generateToken(
        Map.of(UUID_KEY, uuid));
    
    Game game = new Game(uuid);
    
    games.put(uuid, game);
    
    log.info("Created new game with uuid: {}", uuid);
    
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Game created successfully");
    response.put("uuid", uuid);
    
    context.status(201);
    context.json(response);
  }
  
  void startGame(Context context)
  {
    String key = context.pathParam("key");
    Map req = context.bodyAsClass(Map.class);
    
    String reqKey = req.get("key").toString();
    String uuid = JWTUtils.extractClaim(reqKey, UUID_KEY);
    
    if (uuid.equals(key))
    {
      processor.start(key);
    }
    
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Game started successfully");
    response.put("key", key);
    
    context.status(201);
    context.json(response);
  }
  
  public class KeyRequest
  {
    String key;
    
    public KeyRequest()
    {
      
    }
    
    public KeyRequest(String key)
    {
      this.key = key;
    }
    
    public String getKey()
    {
      return key;
    }
    
    public void setKey(String key)
    {
      this.key = key;
    }
  }
}