package ru.vladislavkomkov.models.unit;

public enum Type {
    BEAST("Beast"),
    NONE("");

    final String type;

    Type(final String type){
        this.type = type;
    }

    @Override
    public String toString(){
        return type;
    }
}
