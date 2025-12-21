package ru.vladislavkomkov.controller.sender;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.vladislavkomkov.model.event.Event;

public class MockWebSocketClient extends WebSocketClient
{
  private static final Logger log = LoggerFactory.getLogger(MockWebSocketClient.class);
  private MockConsumer consumer;
  
  public MockWebSocketClient(URI serverUri, MockConsumer consumer)
  {
    super(serverUri);
    this.consumer = consumer;
  }
  
  @Override
  public void onOpen(ServerHandshake handshakedata)
  {
    log.info("Connected to server: {}", getURI());
  }
  
  @Override
  public void onMessage(String message)
  {
    log.info("Received string message: {}", message);
  }
  
  @Override
  public void onMessage(ByteBuffer bytes)
  {
    Event event = new Event(bytes);
    log.info("Received message {}", event);
    consumer.consume(event);
  }
  
  @Override
  public void onClose(int code, String reason, boolean remote)
  {
    log.info("Connection closed: code={}, reason={}, remote={}", code, reason, remote);
  }
  
  @Override
  public void onError(Exception ex)
  {
    log.error("WebSocket error", ex);
  }
}