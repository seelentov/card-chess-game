package ru.vladislavkomkov.consts;

import ru.vladislavkomkov.models.entity.spell.Spell;
import ru.vladislavkomkov.models.entity.spell.impl.first.TavernCoin;

import java.util.ArrayList;
import java.util.List;

public class Spells {
    public static List<Spell> spells = new ArrayList<>();
    public static List<Spell> tavernSpells = new ArrayList<>();

    static {
        setup();
        setupTavern();
    }

    static void setup(){
        spells.add(new TavernCoin());
    }

    static void setupTavern(){
        spells.forEach(unit -> {
            if(unit.isTavern()){
                tavernSpells.add(unit);
            }
        });
    }
}
