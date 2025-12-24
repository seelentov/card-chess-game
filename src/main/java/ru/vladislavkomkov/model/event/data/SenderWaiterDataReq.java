package ru.vladislavkomkov.model.event.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.Entity;

import java.util.List;

public class SenderWaiterDataReq<T extends Entity>
{
  public final static String F_KEY = "key";
  public final static String F_DATA = "data";
  
  
  String key;
  List<Card<T>> data;
  
  public SenderWaiterDataReq()
  {
  }

  public SenderWaiterDataReq(String key, List<Card<T>> data)
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
  public List<Card<T>> getData()
  {
    return data;
  }
  
  public void setData(List<Card<T>> data)
  {
    this.data = data;
  }
}
