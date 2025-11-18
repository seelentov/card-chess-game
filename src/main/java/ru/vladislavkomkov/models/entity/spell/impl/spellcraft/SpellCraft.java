package ru.vladislavkomkov.models.entity.spell.impl.spellcraft;

import ru.vladislavkomkov.models.entity.spell.Spell;
import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.util.ListenerUtils;

public abstract class SpellCraft extends Spell {
    public SpellCraft(){
        level = 0;
    }
    
    protected void boostOneFight(int attackBonus, int hpBonus, Player player, int index){
        player.doFor(unit -> {
            unit.incAttack(attackBonus);
            unit.incHealth(hpBonus);
        }, index);
        
        player.listener.onStartTurnListeners.put(
                ListenerUtils.generateKeyOnce(),
                (game1, player1) -> player.doFor(unit -> {
                    unit.decAttack(attackBonus);
                    unit.decHealth(hpBonus);
                }, index)
        );
    }
}
