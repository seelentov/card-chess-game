package ru.vladislavkomkov;

public class Main {
    public static void main(String[] args) {
        Object obj1 = new Object();
        Object obj2 = new Object();
        
        // Получаем идентификатор в том же формате, что и IntelliJ
        String obj1Hex = Integer.toHexString(System.identityHashCode(obj1));
        String obj2Hex = Integer.toHexString(System.identityHashCode(obj2));
        
        System.out.println("obj1: Object@" + obj1Hex);
        System.out.println("obj2: Object@" + obj2Hex);
        System.out.println("obj1 identity hash (decimal): " + System.identityHashCode(obj1));
        System.out.println("obj2 identity hash (decimal): " + System.identityHashCode(obj2));
        System.out.println("obj2 identity hash (decimal): " + System.identityHashCode(obj2));
    }
}