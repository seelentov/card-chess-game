package ru.vladislavkomkov;

public class Server
{
  public static void main(String[] args)
  {
    new Child().cast();
  }
  
  public static class Parent
  {
    public void cast()
    {
      cast("hey!");
    }
    
    public void cast(String string)
    {
      System.out.println("Parent say: " + string);
    }
  }
  
  public static class Child extends Parent
  {
    @Override
    public void cast(String string)
    {
      System.out.println("Child say: " + string);
    }
  }
}