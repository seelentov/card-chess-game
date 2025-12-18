package ru.vladislavkomkov.model.event.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SenderWaiterDataReq
{
  public final static String F_KEY = "key";
  public final static String F_DATA = "data";
  
  
  String key;
  Object data;
  
  public SenderWaiterDataReq()
  {
  }
  
  public SenderWaiterDataReq(String key, Object data)
  {
    this.key = key;
    this.data = data;
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
  
  @JsonProperty(F_DATA)
  public Object getData()
  {
    return data;
  }
  
  public void setData(Object data)
  {
    this.data = data;
  }
}
