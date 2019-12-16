package server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Package file
 * @ClassName FileServer
 * @Description 服务器程序入口
 * @Date 19/12/6 11:39
 * @Author LIM
 * @Version V1.0
 */
public class FileServer {

    public FileServer() {
        try {
            //1-初始化
            //1.1创建消息传输服务器
            Share.server_socket = new ServerSocket(Share.port);
            //1.2创建文件传输服务器
            Share.fileServerSocket = new ServerSocket(8888);


            //2每次接收一个客户端请求连接时都启用一个线程处理
            while(true) {
                Socket client_socket = Share.server_socket.accept();
                new ServerThread(client_socket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动程序
     * @param args
     */
    public static void main(String[] args) {
        new FileServer();
    }
}
