package ru.vladislavkomkov.model.entity.unit;

import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.entity.PlayType;
import ru.vladislavkomkov.model.player.Player;

public class Choicer extends Unit
{
  public Choicer()
  {
    super();
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
  
  @Override
  public void buildFace(Player player)
  {
    super.buildFace(player);
    
    playType.stream()
        .filter(playPair -> playPair.getType().equals(PlayType.CHOICE))
        .forEach(playPair -> playPair.getChoice().forEach(choice -> choice.buildFace(player)));
  }
}
