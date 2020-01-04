package com.bailiban.day1.socket.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        //1.建立服务端
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("等待链接...");
        //2.监听客户端连接
        Socket socket = serverSocket.accept();
        System.out.println("客户端已连接:"+socket.getInetAddress().getHostAddress());

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(socket.getOutputStream(),true)){
            //4.从客户端读取消息,会阻塞
            System.out.println("client:"+in.readLine());
            //6.从命令行读取消息传给客户端
            String line = sin.readLine();
            out.println(line);
        }
    }
}
