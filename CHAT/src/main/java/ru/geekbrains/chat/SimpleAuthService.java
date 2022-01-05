package ru.geekbrains.chat;

import ru.geekbrains.chat.AuthService;

import java.util.ArrayList;
import java.util.List;

public class SimpleAuthService implements AuthService {

    private List<UserData> users;

    public SimpleAuthService() {
        users = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            users.add(new UserData("login"+i,"pass"+i, "nick" + i));
        }
    }

    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        for (UserData user : users) {
            if(user.login.equals(login) && user.password.equals(password)){
                return user.nick;
            }
        }
        return null;
    }

    private static class UserData{

        private final String login;
        private final String password;
        private final String nick;

        private UserData(String login, String password, String nick) {
            this.login = login;
            this.password = password;
            this.nick = nick;
        }
    }

}
