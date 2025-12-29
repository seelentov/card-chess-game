package ru.vladislavkomkov.model.player;

import ru.vladislavkomkov.model.entity.unit.UnitType;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Statistic
{
  public final Played played = new Played();
  public final Counters counters = new Counters();
  public final Boosts boosts = new Boosts();
  
  public Statistic()
  {
  }
  
  public static class Played
  {
    public int onlyBluesPlayed = 0;
  }
  
  public static class Boosts
  {
    public Map<UnitType, Integer> byUnitType = Map.of(
        UnitType.BEAST, 0,
        UnitType.NAGA, 0,
        UnitType.DRAGON, 0,
        UnitType.UNDEAD, 0,
        UnitType.DEMON, 0,
        UnitType.MECH, 0,
        UnitType.ELEMENTAL, 0,
        UnitType.ALL, 0);

    public int tavern = 0;
  }
  
  public static class Counters
  {
    final AtomicInteger freeTavernReset = new AtomicInteger(0);
    final AtomicInteger incLevelDecreaser = new AtomicInteger(0);
    
    public int useFreeTavernReset()
    {
      return freeTavernReset.getAndUpdate(i -> i > 0 ? i - 1 : i);
    }
    
    public int incrementFreeTavernReset()
    {
      return freeTavernReset.incrementAndGet();
    }
    
    public int getFreeTavernResetCount()
    {
      return freeTavernReset.get();
    }
    
    public void setFreeTavernResetCount(int count)
    {
      freeTavernReset.set(Math.max(0, count));
    }
    
    public void resetFreeTavernReset()
    {
      freeTavernReset.set(0);
    }
    
    public int useIncLevelDecreaser()
    {
      return incLevelDecreaser.getAndUpdate(i -> i > 0 ? i - 1 : i);
    }
    
    public int incrementIncLevelDecreaser()
    {
      return incLevelDecreaser.incrementAndGet();
    }
    
    public int getIncLevelDecreaser()
    {
      return incLevelDecreaser.get();
    }
    
    public void setIncLevelDecreaser(int count)
    {
      incLevelDecreaser.set(Math.max(0, count));
    }
    
    public void resetIncLevelDecreaser()
    {
      incLevelDecreaser.set(0);
    }
  }
}