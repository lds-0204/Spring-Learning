package com.bailiban.day1.socket.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        //3.请求连接服务器
        Socket socket = new Socket("localhost",8080);
        System.out.println("成功连接服务器");
        try (BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            //5.从命令行读取消息传给服务端
            String line = sin.readLine();
            out.println(line);
            //7.收到服务器消息
            System.out.println("server:"+in.readLine());
        }
    }
}
