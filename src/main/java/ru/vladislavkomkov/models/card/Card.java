package ru.vladislavkomkov.models.card;

import java.io.Serializable;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.entity.Entity;
import ru.vladislavkomkov.models.entity.spell.Spell;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.player.Player;

public class Card<T extends Entity> implements Serializable
{
  private final T entity;
  
  public Card(T entity)
  {
    this.entity = entity;
  }
  
  public static <T extends Entity> Card<T> of(T entity)
  {
    return new Card<>(entity);
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
  
  public boolean isGold()
  {
    return entity.isGold();
  }
  
  public String getName()
  {
    return entity.getName();
  }
  
  public boolean isSpell()
  {
    return entity instanceof Spell;
  }
}
