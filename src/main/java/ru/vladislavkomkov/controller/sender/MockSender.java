package ru.vladislavkomkov.controller.sender;

import java.util.function.Consumer;

public class MockSender implements Sender{
    @Override
    public void send(byte[] data) {

    }

    @Override
    public void sendWithResponse(byte[] data, Consumer<byte[]> consumer) {

    }
}
