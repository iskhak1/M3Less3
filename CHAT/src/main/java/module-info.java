module ru.geekbrains.chat {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.geekbrains.chat to javafx.fxml;
    exports ru.geekbrains.chat;
}