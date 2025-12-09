package ru.vladislavkomkov.model.event.data;

public class SenderWaiterData
{
  private String key;
  private int param;
  
  public SenderWaiterData(String key, int param)
  {
    this.key = key;
    this.param = param;
  }
  
  public String getKey()
  {
    return key;
  }
  
  public void setKey(String key)
  {
    this.key = key;
  }
  
  public int getParam()
  {
    return param;
  }
  
  public void setParam(int param)
  {
    this.param = param;
  }
}
