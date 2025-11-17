package ru.vladislavkomkov.util;

import static ru.vladislavkomkov.consts.Listeners.KEY_ONE_USE_PREFIX;

import java.util.UUID;

public class ListenerUtils {
    
    public static String generateKeyOneUse(){
        return KEY_ONE_USE_PREFIX + UUID.randomUUID().toString();
    }
}
