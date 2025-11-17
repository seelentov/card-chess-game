package ru.vladislavkomkov.util;

import static ru.vladislavkomkov.consts.Listeners.KEY_ONCE_PREFIX;

import java.util.UUID;

public class ListenerUtils {
    
    public static String generateKeyOneUse(){
        return KEY_ONCE_PREFIX + generateKey();
    }

    public static String generateKey(){
        return UUID.randomUUID().toString();
    }
}
