package ru.vladislavkomkov.controller.sender;

import java.util.function.Consumer;

public interface Sender
{
  void send(byte[] data);
  
  void sendWithResponse(byte[] data, Consumer<byte[]> consumer);
}
