package ru.vladislavkomkov.util;

import java.util.Map;

public class RandUtils
{
  static Map<Integer, Integer[]> lvlPercentMap = Map.of(
      1, new Integer[] { 100, 0, 0, 0, 0, 0 },
      2, new Integer[] { 70, 30, 0, 0, 0, 0 },
      3, new Integer[] { 50, 30, 20, 0, 0, 0 },
      4, new Integer[] { 30, 30, 30, 10, 0, 0 },
      5, new Integer[] { 20, 25, 30, 20, 5, 0 },
      6, new Integer[] { 15, 20, 25, 25, 10, 5 });
  
  public static int getRand(int max)
  {
    return getRand(0, max);
  }
  
  public static int getRand(int min, int max)
  {
    return (int) ((Math.random() * (max - min)) + min);
  }
  
  public static int getRandLvl(int maxLvl)
  {
    Integer[] probabilities = lvlPercentMap.get(maxLvl);
    if (probabilities == null)
    {
      throw new IllegalArgumentException("Invalid tavern level: " + maxLvl);
    }
    
    int randomValue = (int) (Math.random() * 100);
    
    int cumulativeProbability = 0;
    for (int level = 0; level < probabilities.length; level++)
    {
      cumulativeProbability += probabilities[level];
      if (randomValue < cumulativeProbability)
      {
        return level + 1;
      }
    }
    
    throw new RuntimeException("Level not found");
  }
}
