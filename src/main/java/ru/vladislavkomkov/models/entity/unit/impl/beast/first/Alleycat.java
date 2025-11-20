package ru.vladislavkomkov.models.entity.unit.impl.beast.first;

import java.util.Arrays;
import java.util.List;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.models.entity.unit.Type;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.entity.unit.impl.trash.beast.first.Cat;

public class Alleycat extends Unit {
    public Alleycat(){
        description = "Summon a 1/1 Cat";
        level = 1;
        isTavern = true;
        
        attack = 1;
        
        maxHealth = 1;
        actualHealth = 1;
        
        type = List.of(Type.BEAST);
    }

    public void onPlayed(Game game, Player player, int index) {
        super.onPlayed(game,player,index);
        if(player.inFightTable != null && player.getFightUnitsCount() < 7){
            player.inFightTable.add(index + 1, new Cat());
        } else if (player.getUnitsCount() < 7) {
            player.addToTable(new Cat(),index + 1);
        }
    }
}
