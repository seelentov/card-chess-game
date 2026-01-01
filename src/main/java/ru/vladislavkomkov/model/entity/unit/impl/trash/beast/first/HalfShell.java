package ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;

public class HalfShell extends Unit
{
  public HalfShell()
  {
    this(DUMP_PLAYER);
  }
  
  public HalfShell(Player playerLink)
  {
    super(playerLink);
    
    attack = 2;
    maxHealth = 3;
    
    isTaunt = true;
    
    level = 1;
    
    unitType = List.of(UnitType.BEAST);
    
    actualHealth = getMaxHealth();
  }
}