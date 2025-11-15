package ru.vladislavkomkov.service;

import ru.vladislavkomkov.consts.Units;
import ru.vladislavkomkov.models.unit.Unit;

import java.util.List;

public class UnitService {
    public static List<Unit> getUnits(){
        return Units.units;
    };

    public static List<Unit> getTavernUnits(){
        return Units.tavernUnits;
    };

    public static List<Unit> getUnitsByTavern(int lvl){
        return getUnitsByTavern(lvl, true);
    };

    public static List<Unit> getUnitsByTavern(int lvl, boolean tavern){
        List<Unit> units = tavern ? getTavernUnits() : getUnits();
        return units.stream().filter(unit -> unit.getLevel() == lvl).toList();
    };
}
