package ru.vladislavkomkov.model.entity.unit;

public enum UnitType
{
  BEAST("Beast"),
  NAGA("Naga"),
  DRAGON("Dragon"),
  UNDEAD("Undead"),
  DEMON("Demon"),
  MECH("Mech"),
  ALL("All");
  
  final String type;
  
  UnitType(final String type)
  {
    this.type = type;
  }
  
  @Override
  public String toString()
  {
    return type;
  }
}
