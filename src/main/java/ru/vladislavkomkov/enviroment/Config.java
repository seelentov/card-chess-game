package ru.vladislavkomkov.enviroment;

public class Config
{
  private static Config instance;
  
  private boolean isDebug = false;
  
  public static synchronized Config getInstance()
  {
    if (instance == null)
    {
      instance = new Config();
    }
    return instance;
  }
  
  public boolean isDebug()
  {
    return isDebug;
  }
  
  public void setDebug(boolean debug)
  {
    isDebug = debug;
  }
}
