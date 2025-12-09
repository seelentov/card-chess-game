package ru.vladislavkomkov.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vladislavkomkov.model.Game;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import ru.vladislavkomkov.util.JWTUtils;
import ru.vladislavkomkov.util.UUIDUtils;

import java.util.Map;
import java.util.HashMap;

public class HTTPDataHandler
{
  private static final String UUID_KEY = "UUID";
  private static final Logger log = LoggerFactory.getLogger(HTTPDataHandler.class);
  
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
    
    app.get("/", this::index);
    app.get("/games", this::getAllGames);
    app.get("/games/{key}", this::getGameByKey);
    app.post("/games/{key}/start", this::getGameByKey);
    app.post("/games", this::startGame);
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
  
  private void index(Context context)
  {
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Game Server API");
    response.put("endpoints", new String[] {
        "GET /games - получить все игры",
        "GET /games/{key} - получить игру по ключу",
        "POST /games - создать новую игру"
    });
    context.json(response);
  }
  
  private void getAllGames(Context context)
  {
    context.json(games);
  }
  
  private void getGameByKey(Context context)
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
  
  private void createGame(Context context)
  {
    String key = UUIDUtils.generateKey();
    
    JWTUtils.generateToken(
        Map.of(UUID_KEY, key));
    
    Game game = new Game(key);
    
    games.put(key, game);
    
    log.info("Created new game with key: {}", key);
    
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Game created successfully");
    response.put("key", key);
    response.put("game", game);
    
    context.status(201);
    context.json(response);
  }
  
  private void startGame(Context context)
  {
    String key = context.pathParam("key");
    KeyRequest req = context.bodyAsClass(KeyRequest.class);
    
    String reqKey = req.getKey();
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
    private String key;
    
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