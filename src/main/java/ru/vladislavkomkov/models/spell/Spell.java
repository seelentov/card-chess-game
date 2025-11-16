package ru.vladislavkomkov.models.spell;

import ru.vladislavkomkov.models.Entity;
import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.player.Player;

public abstract class Spell extends Entity {
    public abstract void cast(Game game, Player player, int index);
    
    public void onPlayed(Game game, Player player, int index) {
        super.onPlayed(game,player,index);
        this.cast(game,player,index);
    }
}
