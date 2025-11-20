package ru.vladislavkomkov.models.entity.unit;

public enum Type {
    BEAST("Beast"),
    NAGA("Naga"),
    UNDEAD("Undead"),
    DEMON("Demon"),
    MECH("Mech");

    final String type;

    Type(final String type){
        this.type = type;
    }

    @Override
    public String toString(){
        return type;
    }
}
