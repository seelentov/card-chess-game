package ru.vladislavkomkov.model.entity.unit.impl.beast.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Type;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cubling;

public class Manasaber extends Unit
{
  public Manasaber()
  {
    description = "Deathrattle: Summon two 0/1 Cublings with Taunt";
    level = 1;
    isTavern = true;
    
    attack = 4;
    
    maxHealth = 1;
    actualHealth = 1;
    
    type = List.of(Type.BEAST);
    
    answerOnDead = true;
    
    listener.onDeadListeners.put(
        KEY_CORE,
        (game, player, player2, unit, attacker) -> {
          if (player.inFight())
          {
            int index = player.getFightIndex(this);
            for (int i = 0; i < 2; i++)
            {
              player.addToFightTable(game, new Cubling(), index + 1, true);
            }
          }
          else
          {
            int index = player.getIndex(this);
            for (int i = 0; i < 2; i++)
            {
              player.addToTable(game, new Cubling(), index + 1);
            }
          }
        });
  }
  
  @Override
  public Unit buildGold(Unit unit, Unit unit2, Unit unit3)
  {
    Unit gold = super.buildGold(unit, unit2, unit3);
    
    gold.setDescription("Deathrattle: Summon two 0/2 Cublings with Taunt");
    gold.getListener().onDeadListeners.put(
        KEY_CORE,
        (game, player, player2, unit1, attacker) -> {
          if (player.inFight())
          {
            int index = player.getFightIndex(gold);
            for (int i = 0; i < 2; i++)
            {
              player.addToFightTable(game, new Cubling().newGold(), index + 1, true);
            }
          }
          else
          {
            int index = player.getIndex(gold);
            for (int i = 0; i < 2; i++)
            {
              player.addToTable(game, new Cubling().newGold(), index + 1);
            }
          }
        });
    
    return gold;
  }
  
}
