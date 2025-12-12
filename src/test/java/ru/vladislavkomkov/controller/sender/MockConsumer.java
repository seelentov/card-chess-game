package ru.vladislavkomkov.controller.sender;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;

public class MockConsumer<T>
{
  Queue<T> queue = new ArrayDeque<>();
  Consumer<T> consumer;
  
  public MockConsumer(Consumer<T> consumer)
  {
    
  }
  
  public void consume(T item)
  {
    queue.add(item);
  }
  
  public T process()
  {
    T ev = queue.poll();
    consumer.accept(ev);
    return ev;
  }
}
