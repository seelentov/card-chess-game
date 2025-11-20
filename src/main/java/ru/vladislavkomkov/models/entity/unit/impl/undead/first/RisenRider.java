package ru.vladislavkomkov.models.entity.unit.impl.undead.first;

import ru.vladislavkomkov.models.entity.unit.Type;
import ru.vladislavkomkov.models.entity.unit.Unit;

import java.util.List;

public class RisenRider extends Unit {
    public RisenRider(){
        description = "Taunt Reborn";
        isTaunt = true;
        isRebirth = true;

        type = List.of(Type.UNDEAD);

        attack = 2;

        maxHealth = 1;
        actualHealth = 1;

        level = 1;
    }
}
