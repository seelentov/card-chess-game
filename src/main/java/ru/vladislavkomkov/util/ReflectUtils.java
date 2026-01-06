package ru.vladislavkomkov.util;

import ru.vladislavkomkov.model.player.Player;

public class ReflectUtils
{
  public static <T> T getInstance(Class<T> clazz)
  {
    try
    {
      return clazz.getDeclaredConstructor().newInstance();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public static <T> T getInstance(Class<T> clazz, Player player)
  {
    try
    {
      return clazz.getDeclaredConstructor(Player.class).newInstance(player);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
