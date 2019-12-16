package server;

import java.net.ServerSocket;

/**
 * @Package file
 * @ClassName Share
 * @Description 服务端一些共享的变量
 * @Date 19/12/8 14:53
 * @Author LIM
 * @Version V1.0
 */
public class Share {
    public static final int port = 8848;
    /**
     *普通消息传输服务器套接字
     */
    public static ServerSocket server_socket;
    /**
     * 文件传输服务器套接字
     */
    public static ServerSocket fileServerSocket;

    public static String path ="FileList\\";
}
