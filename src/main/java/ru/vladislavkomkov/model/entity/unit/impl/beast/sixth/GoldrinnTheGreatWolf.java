package ru.vladislavkomkov.model.entity.unit.impl.beast.sixth;

import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

import java.util.List;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

public class GoldrinnTheGreatWolf extends Unit
{
  public static int ATTACK_BOOST = 6;
  public static int HEALTH_BOOST = 6;
  
  public GoldrinnTheGreatWolf()
  {
    this(DUMP_PLAYER);
  }
  
  public GoldrinnTheGreatWolf(Player playerLink)
  {
    super(playerLink);
    
    attack = 8;
    
    maxHealth = 8;
    
    isTavern = true;
    
    level = 6;
    
    unitType = List.of(UnitType.BEAST);
    
    getListener().onDeadListeners.put(
        KEY_CORE,
        (game, fight, player1, player2, unit, attacker) -> {
          if (fight == null)
          {
            return;
          }
          
          fight.getFightTable(player1).forEach(unit1 -> {
            if (!unit1.isType(UnitType.BEAST))
            {
              return;
            }
              int attack = ATTACK_BOOST * (isGold() ? 2 : 1);
              int health = HEALTH_BOOST * (isGold() ? 2 : 1);

              addBuff(unit1, attack, health);
          });

            fight.getFightPlayer(player1).getListener().onSummonedListeners.put(
                    UUIDUtils.generateKey(),
                    (game1, fight1, playeer, entity) -> {
                        int attack = ATTACK_BOOST * (isGold() ? 2 : 1);
                        int health = HEALTH_BOOST * (isGold() ? 2 : 1);
                        
                        if (entity instanceof Unit unit1 && unit.isType(UnitType.BEAST))
                        {
                            addBuff(unit1, attack, health);
                        }
                    });
        });
    
    actualHealth = getMaxHealth();
  }
  
  private void addBuff(Unit unit, int attackBoost, int healthBoost)
  {
    if (unit.getBuffs().stream().anyMatch(buff -> buff.getCreator().equals(getID())))
    {
      return;
    }
    
    unit.addBuff(new Buff(
        unit1 -> {
          unit1.incBaseAttack(attackBoost);
          unit1.incHealth(healthBoost);
        },
        unit1 -> {
          unit1.decBaseAttack(attackBoost);
          unit1.decHealth(healthBoost);
        },
        getDescription(),
        getID(),
        true));
  }
  
  @Override
  public String getDescription()
  {
    int attack = ATTACK_BOOST * (isGold() ? 2 : 1);
    int health = HEALTH_BOOST * (isGold() ? 2 : 1);
    
    return "Deathrattle: For the rest of this combat, your Beasts have +" + attack + "/+" + health + ".";
  }
}
