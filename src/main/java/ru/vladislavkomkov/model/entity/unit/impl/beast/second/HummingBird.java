package ru.vladislavkomkov.model.entity.unit.impl.beast.second;

import java.util.List;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.player.Player;

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
  }
  
  @Override
  public Unit buildGold(Unit unit1, Unit unit2, Unit unit3)
  {
    Unit gold = super.buildGold(unit1, unit2, unit3);
    
    gold.setDescription("Start of Combat: For the rest of this combat, your Beasts have +2 Attack");
    return gold;
  }
  
  @Override
  public void onStartFight(Game game, Fight fight, Player player, Player player2)
  {
    fight.getFightTable(player).forEach(unit -> unit.incAttack(!isGold() ? ATTACK_BOOST : ATTACK_BOOST * 2));
    super.onStartFight(game, fight, player, player2);
  }
  
  @Override
  public void buildFace(Player player)
  {
    
  }
}
