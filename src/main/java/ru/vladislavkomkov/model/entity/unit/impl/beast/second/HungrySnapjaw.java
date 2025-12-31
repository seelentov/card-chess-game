package ru.vladislavkomkov.model.entity.unit.impl.beast.second;

import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

import java.util.List;
import java.util.Optional;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

public class HungrySnapjaw extends Unit
{
  public static int HEALTH_BOOST = 1;
  
  public HungrySnapjaw()
  {
    this(DUMP_PLAYER);
  }
  
  public HungrySnapjaw(Player playerLink)
  {
    super(playerLink);
    
    attack = 4;
    
    level = 2;
    
    maxHealth = 2;
    
    isTavern = true;
    
    unitType = List.of(UnitType.BEAST);
    
    String tempKey = UUIDUtils.generateKey();
    
    getListener().onAppearListeners.put(
        KEY_CORE,
        (g, f, p, e, isSetup) -> {
          if(f == null){
            return;
          }
          f.getFightPlayer(p).getListener().onDeadListeners.put(
              tempKey,
              (game, fight, player1, player2, unit, attacker) -> {
                if (unit.isType(UnitType.BEAST) && !unit.getID().equals(getID()))
                {
                  if (fight != null)
                  {
                    Optional<Unit> hj = fight.getFightUnit(player1, ID);
                    if (hj.isPresent())
                    {
                      addBuff(hj.get());
                      
                      hj = player1.getTableUnit(ID);
                      hj.ifPresent(this::addBuff);
                    }
                  }
                  else
                  {
                    Optional<Unit> hj = player1.getTableUnit(ID);
                    hj.ifPresent(this::addBuff);
                  }
                }
              });
        });
    
    getListener().onDisappearListeners.put(
        KEY_CORE,
        (g, f, p, e) -> {
          if (f == null)
          {
            return;
          }
          f.getFightPlayer(p).getListener().onDeadListeners.remove(tempKey);
        });
    
    actualHealth = getMaxHealth();
  }
  
  @Override
  public String getDescription()
  {
    return "After a friendly Beast dies, gain +" + (HEALTH_BOOST * (isGold() ? 2 : 1)) + " Health permanently.";
  }
  
  private void addBuff(Unit unit)
  {
    unit.addBuff(new Buff(
        unit1 -> unit1.incHealth(HEALTH_BOOST * (isGold() ? 2 : 1)),
        null,
        getDescription()));
  }
}
