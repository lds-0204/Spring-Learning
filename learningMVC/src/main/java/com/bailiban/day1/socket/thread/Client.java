package com.bailiban.day1.socket.thread;

import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("192.168.1.111", 8080);
        new ReceiveThread("Server",socket).start();

        new SendThread("Client",socket).start();
    }
}
