package client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;

/**
 * @Package file
 * @ClassName FileClient
 * @Description 客户端启动
 * @Date 19/12/6 11:40
 * @Author LIM
 * @Version V1.0
 */
public class FileClient extends JFrame {
    private static int width = 600;
    private static int height = 600;

    private static int Y = 0;

    private static final Logger logger = LoggerFactory.getLogger(FileClient.class);


    public FileClient() {
        //1-初始化
        initVariable();
        //2-连接服务器
        connectServer();
        //3-注册监听
        registerListener();
        //4-初始化窗口设置
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setTitle("文件传输客户端");
        //窗口居中显示
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }


    private void initVariable() {
        Share.jScrollPane = new JScrollPane();
        getContentPane().add(Share.jScrollPane);

        Share.staffPanel = new JPanel();
        Share.staffPanel.setLayout(null);
        Share.staffPanel.setOpaque(false);
        Share.staffPanel.setPreferredSize(new Dimension(width, height));

        Share.jScrollPane.setViewportView(Share.staffPanel);
        //设置水平滚动条隐藏
        Share.jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //设置透明
        Share.jScrollPane.getViewport().setOpaque(false);
        //设置透明
        Share.jScrollPane.setOpaque(false);

        renderTop();
        Share.client = this;
    }


    /**
     * 向服务器重新读取群文件列表
     */
    public static void loadGroupFile() {
        logger.info("向服务器发起读取群文件列表的消息");
        Share.client_out.println("loadFileList");
    }


    /**
     * 渲染顶部面板
     */
    public static void renderTop() {
        Share.staffPanel.removeAll();
        Y = 0;
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3, 3, 10));
        Share.groupLabel = new JLabel("\t\t\t\t\t群文件列表 ");
        Share.uploadButton = new JButton("上传文件 ");
        panel.add(Share.groupLabel);
        panel.add(Share.uploadButton);

        panel.setBounds(2, Y, width, 30);
        Share.staffPanel.add(panel);
        Y += 30;
    }

    /**
     * 渲染文件列表
     */
    public static void addToFileList(String filename) {
        JButton downloadBtn = new JButton("下载");
        JButton deleteBtn = new JButton("删除");
        JLabel fileNameLab = new JLabel(filename);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3, 0, 0));
        panel.add(fileNameLab);
        panel.add(downloadBtn);
        panel.add(deleteBtn);

        panel.setBounds(2, Y, width, 30);
        Share.staffPanel.add(panel);
        Y += 30;


        //文件下载
        downloadBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //1-选择下载保存的位置
                JFileChooser f = new JFileChooser(); // 查找文件
                f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                f.showOpenDialog(null);
                File file = f.getSelectedFile();

                if (file != null) {
                    Share.downloadSavePath = file.getPath();
                    //2-向服务器请求下载
                    logger.info("向服务器请求下载");
                    Share.client_out.println("Download[" + filename + ":null:null]");
                }
                Share.client.revalidate(); //刷新界面
            }
        });

        //文件删除
        deleteBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("客户端 要删除的文件是:"+filename);
                //1-向服务器发送删除的请求
                logger.info("向服务器发送删除的请求");
                Share.client_out.println("Delete[" + filename + ":null:null]");
            }
        });

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        Share.client.revalidate();  //刷新界面

    }

    /**
     * 注册监听
     */
    public static void registerListener() {
        /**
         * 上传文件    消息格式: Upload["fileName":"fileSize":result]
         */
        Share.uploadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //1-选择上传文件
                JFileChooser f = new JFileChooser();
                f.showOpenDialog(null);
                Share.currentUploadFile = f.getSelectedFile();
                if (Share.currentUploadFile != null) {
                    //2-向服务器请求上传
                    logger.info("向服务器请求上传");
                    Share.client_out.println("Upload[" + Share.currentUploadFile.getName() + ":" + Share.currentUploadFile.length() + ":null]");
                }

            }
        });
    }

    /**
     * 连接服务器
     */
    private void connectServer() {
        //连接服务器
        try {
            //初始化
            Share.client_socket = new Socket(Share.ip, Share.port);
            Share.client_out = new PrintStream(Share.client_socket.getOutputStream(), true);
            //Share.client_out = new PrintStream(String.valueOf(new OutputStreamWriter(Share.client_socket.getOutputStream(),"UTF-8")), String.valueOf(true));
            Share.client_in = new BufferedReader(new InputStreamReader(Share.client_socket.getInputStream(),"UTF-8"));

            //读取文件列表
            Share.client_out.println("loadFileList");

            //开启线程监听服务器消息
            logger.info("开启线程监听服务器消息");
            new ClientThread().start();

        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 启动程序
     * @param args
     * @throws Exception
     */
    public static void main(String[] args){
        logger.info("启动客户端程序");
        new FileClient();
    }
}