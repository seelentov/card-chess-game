package ru.vladislavkomkov.models.card;

import ru.vladislavkomkov.models.spell.Spell;

public class SpellCard extends Card{
    final Spell spell;

    public SpellCard(Spell spell) {
        this.spell = spell;
    }

    public Spell getSpell() {
        return spell;
    }
}
