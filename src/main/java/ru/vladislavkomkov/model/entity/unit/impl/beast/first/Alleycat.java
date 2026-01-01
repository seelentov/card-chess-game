package ru.vladislavkomkov.model.entity.unit.impl.beast.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.player.Player;

public class Alleycat extends Unit
{
  public Alleycat()
  {
    this(DUMP_PLAYER);
  }
  
  public Alleycat(Player playerLink)
  {
    super(playerLink);
    
    level = 1;
    isTavern = true;
    
    attack = 1;
    
    maxHealth = 1;
    
    unitType = List.of(UnitType.BEAST);
    
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player1, entity, input, auto) -> {
          if (fight != null)
          {
            fight.addToFightTable(player1, isGold ? new Cat(player1).buildGold() : new Cat(player1), (Unit) entity, false);
          }
          else
          {
            int indexParent = player1.getIndex((Unit) entity);
            player1.addToTable(isGold ? new Cat(player1).buildGold() : new Cat(player1), indexParent + 1);
          }
        });
    
    actualHealth = getMaxHealth();
  }
  
  @Override
  public String getDescription()
  {
    Unit sub = isGold ? new Cat(playerLink).buildGold() : new Cat(playerLink);
    return "Summon a " + sub.getAttack() + "/" + sub.getHealth() + " Cat";
  }
}
