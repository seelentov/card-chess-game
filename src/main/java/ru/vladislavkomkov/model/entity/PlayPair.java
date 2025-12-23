package ru.vladislavkomkov.model.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.vladislavkomkov.model.entity.unit.UnitType;

public class PlayPair {
    public final static String F_TYPE = "type";
    public final static String F_UNIT_TYPES = "unit_types";
    
    
    private final PlayType type;
    private final List<UnitType> unitTypes;
    
    public PlayPair(PlayType type) {
        this(type, List.of());
    }
    
    public PlayPair(PlayType type, List<UnitType> unitTypes) {
        this.type = type;
        this.unitTypes = unitTypes;
    }
    
    @JsonProperty(F_TYPE)
    public PlayType getType() {
        return type;
    }
    
    @JsonProperty(F_UNIT_TYPES)
    public List<UnitType> getUnitTypes() {
        return unitTypes;
    }
}
