package ru.vladislavkomkov.controller.sender;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockSender implements Sender
{
  private static final Logger log = LoggerFactory.getLogger(MockSender.class);
  
  @Override
  public void send(byte[] data)
  {
    log.warn("Send mock data: {}", data);
  }
  
  @Override
  public void sendWithResponse(byte[] data, Consumer<byte[]> consumer)
  {
    log.warn("Send mock data with res: {}", data);
  }
}
