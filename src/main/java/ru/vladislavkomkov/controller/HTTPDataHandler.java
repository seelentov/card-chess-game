package ru.vladislavkomkov.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.plugin.bundled.CorsPluginConfig;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.util.JWTUtils;
import ru.vladislavkomkov.util.UUIDUtils;

public class HTTPDataHandler implements AutoCloseable
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
    
    app = Javalin.create(config -> {
      config.bundledPlugins.enableCors(corsPluginConfig -> corsPluginConfig.addRule(CorsPluginConfig.CorsRule::anyHost));
    });
    
    app.get("/games", this::getAllGames);
    app.get("/games/{id}", this::getGameByKey);
    app.post("/games/{id}/start", this::startGame);
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
    context.status(201);
    context.json(games.values().stream()
        .filter(game -> game.getState() == Game.State.LOBBY)
        .map(game -> Map.of(
            "uuid", game.getUUID(),
            "players_count", game.getPlayers().size()))
        .toList());
  }
  
  void getGameByKey(Context context)
  {
    String UUID = context.pathParam("id");
    log.debug("Getting game with key: {}", UUID);
    
    Game game = games.get(UUID);
    
    if (game == null)
    {
      throw new NotFoundResponse("Game not found with UUID: " + UUID);
    }
    
    context.status(201);
    context.json(
        game
            .getPlayers()
            .values()
            .stream()
            .map(player -> Map.of("uuid", player.getUUID()))
            .toList());
  }
  
  void createGame(Context context)
  {
    String uuid = UUIDUtils.generateKey();
    
    String key = JWTUtils.generateToken(
        Map.of(UUID_KEY, uuid));
    
    Game game = new Game(uuid);
    
    games.put(uuid, game);
    
    log.info("Created new game with uuid: {}", uuid);
    
    context.status(201);
    context.json(Map.of(
        "key", key,
        "uuid", uuid));
  }
  
  void startGame(Context context)
  {
    String UUID = context.pathParam("id");
    Map req = context.bodyAsClass(Map.class);
    
    String reqKey = req.get("key").toString();
    String uuidKey = JWTUtils.extractClaim(reqKey, UUID_KEY);
    
    if (uuidKey.equals(UUID))
    {
      
      if (games.get(UUID).getState() != Game.State.LOBBY)
      {
        throw new RuntimeException("Game already started");
      }
      
      if (games.get(UUID).getPlayers().size() % 2 != 0)
      {
        throw new RuntimeException("Users count not % 2");
      }
      
      games.get(UUID).startGame(processor);
    }
    
    context.status(201);
  }
  
  @Override
  public void close() throws Exception
  {
    app.stop();
  }
}