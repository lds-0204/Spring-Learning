package com.bailiban.day1.socket.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveThread extends Thread {
    private String name;
    private Socket socket;

    public ReceiveThread(String name, Socket socket) {
        this.name = name;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (true){
                String line = in.readLine();
                if (line==null || line.equals("bye")){
                    break;
                }
                System.out.println(name + ":"+line);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}
