package ru.geekbrains.chat;

public interface AuthService {
    String getNickByLoginAndPassword(String login , String password);
}
