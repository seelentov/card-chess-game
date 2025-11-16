package ru.vladislavkomkov.models.entity.unit.impl.trash.beast.first;

import java.util.List;

import ru.vladislavkomkov.models.entity.unit.Type;
import ru.vladislavkomkov.models.entity.unit.TrashUnit;

public class Cat extends TrashUnit {
    public Cat(){
        super();
        type = List.of(Type.BEAST);
    }
}
