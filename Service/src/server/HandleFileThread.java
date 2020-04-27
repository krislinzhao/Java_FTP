package server;

import dao.IAddFileDao;
import dao.impl.AddFileDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.Image;
import pojo.Text;
import pojo.Video;
import utils.CreateSQL;
import utils.ParseTextTypeUtil;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * @Package server
 * @ClassName HandleFileThread
 * @Description 文件传输线程
 * @Date 19/12/17 10:14
 * @Author LIM
 * @Version V1.0
 */
public class HandleFileThread implements Runnable {
    private String filename;
    private String fileSize;
    private static final Logger logger = LoggerFactory.getLogger(HandleFileThread.class);
    /**
     * 文件传输模式 0-下载 1-上传
     */
    private int mode;
    private PrintWriter server_out;
    Socket socket;

    public HandleFileThread(int mode, String name, String size, PrintWriter server_out) {

        try {
            socket = Share.fileServerSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        filename = name;
        fileSize = size;
        this.mode = mode;
        this.server_out = server_out;
        System.out.println("文件名:" + filename);
        System.out.println("模式:" + mode);
    }

    @Override
    public void run() {
        try {
            //下载文件模式
            if (mode == 0) {
                logger.info("下载模式");
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(Share.path, filename)));
                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

                System.out.println("服务器：开始下载");
                logger.info("服务器: 开始下载");
                int len;
                byte[] arr = new byte[8192];
                while ((len = bis.read(arr)) != -1) {
                    bos.write(arr, 0, len);
                    bos.flush();
                }
                System.out.println("服务器:下载完成");
                logger.info("服务器: 下载完成");
                socket.shutdownOutput();
            }

            //上传文件模式
            if (mode == 1) {
                logger.info("上传模式");
                //开始接收上传
                BufferedInputStream file_in = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(Share.path, filename)));

                int len;
                byte[] arr = new byte[8192];

                // 保存到服务器
                while ((len = file_in.read(arr)) != -1) {
                    bos.write(arr, 0, len);
                    bos.flush();
                }
                server_out.println("Upload[null:null:上传完成]");
                server_out.println("\n");
                bos.close();

                String fileTpe = filename.substring(filename.indexOf(".") + 1);
                System.out.println("文件类型:" + fileTpe);

                String str = null;

                // 文本文件
                if (Share.textTypes.contains(fileTpe)) {
                    logger.info("开始上传的文本文件:{}", filename);
                    System.out.println("开始上传的文件:" + filename);
                    String file = Share.path + filename;
                    str = ParseTextTypeUtil.importFile(file, null);
                    System.out.println("文本内容:" + str);
                    IAddFileDao addText = new AddFileDaoImpl();
                    Text text = new Text();
                    text.setFilename(filename);
                    text.setContext(str);
                    text.setTime(String.valueOf(new Date()));
                    addText.textAdd(text);
                    logger.info("文本文件上传结束");
                }
                // 图像文件
                if (Share.imageTypes.contains(fileTpe)) {
                    logger.info("开始上传的图像文件:{}", filename);
                    System.out.println("开始上传的文件名" + filename);
                    String file = Share.path + filename;
                    FileInputStream fileInputStream = new FileInputStream(file);
                    IAddFileDao addImage = new AddFileDaoImpl();
                    Image image = new Image();
                    image.setFilename(filename);
                    image.setImage(fileInputStream);
                    image.setTime(String.valueOf(new Date()));
                    addImage.imageAdd(image);
                    logger.info("图像文件上传结束");
                }
                // 视频文件
                if (Share.videoTypes.contains(fileTpe)) {
                    logger.info("开始上传的视频文件:{}", filename);
                    System.out.println("开始上传的文件名" + filename);
                    //插入全文件
                    String file = Share.path + filename;
                    FileInputStream fileInputStream = new FileInputStream(file);
                    IAddFileDao addVideo = new AddFileDaoImpl();
                    Video video = new Video();
                    video.setFilename(filename);
                    video.setVideo(fileInputStream);
                    video.setTime(String.valueOf(new Date()));
                    addVideo.videoAdd(video);
                    logger.info("图像文件上传结束");
                }
                // excel格式
                if (Share.excelTypes.contains(fileTpe)) {
                    logger.info("开始上传的excel文件:{}", filename);
                    System.out.println("开始上传的文件名" + filename);
                    System.out.println(Share.path + filename);
                    try {
                        logger.info("解析excel文件,动态创建数据库");
                        CreateSQL.createTable(Share.path + filename);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        e.printStackTrace();
                    }
                    logger.info("excel文件上传结束");
                }
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}