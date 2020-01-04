package com.bailiban.day1.socket.BStest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HTTPServer {
    public static Map<String,String> contentMap = new HashMap<>();
    static {
        contentMap.put("/","welcome");
        contentMap.put("/hello","world");
        contentMap.put("/yuxuyang","sb");
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(80);

        while (true){
            Socket socket = serverSocket.accept();
            new Thread(()->{
                //System.out.println("客户端已连接"+socket.getInetAddress().getHostAddress());
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(),true)){
                    String line = null;
                    StringBuffer buffer = new StringBuffer();
                    String request = null;
                    while ((line = in.readLine()) !=null && !line.equals("")){
                        buffer.append(line).append("\n");
                        if (request==null){
                            request = line;
                        }
                    }
                    //System.out.println(buffer.toString());

                    //System.out.println(buffer);
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type:text/html;charset=utf-8");
                    //空行表示请求头结束
                    out.println();
                    out.println("<html><head><title>HttpTest</title></head><body>");

                    if (request != null){
                        String url = request.split(" ")[1].split("\\?")[0];
                        System.out.println(url);
                        if (request.split(" ")[1].split("\\?").length>1){
                            String parameter = request.split(" ")[1].split("\\?")[1];
                            String[] parameters = parameter.split("&");
                            for (String s:parameters) {
                                String[] split = s.split("=");
                                String s1 = split[0];
                                String s2 = split[1];
                                System.out.println(s1 +":"+s2);
                            }
                        }
                        String s = contentMap.get(url);
                        out.println(s==null?"404":s);
                    }
                    out.println("</body></html>");
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }).start();
        }





    }
}
