package com.bailiban.day1.socket.cstest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HTTPClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8080);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
             BufferedReader sin = new BufferedReader(new InputStreamReader(System.in))){
            while (true){
                String line = sin.readLine();
                if (line==null || line.equals("bye")){
                    break;
                }
                out.println(line);
                String readLine = in.readLine();
                System.out.println(readLine);
            }
        }
    }
}
