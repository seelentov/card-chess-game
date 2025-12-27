package ru.vladislavkomkov.model.entity.unit.impl.beast.second;

import java.util.List;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.player.Player;

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
    
    listener.onStartTurnListeners.put(
        KEY_CORE,
        ((game, fight, player) -> fight.getFightTable(player).forEach(unit -> unit.incAttack(ATTACK_BOOST))));
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
    super.onStartFight(game, fight, player, player2);
  }
  
  @Override
  public void buildFace(Player player)
  {
    
  }
}
