package ru.vladislavkomkov.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.util.Pair;

public class RandUtilsTest
{
  @Test
  void testGetRand()
  {
    List<Pair<Integer, Integer>> cases = new ArrayList<>();
    
    for (int i = 0; i < 10; i++)
    {
      for (int j = 0; j < 10; j++)
      {
        if (i < j)
        {
          cases.add(new Pair<>(i, j));
        }
      }
    }
    
    for (Pair<Integer, Integer> c : cases)
    {
      int min = c.getKey();
      int max = c.getValue();
      
      int i = RandUtils.getRand(min, max);
      assertTrue(i <= max && i >= min);
    }
  }
  
  @Test
  void testGetRandLvl()
  {
    for (int i = 0; i < 1000; i++)
    {
      int lvl = RandUtils.getRand(1, 6);
      int rLvl = RandUtils.getRandLvl(lvl);
      assertTrue(rLvl <= lvl);
    }
  }
}
