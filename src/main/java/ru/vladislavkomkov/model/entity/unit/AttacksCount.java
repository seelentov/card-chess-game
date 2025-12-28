package ru.vladislavkomkov.model.entity.unit;

public enum AttacksCount
{
  DEFAULT(1),
  DOUBLE(2);
  
  final int attacksCount;
  
  AttacksCount(final int attacksCount)
  {
    this.attacksCount = attacksCount;
  }
  
  public int toInt()
  {
    return attacksCount;
  }
}
