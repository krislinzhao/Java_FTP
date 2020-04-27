package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * @Package client
 * @ClassName HandelFileThread
 * @Description 文件传输线程
 * @Date 19/12/17 10:11
 * @Author LIM
 * @Version V1.0
 */
public class HandelFileThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(HandelFileThread.class);
    /**
     * 文件传输模式  0:下载 1:上传
     */
    private int mode;
    private String filename;
    private Long fileSize;

    public HandelFileThread(int mode) {
        this.mode = mode;
    }

    public HandelFileThread(int mode, String filename, String fileSize) {
        this.mode = mode;
        this.filename = filename;
        this.fileSize = Long.parseLong(fileSize);
    }

    @Override
    public void run() {
        try {
            //下载文件模式
            if (mode == 0) {
                logger.info("下载模式");
                Socket socket = new Socket(Share.ip, 8888);
                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(Share.downloadSavePath + "/" + filename));

                int len;
                byte[] arr = new byte[8192];
                int i = 0;

                System.out.println("客户端开始下载 ");
                logger.info("客户端开始下载");
                while ((len = bis.read(arr)) != -1) {
                    bos.write(arr, 0, len);
                    bos.flush();
                }
                bos.close();
                bis.close();
                socket.close();
                System.out.println("客户端下载完成");
                logger.info("客户端下载完成");
            }

            //上传文件模式
            if (mode == 1) {
                logger.info("上传模式");
                Socket socket = new Socket(Share.ip, 8888);
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(Share.currentUploadFile));
                BufferedOutputStream bos = new BufferedOutputStream(new PrintStream(socket.getOutputStream()));

                int len;
                byte[] arr = new byte[8192];

                logger.info("开始上传--文件为{},大小为{}",Share.currentUploadFile.getName(),Share.currentUploadFile.length());

                System.out.println("开始上传--文件大小为：" + Share.currentUploadFile.length());

                while ((len = bis.read(arr)) != -1) {
                    bos.write(arr, 0, len);
                    bos.flush();
                }
                //上传完成
                socket.shutdownOutput();
                System.out.println("上传完成");
                logger.info("上传完成");
            }

        } catch (IOException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }
}