package ru.vladislavkomkov.util;

public interface TriConsumer<T,K,V> {
    void consume(T a, K b, V c);
}
