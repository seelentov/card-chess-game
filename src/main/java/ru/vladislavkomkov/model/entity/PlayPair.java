package ru.vladislavkomkov.model.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.unit.UnitType;

public class PlayPair
{
  public final static String F_TYPE = "type";
  public final static String F_UNIT_TYPES = "unit_types";
  public final static String F_CHOICE = "choice";
  
  private final PlayType type;
  private final List<UnitType> unitTypes;
  private final List<Choice> choice;
  
  public PlayPair(PlayType type)
  {
    this.type = type;
    this.choice = List.of();
    this.unitTypes = List.of();
  }
  
  public PlayPair(PlayType type, List unitTypesOrChoice)
  {
    this.type = type;
    
    if (!unitTypesOrChoice.isEmpty())
    {
      if (unitTypesOrChoice.get(0) instanceof UnitType)
      {
        this.unitTypes = unitTypesOrChoice;
        this.choice = List.of();
      }
      else
      {
        this.choice = unitTypesOrChoice;
        this.unitTypes = List.of();
      }
    }
    else
    {
      this.choice = List.of();
      this.unitTypes = List.of();
    }
  }
  
  @JsonProperty(F_TYPE)
  public PlayType getType()
  {
    return type;
  }
  
  @JsonProperty(F_UNIT_TYPES)
  public List<UnitType> getUnitTypes()
  {
    return unitTypes;
  }
  
  @JsonProperty(F_CHOICE)
  public List<Choice> getChoice()
  {
    return choice;
  }
}
