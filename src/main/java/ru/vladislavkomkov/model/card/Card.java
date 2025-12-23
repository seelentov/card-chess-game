package ru.vladislavkomkov.model.card;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;

public class Card<T extends Entity>
{
  public final static String F_ENTITY = "entity";
  public final static String F_IS_SPELL = "is_spell";


  final T entity;
  
  public Card(T entity)
  {
    this.entity = entity;
  }
  
  public static <T extends Entity> Card<T> of(T entity)
  {
    return new Card<>(entity);
  }
  
  public boolean play(Game game, Player player, List<Integer> input)
  {
    if (entity instanceof Unit unit)
    {
      if(input.isEmpty()){
        return false;
      }
      
      if (player.addToTable(unit, input.get(0)))
      {
        entity.onPlayed(game, null, player, input, false);
      }
    }
    else
    {
      entity.onPlayed(game, null, player, input, false);
    }
    
    return true;
  }

  @JsonProperty(F_ENTITY)
  public T getEntity()
  {
    return entity;
  }
  
  @JsonProperty(F_IS_SPELL)
  public boolean isSpell()
  {
    return entity instanceof Spell;
  }
}
