package ru.vladislavkomkov.model.entity.unit.impl.beast.sixth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;
import java.util.Optional;

import ru.vladislavkomkov.model.Listener;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

public class P0ULTR0N extends Unit
{
  public static final int AVENGE = 4;
  
  int avenge = 0;
  
  public P0ULTR0N()
  {
    this(DUMP_PLAYER);
  }
  
  public P0ULTR0N(Player playerLink)
  {
    super(playerLink);
    
    attack = 8;
    
    maxHealth = 8;
    
    isTavern = true;
    
    level = 6;
    
    unitType = List.of(UnitType.MECH, UnitType.BEAST);
    
    actualHealth = getMaxHealth();
    
    String tempKey = UUIDUtils.generateKey();
    
    getListener().onAppearListeners.put(
        KEY_CORE,
        (g, f, p, e) -> {
          Listener listener = f != null ? f.getFightPlayer(p).getListener() : p.getListener();
          
          listener.onDeadListeners.put(
              tempKey,
              (game, fight, player1, player2, unit, attacker) -> {
                if (unit.getID().equals(getID()) || fight == null)
                {
                  return;
                }
                
                avenge++;
                
                if (avenge == AVENGE)
                {
                  avenge = 0;
                  
                  Unit thisUnit = (Unit) e;
                  thisUnit.setIsBubbled(true);
                  
                  Optional<Unit> attackedOpt = fight.getRandAttackedUnit(player1);
                  if (attackedOpt.isPresent())
                  {
                    thisUnit.onAttack(game, fight, player1, player2, attackedOpt.get());
                    attackedOpt.get().onAttacked(game, fight, player2, player1, thisUnit);
                  }
                }
              });
        });
    
    getListener().onDisappearListeners.put(
        KEY_CORE,
        (g, f, p, e) -> {
          Listener listener = f != null ? f.getFightPlayer(p).getListener() : p.getListener();
          listener.onDeadListeners.remove(tempKey);
        });
  }
}
