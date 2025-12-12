package ru.vladislavkomkov.controller.sender;

import ru.vladislavkomkov.model.event.Event;

public class MockSender implements Sender
{
  final MockConsumer<Event> consumer;
  
  public MockSender(MockConsumer<Event> consumer)
  {
    this.consumer = consumer;
  }
  
  @Override
  public void send(byte[] data)
  {
    consumer.consume(new Event(data));
  }
}
