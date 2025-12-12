package ru.vladislavkomkov.model.entity.unit.impl.beast.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Type;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

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
    
    isAnswerOnPlayed = true;
    
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, player, entity, index, isTavernIndex, index2, isTavernIndex2, auto) -> {
          if (player.inFight())
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
  
  @Override
  public Unit buildGold(Unit unit, Unit unit2, Unit unit3)
  {
    Unit gold = super.buildGold(unit, unit2, unit3);
    
    gold.setDescription("Summon a 2/2 Cat");
    gold.getListener().onPlayedListeners.put(
        KEY_CORE,
        (game, player, entity, index, isTavernIndex, index2, isTavernIndex2, auto) -> {
          if (player.inFight())
          {
            int indexParent = player.getFightIndex(gold);
            player.addToFightTable(new Cat().newGold(), indexParent + 1);
          }
          else
          {
            int indexParent = player.getIndex(gold);
            player.addToTable(new Cat().newGold(), indexParent + 1);
          }
        });
    
    return gold;
  }
}
