package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(FileServer.class);

    public FileServer() {
        try {
            //1-初始化
            //1.1创建消息传输服务器
            Share.server_socket = new ServerSocket(Share.port);
            logger.info("消息传输套接字创建成功");
            //1.2创建文件传输服务器
            Share.fileServerSocket = new ServerSocket(8888);
            logger.info("文件传输套接字创建成功");


            //2每次接收一个客户端请求连接时都启用一个线程处理
            while(true) {
                Socket client_socket = Share.server_socket.accept();
                ServerThread st = new ServerThread(client_socket);
                logger.info("开启一个线程处理客户端请求消息");
                new Thread(st).start();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
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