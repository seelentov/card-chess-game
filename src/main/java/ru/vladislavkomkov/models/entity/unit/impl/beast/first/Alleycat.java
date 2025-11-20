package ru.vladislavkomkov.models.entity.unit.impl.beast.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.List;

import ru.vladislavkomkov.models.entity.unit.Type;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.entity.unit.impl.trash.beast.first.Cat;

public class Alleycat extends Unit
{
  public Alleycat()
  {
    description = "Summon a 1/1 Cat";
    level = 1;
    isTavern = true;
    
    attack = 1;
    
    maxHealth = 1;
    actualHealth = 1;
    
    type = List.of(Type.BEAST);
    
    answerOnPlayed = true;
    
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, player, entity, index, isTavernIndex, index2, isTavernIndex2) -> {
          if (player.inFightTable != null)
          {
            int indexParent = player.getFightIndex(this);
            player.addToFightTable(new Cat(), indexParent + 1);
          }
          else
          {
            int indexParent = player.getIndex(this);
            player.addToTable(new Cat(), indexParent + 1);
          }
        });
  }
}
