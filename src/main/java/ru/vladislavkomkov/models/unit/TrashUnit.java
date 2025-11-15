package ru.vladislavkomkov.models.unit;

public abstract class TrashUnit extends Unit{
    public TrashUnit(){
        level = 1;
        
        attack = 1;
        maxHealth = 1;
        
        actualHealth = 1;
    }
}
