package ru.vladislavkomkov.model.entity.unit.impl.beast.fifth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Mechorse;
import ru.vladislavkomkov.model.player.Player;

public class MechanizedGiftHorse extends Unit {
    public MechanizedGiftHorse()
    {
        this(DUMP_PLAYER);
    }
    
    public MechanizedGiftHorse(Player playerLink)
    {
        super(playerLink);
        
        attack = 4;
        
        maxHealth = 4;
        
        isTavern = true;
        
        level = 5;
        
        unitType = List.of(UnitType.MECH, UnitType.BEAST);
        
        listener.onDeadListeners.put(
                KEY_CORE,
                (game, fight, player1, player2, unit, attacker) -> {
                    if (fight != null)
                    {
                        for (int i = 0; i < 2; i++)
                        {
                            fight.addToFightTable(player1, isGold ? new Mechorse(player1).newGold() : new Mechorse(player1), unit, true);
                        }
                    }
                    else
                    {
                        int index = player1.getIndex(unit);
                        for (int i = 0; i < 2; i++)
                        {
                            player1.addToTable(isGold ? new Mechorse(player1).newGold() : new Mechorse(player1), index + 1, true);
                        }
                    }
                });
        
        actualHealth = getMaxHealth();
    }
    
    @Override
    public String getDescription() {
        Unit sub = isGold() ? new Mechorse(playerLink).buildGold() : new Mechorse(playerLink);
        return "Deathrattle: Summon two " + sub.getBaseAttack() + "/" + sub.getBaseHealth() + "Mechorses with '" + sub.getDescription() + "'";
    }
}
