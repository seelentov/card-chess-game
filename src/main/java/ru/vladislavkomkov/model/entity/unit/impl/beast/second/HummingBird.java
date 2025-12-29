package ru.vladislavkomkov.model.entity.unit.impl.beast.second;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

public class HummingBird extends Unit
{
  public static final int ATTACK_BOOST = 1;
  
  public HummingBird()
  {
    this(DUMP_PLAYER);
  }
  
  public HummingBird(Player playerLink)
  {
    super(playerLink);
    
    attack = 1;
    
    level = 2;
    
    maxHealth = 4;
    
    isTavern = true;
    
    unitType = List.of(UnitType.BEAST);
    
    listener.onStartFightListeners.put(
        KEY_CORE,
        ((game, fight, player1, player2) -> {
          if (fight == null)
          {
            return;
          }
          
          fight.getFightTable(player1).forEach(unit -> addBuff(unit, ATTACK_BOOST * (isGold ? 2 : 1)));
          
          fight.getFightPlayer(player1).getListener().onAppearListeners.put(
              UUIDUtils.generateKey(),
              (game1, fight1, playeer, entity) -> {
                if (entity instanceof Unit unit && unit.isType(UnitType.BEAST))
                {
                  addBuff(unit, ATTACK_BOOST * (isGold ? 2 : 1));
                }
              });
        }));
    
    actualHealth = getMaxHealth();
  }
  
  @Override
  public String getDescription()
  {
    return "Start of Combat: For the rest of this combat, your Beasts have " + (ATTACK_BOOST * (isGold ? 2 : 1)) + " Attack";
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
