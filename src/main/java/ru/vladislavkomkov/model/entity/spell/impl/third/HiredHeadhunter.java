package ru.vladislavkomkov.model.entity.spell.impl.third;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.ReflectUtils;

public class HiredHeadhunter extends Spell
{
  public HiredHeadhunter()
  {
    this(DUMP_PLAYER, false);
  }
  
  public HiredHeadhunter(Player playerLink)
  {
    this(playerLink, false);
  }
  
  public HiredHeadhunter(Player playerLink, boolean isGold)
  {
    super(playerLink, isGold);
    isTavern = true;
    
    level = 3;
    
    playType = List.of();
  }
  
  @Override
  public void build()
  {
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          List<Class<? extends Unit>> allUnits = player.getTavern().getUnitsPool().stream()
              .filter(u -> {
                Unit un = ReflectUtils.getInstance(u);
                return un.isAnswerOnPlayed() && un.getLevel() <= player.getLevel();
              })
              .toList();
              
          excavation(player, allUnits);
        });
  }
  
  @Override
  public String getDescription()
  {
    return "Discover a Battlecry minion";
  }
}
