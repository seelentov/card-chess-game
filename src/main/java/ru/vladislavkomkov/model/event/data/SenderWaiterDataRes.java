package ru.vladislavkomkov.model.event.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SenderWaiterDataRes
{
  public final static String F_KEY = "key";
  public final static String F_PARAM = "param";
  
  
  String key;
  int param;
  
  public SenderWaiterDataRes()
  {
  }
  
  public SenderWaiterDataRes(String key, int param)
  {
    this.key = key;
    this.param = param;
  }
  
  @JsonProperty(F_KEY)
  public String getKey()
  {
    return key;
  }
  
  public void setKey(String key)
  {
    this.key = key;
  }
  
  @JsonProperty(F_PARAM)
  public int getParam()
  {
    return param;
  }
  
  public void setParam(int param)
  {
    this.param = param;
  }
}
