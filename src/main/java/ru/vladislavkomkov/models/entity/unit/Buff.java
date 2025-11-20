package ru.vladislavkomkov.models.entity.unit;

import java.io.Serializable;
import java.util.function.Consumer;

public class Buff implements Serializable
{
  private final String description;
  private transient Consumer<Unit> upgrade = unit -> {
  };
  private transient Consumer<Unit> rollback = unit -> {
  };
  
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
  
  public Consumer<Unit> getUpgrade()
  {
    return upgrade;
  }
  
  public Consumer<Unit> getRollback()
  {
    return rollback;
  }
  
  public String getDescription()
  {
    return description;
  }
}
