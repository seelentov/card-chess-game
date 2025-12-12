package ru.vladislavkomkov.model.entity.unit;

import java.util.function.Consumer;

public class Buff
{
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
}
