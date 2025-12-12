package ru.vladislavkomkov.model.entity.unit;

import java.util.function.Consumer;

public class Buff
{
  final String description;
  
  Consumer<Unit> upgrade = unit -> {
  };
  Consumer<Unit> rollback = unit -> {
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
