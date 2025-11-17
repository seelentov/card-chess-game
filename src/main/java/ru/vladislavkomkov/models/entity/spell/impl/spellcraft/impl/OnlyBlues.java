package ru.vladislavkomkov.models.entity.spell.impl.spellcraft.impl;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.entity.spell.Spell;
import ru.vladislavkomkov.models.entity.spell.impl.spellcraft.SpellCraft;
import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.util.ListenerUtils;

public class OnlyBlues extends SpellCraft {
    public static final int ATTACK_BOOST = 2;
    public static final int HEALTH_BOOST = 3;
    
    public OnlyBlues(){
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
        
        player.doFor(unit -> {
            unit.incAttack(attackBonus);
            unit.incHealth(hpBonus);
        }, index);
        
        player.listener.onStartTurnListeners.put(
                ListenerUtils.generateKeyOneUse(),
                (game1, player1) -> player.doFor(unit -> {
                    unit.decAttack(attackBonus);
                    unit.decHealth(hpBonus);
                    }, index)
        );
        
        player.statistic.onlyBluesPlayed++;
    }
}
