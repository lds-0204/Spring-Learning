package com.bailiban.day1.socket.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket socket = serverSocket.accept();
        System.out.println(socket.getInetAddress().getHostAddress());
        new ReceiveThread("Client",socket).start();

        new SendThread("Server",socket).start();
    }
}
