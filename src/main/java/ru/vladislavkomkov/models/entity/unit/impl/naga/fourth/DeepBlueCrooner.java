package ru.vladislavkomkov.models.entity.unit.impl.naga.fourth;

import ru.vladislavkomkov.models.entity.spell.impl.spellcraft.impl.DeepBlues;
import ru.vladislavkomkov.models.entity.unit.impl.naga.SpellCrafter;

public class DeepBlueCrooner extends SpellCrafter {
    public DeepBlueCrooner(){
        super(new DeepBlues());
        description = "Spellcraft: Give a minion +2/+3 until next turn. Improve your future Deep Blues.";
        level = 4;
        isTavern = true;
        
        attack = 2;
        
        maxHealth = 3;
        actualHealth = 3;
    }
}
