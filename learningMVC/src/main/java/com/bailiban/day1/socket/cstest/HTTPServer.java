package com.bailiban.day1.socket.cstest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HTTPServer {
    private static Map<String,String> contentMap = new HashMap<>();
    static {
        contentMap.put("/","welcome");
        contentMap.put("/hello","world");
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);

        while (true){
            Socket socket = serverSocket.accept();
            System.out.println("客户端已连接"+socket.getInetAddress().getHostAddress());
            new Thread(()->{
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(),true)){
                    while (true){
                        String line = in.readLine();
                        System.out.println(line);
                        if (line == null || line.equals("bye")){
                            break;
                        }
                        String s = contentMap.get(line);
                        out.println(s==null?404:s);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        }

    }
}
