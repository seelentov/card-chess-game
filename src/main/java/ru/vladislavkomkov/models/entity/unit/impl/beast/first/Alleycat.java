package ru.vladislavkomkov.models.entity.unit.impl.beast.first;

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
        
        answerOnPlayed = true;
    }

    public void onPlayed(Game game, Player player, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2) {
        super.onPlayed(game,player,index, isTavernIndex, index2, isTavernIndex2);
        if(player.inFightTable != null){
            int indexParent = player.getFightIndex(this);
            player.addToFightTable(new Cat(), indexParent + 1);
        } else {
            int indexParent = player.getIndex(this);
            player.addToTable(new Cat(),indexParent + 1);
        }
    }
    
    public class Gold extends Alleycat{
        public Gold(){
        
        }
    }
    
    public Gold getGold() {
        return new Gold();
    }
}
