package ru.vladislavkomkov.model.entity.unit.impl.beast.second;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Type;
import ru.vladislavkomkov.model.entity.unit.Unit;

public class HummingBird extends Unit
{
  public static final int ATTACK_BOOST = 1;
  
  public HummingBird()
  {
    super();
    
    description = "Start of Combat: For the rest of this combat, your Beasts have +1 Attack";
    attack = 1;
    
    level = 2;
    
    maxHealth = 4;
    actualHealth = 4;
    
    type = List.of(Type.BEAST);
    
    listener.onStartFightListeners.put(
        KEY_CORE,
        (game, fight, player, player2) -> {
          if (fight == null)
          {
            return;
          }
          for (Unit unit : fight.getFightTable(player))
          {
            if (unit.isType(Type.BEAST))
            {
              unit.incAttack(ATTACK_BOOST);
            }
          }
        });
  }
  
  @Override
  public Unit buildGold(Unit unit1, Unit unit2, Unit unit3)
  {
    Unit gold = super.buildGold(unit1, unit2, unit3);
    
    gold.setDescription("Start of Combat: For the rest of this combat, your Beasts have +2 Attack");
    gold.getListener().onStartFightListeners.put(
        KEY_CORE,
        (game, fight, player, player2) -> {
          if (fight == null)
          {
            return;
          }
          for (Unit unit : fight.getFightTable(player))
          {
            if (unit.isType(Type.BEAST))
            {
              unit.incAttack(ATTACK_BOOST * 2);
            }
          }
        });
    
    return gold;
  }
}
