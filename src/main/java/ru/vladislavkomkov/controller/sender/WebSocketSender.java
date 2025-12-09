package ru.vladislavkomkov.controller.sender;

import java.util.function.Consumer;

import org.java_websocket.WebSocket;

public class WebSocketSender implements Sender
{
  private WebSocket conn;
  
  public WebSocketSender(WebSocket conn)
  {
    this.conn = conn;
  }
  
  @Override
  public void send(byte[] data)
  {
    conn.send(data);
  }
  
  @Override
  public void sendWithResponse(byte[] data, Consumer<byte[]> consumer)
  {
    consumer.accept(data);
  }
}
