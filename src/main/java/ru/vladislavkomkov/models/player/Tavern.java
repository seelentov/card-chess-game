package ru.vladislavkomkov.models.player;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.actions.GlobalAction;
import ru.vladislavkomkov.models.card.Card;
import ru.vladislavkomkov.models.card.SpellCard;
import ru.vladislavkomkov.models.card.UnitCard;
import ru.vladislavkomkov.models.entity.spell.Spell;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.util.RandUtils;
import ru.vladislavkomkov.util.SpellUtils;
import ru.vladislavkomkov.util.UnitUtils;

import java.util.ArrayList;
import java.util.List;

public class Tavern {
    final List<Card> cards = new ArrayList<>();

    public Card buy(int index){
        return cards.remove(index);
    }

    boolean freeze = false;

    public void reset(int level){
        if(freeze){
            freeze = false;
            return;
        }

        cards.clear();

        int count = switch (level){
            case 1 -> 3;
            case 2,3 -> 4;
            case 4,5 -> 5;
            case 6 -> 6;
            default -> throw new RuntimeException("Wrong level");
        };

        for (int i = 0; i < count; i++) {
            int lvl = RandUtils.getRandLvl(level);
            List<Unit> units = UnitUtils.getUnitsByTavern(lvl);
            Unit unit = units.get(RandUtils.getRand(units.size() - 1));
            cards.add(new UnitCard(unit));
        }

        int lvl = RandUtils.getRandLvl(level);
        List<Spell> spells = SpellUtils.getByTavern(lvl);
        Spell spell = spells.get(RandUtils.getRand(spells.size() - 1));

        cards.add(new SpellCard(spell));
    }
}
