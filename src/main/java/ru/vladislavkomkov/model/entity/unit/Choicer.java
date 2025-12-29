package ru.vladislavkomkov.model.entity.unit;

import ru.vladislavkomkov.model.entity.PlayType;
import ru.vladislavkomkov.model.player.Player;

public abstract class Choicer extends Unit
{
  public Choicer(Player playerLink)
  {
    super(playerLink);
  }
  
  @Override
  public Unit buildGold(Unit unit, Unit unit2, Unit unit3)
  {
    Unit entity = super.buildGold(unit, unit2, unit3);
    
    entity.getPlayUnitType().stream()
        .filter(playPair -> playPair.getType().equals(PlayType.CHOICE))
        .forEach(playPair -> playPair.getChoice().forEach(choice -> choice.setIsGold(true)));
    
    return entity;
  }
}
