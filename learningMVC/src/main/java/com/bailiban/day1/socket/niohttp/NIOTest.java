package com.bailiban.day1.socket.niohttp;


import com.bailiban.day1.socket.BStest.HTTPServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class NIOTest {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.socket().bind(new InetSocketAddress(80));
        socketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            if (selector.select(1000)<=0){
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                httpHandle(key);
                iterator.remove();
            }
        }
    }

    private static void httpHandle(SelectionKey key) throws IOException {
        if (key.isAcceptable()){
            acceptHandle(key);
        }else if (key.isReadable()){
            requestHandle(key);
        }
    }

    private static void acceptHandle(SelectionKey key) throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(),SelectionKey.OP_READ,ByteBuffer.allocate(1024));

    }

    private static void requestHandle(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel)key.channel();
        ByteBuffer byteBuffer = (ByteBuffer)key.attachment();
        byteBuffer.clear();
        if (socketChannel.read(byteBuffer) == -1){
            socketChannel.close();
            return;
        }
        byteBuffer.flip();
        String request = new String(byteBuffer.array());
        String url = request.split("\r\n")[0].split(" ")[1];
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HTTP/1.1 200 OK\r\n");
        stringBuffer.append("Content-Type:text/html;charset=utf-8\r\n\r\n");
        stringBuffer.append("<html><head><title>HttpTest</title></head><body>");
        String s = HTTPServer.contentMap.get(url);
        stringBuffer.append(s==null?"404":s);

        stringBuffer.append("</body></html>");
        socketChannel.write(ByteBuffer.wrap(stringBuffer.toString().getBytes()));
        socketChannel.close();
    }

}
