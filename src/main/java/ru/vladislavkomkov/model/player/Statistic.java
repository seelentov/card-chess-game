package ru.vladislavkomkov.model.player;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import ru.vladislavkomkov.model.entity.unit.UnitType;

public class Statistic
{
  final Played played = new Played();
  final Counters counters = new Counters();
  final Boosts boosts = new Boosts();
  
  public Statistic()
  {
  }
  
  public Played getPlayed()
  {
    return played;
  }
  
  public Counters getCounters()
  {
    return counters;
  }
  
  public Boosts getBoosts()
  {
    return boosts;
  }
  
  public static class Played
  {
    int onlyBluesPlayed = 0;
    
    public int getOnlyBluesPlayed()
    {
      return onlyBluesPlayed;
    }
    
    public void incOnlyBluesPlayed()
    {
      onlyBluesPlayed++;
    }
  }
  
  public static class Boosts
  {
    Map<UnitType, Integer> attackByUnitType = Map.of();
    Map<UnitType, Integer> healthByUnitType = Map.of();
    
    int attackUnit = 0;
    int healthUnit = 0;
    
    int attackSpell = 0;
    int healthSpell = 0;
    
    public void incAttackByUnitType(UnitType type, int i)
    {
      incByUnitType(attackByUnitType, type, i);
    }
    
    public void incHealthByUnitType(UnitType type, int i)
    {
      incByUnitType(healthByUnitType, type, i);
    }
    
    private void incByUnitType(Map<UnitType, Integer> map, UnitType type, int i)
    {
      map.merge(type, i, Integer::sum);
    }
    
    public int getAttackByUnitType(UnitType type)
    {
      return getByType(attackByUnitType, type);
    }
    
    public int getAttackByUnitType(List<UnitType> type)
    {
      return type.stream().map(this::getAttackByUnitType).reduce(0, Integer::sum);
    }
    
    public int getHealthByUnitType(UnitType type)
    {
      return getByType(healthByUnitType, type);
    }
    
    public int getHealthByUnitType(List<UnitType> type)
    {
      return type.stream().map(this::getHealthByUnitType).reduce(0, Integer::sum);
    }
    
    private int getByType(Map<UnitType, Integer> map, UnitType type)
    {
      if (type == UnitType.ALL)
      {
        return map.values().stream().reduce(0, Integer::sum);
      }
      
      return map.getOrDefault(type, 0);
    }
    
    public int getAttackUnit()
    {
      return attackUnit;
    }
    
    public void incAttackUnit(int i)
    {
      attackUnit += i;
    }
    
    public int getHealthUnit()
    {
      return healthUnit;
    }
    
    public void incHealthUnit(int i)
    {
      healthUnit += i;
    }
    
    public int getAttackSpell()
    {
      return attackSpell;
    }
    
    public void incAttackSpell(int i)
    {
      attackSpell += i;
    }
    
    public int getHealthSpell()
    {
      return healthSpell;
    }
    
    public void incHealthSpell(int i)
    {
      healthSpell += i;
    }
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