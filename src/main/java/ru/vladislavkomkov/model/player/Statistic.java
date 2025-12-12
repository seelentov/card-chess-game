package ru.vladislavkomkov.model.player;

import java.util.concurrent.atomic.AtomicInteger;

public class Statistic
{
  public final Played played = new Played();
  public final Counters counters = new Counters();
  
  public Statistic()
  {
  }
  
  public void reset()
  {
    played.resetOnlyBluesPlayed();
    counters.resetFreeTavern();
  }
  
  public int getFreeTavernCount()
  {
    return counters.getFreeTavernCount();
  }
  
  public int useFreeTavern()
  {
    return counters.useFreeTavern();
  }
  
  public int addFreeTavern()
  {
    return counters.incrementFreeTavern();
  }
  
  public static class Played
  {
    public int onlyBluesPlayed = 0;
    
    public void incrementOnlyBluesPlayed()
    {
      onlyBluesPlayed++;
    }
    
    public void resetOnlyBluesPlayed()
    {
      onlyBluesPlayed = 0;
    }
  }
  
  public static class Counters
  {
    final AtomicInteger freeTavernCounter = new AtomicInteger(0);
    
    public int useFreeTavern()
    {
      return freeTavernCounter.getAndUpdate(i -> i > 0 ? i - 1 : i);
    }
    
    public int incrementFreeTavern()
    {
      return freeTavernCounter.incrementAndGet();
    }
    
    public int getFreeTavernCount()
    {
      return freeTavernCounter.get();
    }
    
    public void setFreeTavernCount(int count)
    {
      freeTavernCounter.set(Math.max(0, count));
    }
    
    public void resetFreeTavern()
    {
      freeTavernCounter.set(0);
    }
  }
}