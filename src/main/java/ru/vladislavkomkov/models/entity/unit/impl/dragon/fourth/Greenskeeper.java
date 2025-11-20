package ru.vladislavkomkov.models.entity.unit.impl.dragon.fourth;

import java.util.List;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.entity.unit.Type;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.util.RandUtils;

public class Greenskeeper extends Unit {
    public Greenskeeper(){
        description = "Rally: Trigger your right-most Battlecry";
        
        type = List.of(Type.DRAGON);
        
        attack = 4;
        
        maxHealth = 5;
        actualHealth = 5;
        
        level = 4;
    }
    
    @Override
    public void onAttack(Game game, Player player, Player player2, Unit attacked) {
        super.onAttack(game, player, player2, attacked);
        
        List<Unit> units = player.inFightTable != null ? player.inFightTable : player.cloneTable();
        for (int i = units.size() - 1; i >= 0; i--) {
            Unit unit = units.get(i);
            if(unit.isAnswerOnPlayed()){
                unit.onPlayed(game, player, RandUtils.getRand(units.size()), RandUtils.getRand(units.size()));
                break;
            }
        }
    }
}
