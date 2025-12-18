package ru.vladislavkomkov.model.entity.unit;

import java.util.function.Consumer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Buff implements Cloneable
{
  public final static String F_DESCRIPTION = "description";
  
  final String description;
  
  final Consumer<Unit> upgrade;
  final Consumer<Unit> rollback;
  
  public Buff(Consumer<Unit> upgrade, Consumer<Unit> rollback)
  {
    this(upgrade, rollback, "");
  }
  
  public Buff(Consumer<Unit> upgrade, Consumer<Unit> rollback, String description)
  {
    this.upgrade = upgrade;
    this.rollback = rollback;
    this.description = description;
  }
  
  @JsonProperty(F_DESCRIPTION)
  public String getDescription()
  {
    return description;
  }
  
  public Consumer<Unit> getUpgrade()
  {
    return upgrade;
  }
  
  public Consumer<Unit> getRollback()
  {
    return rollback;
  }
  
  @Override
  public Buff clone()
  {
    try
    {
      return new Buff(this.upgrade, this.rollback, this.description);
    }
    catch (Exception e)
    {
      throw new AssertionError("Unexpected error during cloning Buff", e);
    }
  }
}
