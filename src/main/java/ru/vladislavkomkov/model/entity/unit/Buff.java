package ru.vladislavkomkov.model.entity.unit;

import java.util.function.Consumer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Buff implements Cloneable
{
  public final static String F_DESCRIPTION = "description";
  
  final String description;
  
  final Consumer<Unit> upgrade;
  final Consumer<Unit> rollback;
  
  final String creator;
  final boolean isTemp;
  
  public Buff(String description)
  {
    this(description, false);
  }
  
  public Buff(String description, boolean rollback)
  {
    this(unit -> {
    }, rollback ? unit -> {
    } : null, description, null, false);
  }
  
  public Buff(Consumer<Unit> upgrade, Consumer<Unit> rollback, String description, String creator)
  {
    this(upgrade, rollback, description, creator, false);
  }
  
  public Buff(Consumer<Unit> upgrade, Consumer<Unit> rollback, String creator)
  {
    this(upgrade, rollback, "", creator, false);
  }
  
  public Buff(Consumer<Unit> upgrade, Consumer<Unit> rollback, String creator, boolean isTemp)
  {
    this(upgrade, rollback, "", creator, isTemp);
  }
  
  public Buff(Consumer<Unit> upgrade, String description, String creator, boolean isTemp)
  {
    this(upgrade, null, description, creator, isTemp);
  }
  
  public Buff(Consumer<Unit> upgrade, Consumer<Unit> rollback, String description, String creator, boolean isTemp)
  {
    this.upgrade = upgrade;
    this.rollback = rollback;
    this.description = description;
    this.creator = creator;
    this.isTemp = isTemp;
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
  
  public String getCreator()
  {
    return creator;
  }
  
  public boolean isTemp()
  {
    return isTemp;
  }
  
  @Override
  public Buff clone()
  {
    try
    {
      return new Buff(this.upgrade, this.rollback, this.description, this.creator, this.isTemp);
    }
    catch (Exception e)
    {
      throw new AssertionError("Unexpected error during cloning Buff", e);
    }
  }
}
