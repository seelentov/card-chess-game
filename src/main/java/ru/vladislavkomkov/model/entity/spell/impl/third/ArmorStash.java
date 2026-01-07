package ru.vladislavkomkov.model.entity.spell.impl.third;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.player.Player;

public class ArmorStash extends Spell
{
  public final static int ARMOR = 5;
  
  public ArmorStash()
  {
    this(DUMP_PLAYER, false);
  }
  
  public ArmorStash(Player playerLink)
  {
    this(playerLink, false);
  }
  
  public ArmorStash(Player playerLink, boolean isGold)
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
          player.setArmor(ARMOR);
        });
  }
  
  @Override
  public String getDescription()
  {
    return "Set your armor to " + ARMOR;
  }
}
