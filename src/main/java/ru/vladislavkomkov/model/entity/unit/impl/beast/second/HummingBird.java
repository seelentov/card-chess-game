package ru.vladislavkomkov.model.entity.unit.impl.beast.second;

import java.util.List;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

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
    
    isTavern = true;
    
    unitType = List.of(UnitType.BEAST);
    
    listener.onStartFightListeners.put(
        KEY_CORE,
        ((game, fight, player, player2) -> {
          if (fight == null)
          {
            return;
          }
          
          fight.getFightTable(player).forEach(unit -> addBuff(unit, ATTACK_BOOST));
          
          fight.getFightPlayer(player).getListener().onAppearListeners.put(
              UUIDUtils.generateKey(),
              (game1, fight1, player1, entity) -> {
                if (entity instanceof Unit unit && unit.isType(UnitType.BEAST))
                {
                  addBuff(unit, ATTACK_BOOST);
                }
              });
        }));
  }
  
  @Override
  public Unit buildGold(Unit unit1, Unit unit2, Unit unit3)
  {
    Unit gold = super.buildGold(unit1, unit2, unit3);
    gold.setDescription("Start of Combat: For the rest of this combat, your Beasts have +2 Attack");
    
    gold.getListener().onStartFightListeners.put(
        KEY_CORE,
        ((game, fight, player, player2) -> {
          if (fight == null)
          {
            return;
          }
          
          fight.getFightTable(player).forEach(unit -> addBuff(unit, ATTACK_BOOST * 2));
          
          fight.getFightPlayer(player).getListener().onAppearListeners.put(
              UUIDUtils.generateKey(),
              (game1, fight1, player1, entity) -> {
                if (entity instanceof Unit unit)
                {
                  if (unit.isType(UnitType.BEAST))
                  {
                    addBuff(unit, ATTACK_BOOST * 2);
                  }
                }
              });
              
        }));
    return gold;
  }
  
  @Override
  public void buildFace(Player player)
  {
    
  }
  
  private void addBuff(Unit unit, int attackBoost)
  {
    unit.addBuff(new Buff(
        unit1 -> {
          unit1.incAttack(attackBoost);
        },
        unit1 -> {
          unit1.decAttack(attackBoost);
        },
        getDescription()));
  }
}
