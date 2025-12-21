package ru.vladislavkomkov.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.vladislavkomkov.model.Game;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class HTTPDataHandlerIntegrationTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private HTTPDataHandler handler;
    private int port;
    private final Map<String, Game> games = new ConcurrentHashMap<>();
    private final GameProcessor gameProcessor = new GameProcessor(games);
    
    private final HttpClient httpClient = HttpClient.newHttpClient();
    
    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        port = 0;
        handler = new HTTPDataHandler(port, games, gameProcessor);
        handler.start();
        
        port = getActualPort(handler);
    }
    
    @AfterEach
    void tearDown() {
        if (handler != null) {
            handler.stop();
        }
    }
    
    private int getActualPort(HTTPDataHandler handler) {
        return handler.app.port();
    }
    
    @Test
    void testCreateGame() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/games"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/json")
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(201, response.statusCode());
        
        JsonNode json = objectMapper.readTree(response.body());
        assertTrue(json.has("uuid"));
        assertTrue(json.has("message"));
        assertEquals("Game created successfully", json.get("message").asText());
        
        String uuid = json.get("uuid").asText();
        assertNotNull(UUID.fromString(uuid)); // валидный UUID
        
        assertTrue(games.containsKey(uuid));
    }
    
    @Test
    void testGetAllGames() throws IOException, InterruptedException {
        HttpRequest createReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/games"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        httpClient.send(createReq, HttpResponse.BodyHandlers.ofString());
        
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/games"))
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(getReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        
        JsonNode json = objectMapper.readTree(response.body());
        assertTrue(json.isArray() || json.isObject()); // зависит от сериализации Map
        
        assertTrue(json.size() > 0);
    }
    
    @Test
    void testGetGameByKey() throws IOException, InterruptedException {
        HttpRequest createReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/games"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> createResp = httpClient.send(createReq, HttpResponse.BodyHandlers.ofString());
        String uuid = objectMapper.readTree(createResp.body()).get("uuid").asText();
        
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/games/" + uuid))
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(getReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        
        JsonNode gameJson = objectMapper.readTree(response.body());
        assertEquals(uuid, gameJson.get("uuid").asText());
    }
    
    @Test
    void testGetGameByKey_NotFound() throws IOException, InterruptedException {
        String nonExistentKey = "nonexistent123";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/games/" + nonExistentKey))
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }
    
    @Test
    void testStartGame() throws IOException, InterruptedException {
        HttpResponse<String> createResp = httpClient.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:" + port + "/games"))
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );
        String uuid = objectMapper.readTree(createResp.body()).get("uuid").asText();
        
        String token = ru.vladislavkomkov.util.JWTUtils.generateToken(Map.of("UUID", uuid));
        
        String requestBody = objectMapper.writeValueAsString(Map.of("key", token));
        
        HttpRequest startReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/games/" + uuid + "/start"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();
        
        HttpResponse<String> response = httpClient.send(startReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        
        JsonNode json = objectMapper.readTree(response.body());
        assertEquals("Game started successfully", json.get("message").asText());
    }
}