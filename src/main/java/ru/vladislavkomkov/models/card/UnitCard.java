package ru.vladislavkomkov.models.card;

import ru.vladislavkomkov.models.entity.unit.Unit;

public class UnitCard extends Card{
    final Unit unit;

    public UnitCard(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }
}
