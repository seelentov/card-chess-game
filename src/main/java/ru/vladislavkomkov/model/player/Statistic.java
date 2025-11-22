package ru.vladislavkomkov.model.player;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Statistic implements Serializable
{
  public Played played = new Played();
  public Counters counters = new Counters();
  
  public Statistic()
  {
  }
  
  static class Played
  {
    public int onlyBluesPlayed = 0;
  }
  
  static class Counters
  {
    private final AtomicInteger freeTavernCounter = new AtomicInteger(0);

    public int freeTavernCounter()
    {
      return freeTavernCounter.getAndUpdate(i -> i > 0 ? i - 1 : i);
    }

    public int freeTavernCounterInc()
    {
      return freeTavernCounter.incrementAndGet();
    }
  }
}