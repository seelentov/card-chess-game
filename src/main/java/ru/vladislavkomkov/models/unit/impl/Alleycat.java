package ru.vladislavkomkov.models.unit.impl;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.models.unit.Type;
import ru.vladislavkomkov.models.unit.Unit;

public class Alleycat extends Unit {
    public Alleycat(){
        description = "Summon a 1/1 Cat";
        level = 1;
        isTavern = true;
        
        attack = 1;
        
        maxHealth = 1;
        actualHealth = 1;
        
        type = Type.BEAST;
    }

    public void onPlayed(Game game, Player player, int index) {
        super.onPlayed(game,player,index);
        player.addToTable(new Cat(), -1);
    }
}
