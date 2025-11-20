package ru.vladislavkomkov.models.entity.unit;

public enum Type {
    BEAST("Beast"),
    NAGA("Naga"),
    DRAGON("Dragon"),
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
