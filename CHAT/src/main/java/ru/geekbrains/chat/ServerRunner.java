package ru.geekbrains.chat;

import ru.geekbrains.chat.ChatServer;

import java.io.IOException;

public class ServerRunner {
    public static void main(String[] args) throws IOException {
        new ChatServer().run();
    }
}
