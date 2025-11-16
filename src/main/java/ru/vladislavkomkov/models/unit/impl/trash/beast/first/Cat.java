package ru.vladislavkomkov.models.unit.impl.trash.beast.first;

import java.util.List;

import ru.vladislavkomkov.models.unit.Type;
import ru.vladislavkomkov.models.unit.TrashUnit;

public class Cat extends TrashUnit {
    public Cat(){
        super();
        type = List.of(Type.BEAST);
    }
}
