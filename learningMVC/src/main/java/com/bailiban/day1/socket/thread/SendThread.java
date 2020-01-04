package com.bailiban.day1.socket.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SendThread extends Thread{
    private String name;
    private Socket socket;

    public SendThread(String name, Socket socket) {
        this.name = name;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
             BufferedReader sin = new BufferedReader(new InputStreamReader(System.in))){
            while (true){
                String line = sin.readLine();
                if (line == null || line.equals("bye")){
                    break;
                }
                System.out.println(name + ":"+line);
                out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
