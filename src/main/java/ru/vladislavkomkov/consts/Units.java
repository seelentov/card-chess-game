package ru.vladislavkomkov.consts;

import ru.vladislavkomkov.models.unit.Unit;
import ru.vladislavkomkov.models.unit.impl.AccordoTron;
import ru.vladislavkomkov.models.unit.impl.Alleycat;
import ru.vladislavkomkov.models.unit.impl.Cat;

import java.util.ArrayList;
import java.util.List;

public class Units {
    public static List<Unit> units = new ArrayList<>();
    public static List<Unit> tavernUnits = new ArrayList<>();

    static {
        setupUnits();
        setupTavernUnits();
    }

    static void setupUnits(){
        units.add(new Alleycat());
        units.add(new Cat());
        units.add(new AccordoTron());
    }

    static void setupTavernUnits(){
        units.forEach(unit -> {
            if(unit.isTavern()){
                tavernUnits.add(unit);
            }
        });
    }
}
