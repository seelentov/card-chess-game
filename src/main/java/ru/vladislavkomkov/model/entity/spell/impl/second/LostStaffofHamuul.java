package ru.vladislavkomkov.model.entity.spell.impl.second;

import static java.util.List.of;
import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;
import static ru.vladislavkomkov.model.entity.PlayType.TAVERN_FRIENDLY;

import java.util.List;
import java.util.Optional;

import ru.vladislavkomkov.model.entity.PlayPair;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.ReflectUtils;

public class LostStaffofHamuul extends Spell
{
  public LostStaffofHamuul()
  {
    this(DUMP_PLAYER, false);
  }
  
  public LostStaffofHamuul(Player playerLink)
  {
    this(playerLink, false);
  }
  
  public LostStaffofHamuul(Player playerLink, boolean isGold)
  {
    super(playerLink, isGold);
    isTavern = true;
    
    playType = of(new PlayPair(TAVERN_FRIENDLY, List.of(UnitType.values())));
  }
  
  @Override
  public void build()
  {
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          Optional<Unit> unit = getUnitFromTavernFriendlyInput(fight, player, input);
          
          List<Class<? extends Unit>> pool;
          if (unit.isPresent() && !unit.get().getType().isEmpty())
          {
            pool = player.getTavern().getUnitsPool().stream()
                .filter(u -> {
                  Unit un = ReflectUtils.getInstance(u);
                  return un.isType(unit.get().getType()) && un.getLevel() <= player.getLevel();
                })
                .toList();
          }
          else
          {
            pool = player.getTavern().getUnitsPool();
          }
          
          player.getTavern().reset(player.getLevel(), false, pool);
        });
  }
  
  @Override
  public String getDescription()
  {
    return "Choose a minion. Refresh the Tavern with minions of its type";
  }
}
