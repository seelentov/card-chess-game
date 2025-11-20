package ru.vladislavkomkov.models.card;

import java.io.Serializable;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.entity.Entity;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.player.Player;

public class Card implements Serializable
{
  private Entity entity;
  
  public Card(Entity entity)
  {
    this.entity = entity;
  }
  
  public static Card of(Entity entity)
  {
    return new Card(entity);
  }
  
  public void play(Game game, Player player, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2)
  {
    if (entity instanceof Unit unit)
    {
      player.addToTable(unit, index);
    }
    entity.onPlayed(game, player, index, isTavernIndex, index2, isTavernIndex2);
  }
  
  public Entity get()
  {
    return entity;
  }
}
