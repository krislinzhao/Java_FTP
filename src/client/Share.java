package client;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintStream;
import java.net.Socket;

/**
 * @Package client
 * @ClassName Share
 * @Description 客户端的共享变量
 * @Date 19/12/15 16:02
 * @Author LIM
 * @Version V1.0
 */
public class Share {
    static String ip = "127.0.0.1";
    static File currentUploadFile;
    static String downloadSavePath;
    static Socket client_socket;
    static PrintStream client_out;
    static BufferedReader client_in;
    static int port = 8848;

    static JLabel groupLabel;
    static JButton uploadButton;
    static JScrollPane jScrollPane;
    static JPanel staffPanel;
    static Frame client;
}
