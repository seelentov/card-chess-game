package ru.vladislavkomkov.controller.sender;

import org.java_websocket.WebSocket;

import ru.vladislavkomkov.model.event.Event;

public class WebSocketSender implements Sender
{
  WebSocket conn;
  
  public WebSocketSender(WebSocket conn)
  {
    this.conn = conn;
  }
  
  @Override
  public void send(byte[] data)
  {
    try
    {
      conn.send(data);
    }
    catch (Exception ex)
    {
      conn.send(new Event(null, null, Event.Type.ERROR, ex.getMessage()).getBytes());
    }
  }
}
