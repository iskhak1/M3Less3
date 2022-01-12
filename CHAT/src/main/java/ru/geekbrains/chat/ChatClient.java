package ru.geekbrains.chat;

import java.io.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatClient {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private final Controller controller;

    private boolean timeToClose = true;
     String block;

    public ChatClient(Controller controller) {
        this.controller = controller;

    }

    public void openConnection() {
        try {
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try{
                    final String nick;
                    while (true) {
                        block();
                        final String msgAuth = in.readUTF();
                        if (msgAuth.startsWith("/authok")) {
                            final String[] split = msgAuth.split(" ");
                             nick = split[1];
                             controller.addMessage("Успешная авторизация под ником " + nick);

                            controller.setAuth(true);
                            try (BufferedReader reader = new BufferedReader(new FileReader(nick + ".txt"))) {
                                String str;
                                while ((str = reader.readLine()) != null) {
                                    controller.addMessage(str);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            timeToClose = false;
                            break;
                        }
                    }

                    while (true) {
                         String message = in.readUTF();
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nick + ".txt", true))) {
                            writer.write(message+"\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (message.startsWith("/")) {
                            if ("/end".equals(message)) {
                                controller.setAuth(false);
                                break;
                            }
                            if (message.startsWith("/clients")) { // /clients nick1 nick0
                                final String[] tokens = message.replace("/clients ", "").split(" ");
                                final List<String> clients = Arrays.asList(tokens);
                                controller.updateClientList(clients);
                            }
                        }
                        controller.addMessage(message);
                    }
                } catch (IOException e) {

                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    public void block(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(timeToClose==true) {
                    System.out.println("you Late, сработал метод ,block /=)");
                    block = "you Late, сработал метод ,block /=)";
                    closeConnection();
                    timer.cancel();
                    timer.purge();
                }
                timer.cancel();
                timer.purge();
            }
        };
        timer.schedule(task, 5000);
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}