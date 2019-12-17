package server;

import java.net.ServerSocket;

/**
 * @Package file
 * @ClassName Share
 * @Description 服务端的共享变量
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

    /**
     * image格式
     */
    public static String imageTypes = "jpg png jif ";
    /**
     * video格式
     */
    public static String videoTypes = "mp4 avi dat mkv flv vob qlv";
    /**
     * 文本文件格式
     */
    public static String textTypes = "txt docx doc ppt pptx pdf c java cpp js html py ";
    /**
     * excel格式
     */
    public static String excelTypes = "xls xlsx";
}
