package ru.vladislavkomkov.model.entity.unit.impl.beast.fifth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.AttacksCount;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;

public class LightfeatherScreecher extends Unit
{
  public LightfeatherScreecher()
  {
    this(DUMP_PLAYER);
  }
  
  public LightfeatherScreecher(Player playerLink)
  {
    super(playerLink);
    
    attack = 6;
    
    maxHealth = 6;
    
    isTavern = true;
    
    level = 5;
    
    isTaunt = true;
    
    unitType = List.of(UnitType.BEAST);
    
    getListener().onStartFightListeners.put(
        KEY_CORE,
        (game, fight, player, player2) -> {
          if (fight == null)
          {
            return;
          }
          
          List<Unit> units = fight.getFightTable(player).stream().filter(unit -> unit.isType(UnitType.BEAST)).toList();
          
          if (!units.isEmpty())
          {
            addBuff(units.get(0));
          }
          
          if (isGold() && units.size() > 1)
          {
            addBuff(units.get(1));
          }
        });
    
    actualHealth = getMaxHealth();
  }
  
  private void addBuff(Unit unit)
  {
    unit.addBuff(new Buff(
        unit1 -> {
          unit1.setAttacksCount(AttacksCount.DOUBLE);
          unit1.setIsBubbled(true);
        },
        null,
        getDescription(),
        getID()));
  }
  
  @Override
  public String getDescription()
  {
    return "Taunt\nStart of Combat: Give your " + (isGold() ? "2" : "") + " left-most Beasts Windfury and Divine Shield";
  }
}
