package ru.vladislavkomkov.controller.sender;

import org.java_websocket.WebSocket;
import ru.vladislavkomkov.model.event.Event;

import java.util.function.Consumer;

public class WebSocketSender implements Sender{
    private WebSocket conn;


    public WebSocketSender(WebSocket conn){
        this.conn = conn;
    }

    @Override
    public void send(byte[] data) {
        conn.send(data);
    }

    @Override
    public void sendWithResponse(byte[] data, Consumer<byte[]> consumer) {
        consumer.accept(data);
    }
}
