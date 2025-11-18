package ru.vladislavkomkov.models.entity.spell.impl.spellcraft.impl;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.entity.spell.impl.spellcraft.SpellCraft;
import ru.vladislavkomkov.models.player.Player;

public class DeepBlues extends SpellCraft {
    public static final int ATTACK_BOOST = 2;
    public static final int HEALTH_BOOST = 3;
    
    public DeepBlues(){
        super();
    }
    
    int calcMulti(Player player){
        int casted = player.statistic.onlyBluesPlayed;
        return casted + 1;
    }
    
    @Override
    public void cast(Game game, Player player, int index) {
        int multi = calcMulti(player);
        int attackBonus = ATTACK_BOOST * multi;
        int hpBonus = HEALTH_BOOST * multi;
        
        boostOneFight(attackBonus,hpBonus,player,index);
        
        player.statistic.onlyBluesPlayed++;
    }
}
