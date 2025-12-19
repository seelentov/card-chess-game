package ru.vladislavkomkov.model.card;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;

public class Card<T extends Entity>
{
  public final static String F_ENTITY = "entity";


  final T entity;
  
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
    entity.onPlayed(game, null, player, index, isTavernIndex, index2, isTavernIndex2, false);
  }

  @JsonProperty(F_ENTITY)
  public T getEntity()
  {
    return entity;
  }
  
  public boolean isSpell()
  {
    return entity instanceof Spell;
  }
}
