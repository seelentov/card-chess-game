package ru.vladislavkomkov.util;

import ru.vladislavkomkov.consts.Units;
import ru.vladislavkomkov.models.entity.unit.Unit;

import java.util.List;

public class UnitUtils {
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
