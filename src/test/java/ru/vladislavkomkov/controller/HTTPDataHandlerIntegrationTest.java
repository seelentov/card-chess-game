package ru.vladislavkomkov.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.JWTUtils;
import ru.vladislavkomkov.util.UUIDUtils;

public class HTTPDataHandlerIntegrationTest extends IntegrationTestCase
{
  private ObjectMapper objectMapper = new ObjectMapper();
  
  private GameProcessor processor;
  private HTTPDataHandler handler;
  
  private Map<String, Game> games;
  private Game game;
  
  private String gameUUID = "11000000-0000-0000-0000-000000000000";
  private int port;
  
  private HttpClient client;
  
  @BeforeEach
  void setUp()
  {
    port = 8081;
    
    games = new HashMap<>();
    
    game = new Game(gameUUID);
    games.put(gameUUID, game);
    
    processor = new GameProcessor(games);
    
    handler = new HTTPDataHandler(port, games, processor);
    
    client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .build();
    
    handler.start();
  }
  
  @AfterEach
  void teardown() throws Exception
  {
    if (processor != null)
    {
      processor.close();
    }
    
    if (handler != null)
    {
      handler.close();
    }
  }
  
  @Test
  void testGetAllGames() throws Exception
  {
    String key = UUIDUtils.generateKey();
    game.addPlayer(key, new Player(key, game));
    
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + port + "/games"))
        .GET()
        .build();
    
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    List<Map<String, Object>> responseBody = objectMapper.readValue(
        response.body(),
        new TypeReference<List<Map<String, Object>>>()
        {
        });
    
    assertEquals(201, response.statusCode(), response.toString());
    
    assertFalse(responseBody.isEmpty());
    assertTrue(responseBody.stream().allMatch(game -> game.containsKey("uuid")));
    
    assertEquals(1, responseBody.size());
    assertEquals(1, responseBody.get(0).get("players_count"));
  }
  
  @Test
  void testGetGame() throws Exception
  {
    String key = UUIDUtils.generateKey();
    game.addPlayer(key, new Player(key, game));
    
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + port + "/games/" + game.getUUID()))
        .GET()
        .build();
    
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    List<Map<String, Object>> responseBody = objectMapper.readValue(
        response.body(),
        new TypeReference<List<Map<String, Object>>>()
        {
        });
    
    assertEquals(201, response.statusCode(), response.toString());
    
    assertFalse(responseBody.isEmpty());
    assertTrue(responseBody.stream().allMatch(game -> game.containsKey("uuid")));
    
    assertEquals(1, responseBody.size());
    assertEquals(key, responseBody.get(0).get("uuid"));
  }
  
  @Test
  void testCreateGame() throws Exception
  {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + port + "/games"))
        .POST(HttpRequest.BodyPublishers.noBody())
        .build();
    
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
    assertEquals(201, response.statusCode(), response.toString());
    
    Map<String, Object> responseBody = objectMapper.readValue(response.body(), new TypeReference<>()
    {
    });
    assertTrue(responseBody.containsKey("key"));
    assertNotNull(responseBody.get("key"));
    assertFalse(((String) responseBody.get("key")).isBlank());
    
    assertEquals(2, games.size());
  }
  
  @Test
  void testStartGame() throws Exception
  {
    HttpRequest createRequest = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + port + "/games"))
        .POST(HttpRequest.BodyPublishers.noBody())
        .build();
    
    HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
    assertEquals(201, createResponse.statusCode());
    
    Map<String, Object> createResponseBody = objectMapper.readValue(createResponse.body(), new TypeReference<>()
    {
    });
    String key = (String) createResponseBody.get("key");
    
    String gameUUID = (String) JWTUtils.extractClaim(key, HTTPDataHandler.UUID_KEY);
    
    Game game = games.get(gameUUID);
    String player1Key = UUIDUtils.generateKey();
    String player2Key = UUIDUtils.generateKey();
    game.addPlayer(player1Key, new Player(player1Key, game));
    game.addPlayer(player2Key, new Player(player2Key, game));
    
    Map<String, String> requestBody = Map.of("key", key);
    String jsonBody = objectMapper.writeValueAsString(requestBody);
    
    HttpRequest startRequest = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + port + "/games/" + gameUUID + "/start"))
        .header("Content-UnitType", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();
    
    HttpResponse<String> startResponse = client.send(startRequest, HttpResponse.BodyHandlers.ofString());
    
    assertEquals(201, startResponse.statusCode());
    assertTrue(waitForCondition(() -> game.getState() != Game.State.LOBBY, 10000));
  }
  
  @Test
  void testStartGameIfCant()
  {
    HttpRequest createRequest = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + port + "/games"))
        .POST(HttpRequest.BodyPublishers.noBody())
        .build();
    
    HttpResponse<String> createResponse;
    try
    {
      createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    assertEquals(201, createResponse.statusCode());
    
    Map<String, Object> createResponseBody = null;
    try
    {
      createResponseBody = objectMapper.readValue(createResponse.body(), new TypeReference<>()
      {
      });
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    String key = (String) createResponseBody.get("key");
    String gameUUID = (String) JWTUtils.extractClaim(key, HTTPDataHandler.UUID_KEY);
    
    Game game = games.get(gameUUID);
    String playerKey = UUIDUtils.generateKey();
    game.addPlayer(playerKey, new Player(playerKey, game));
    
    Map<String, String> requestBody = Map.of("key", key);
    String jsonBody = null;
    try
    {
      jsonBody = objectMapper.writeValueAsString(requestBody);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    
    HttpRequest startRequest = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + port + "/games/" + gameUUID + "/start"))
        .header("Content-UnitType", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();
    
    HttpResponse<String> startResponse;
    try
    {
      startResponse = client.send(startRequest, HttpResponse.BodyHandlers.ofString());
    }
    catch (Exception e)
    {
      return;
    }
    
    assertNotEquals(201, startResponse.statusCode());
  }
}