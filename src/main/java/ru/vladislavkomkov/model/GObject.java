package ru.vladislavkomkov.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.vladislavkomkov.util.UUIDUtils;

public abstract class GObject implements Cloneable
{
  public final static String F_ID = "id";
  
  public final static String F_NAME = "name";
  public final static String F_DESCRIPTION = "description";
  
  public final static String F_IS_GOLD = "is_gold";
  
  protected Boolean isGold = false;
  
  protected String ID = UUIDUtils.generateKey();
  protected String name = this.getClass().getSimpleName();
  
  public GObject()
  {
  }
  
  public GObject(boolean isGold)
  {
    this.isGold = isGold;
  }
  
  @JsonProperty(F_NAME)
  public String getName()
  {
    return name;
  }
  
  @JsonProperty(F_ID)
  public String getID()
  {
    return ID;
  }
  
  protected String generateKey()
  {
    return UUIDUtils.generateKey(getID() + "_");
  }
  
  @JsonProperty(F_DESCRIPTION)
  public abstract String getDescription();
  
  @JsonProperty(F_IS_GOLD)
  public Boolean isGold()
  {
    return isGold;
  }
  
  public void setIsGold(boolean isGold)
  {
    this.isGold = isGold;
  }
  
  @Override
  public GObject clone()
  {
    try
    {
      return (GObject) super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      throw new AssertionError("Clone not supported", e);
    }
  }
}
