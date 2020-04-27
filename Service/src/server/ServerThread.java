package server;


import dao.IDeleteFileDao;
import dao.impl.DeleteFileDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ParseDataUtil;

import java.io.*;
import java.net.Socket;

/**
 * @Package file
 * @ClassName ServerThread
 * @Description 启用一个线程处理客户端的请求
 * @Date 19/12/8 14:51
 * @Author LIM
 * @Version V1.0
 */
public class ServerThread implements Runnable {
    private Socket client_socket;
    private BufferedReader server_in;
    private PrintWriter server_out;
    private static final Logger logger = LoggerFactory.getLogger(ServerThread.class);


    public ServerThread(Socket client_socket) {
        try {
            //初始化
            this.client_socket = client_socket;
            server_in = new BufferedReader(new InputStreamReader(this.client_socket.getInputStream(), "UTF-8"));
            server_out = new PrintWriter(new OutputStreamWriter(this.client_socket.getOutputStream(), "UTF-8"), true);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String uploadFileName = null;
            String uploadFileSize = null;
            String fromClientData;

            while((fromClientData = server_in.readLine()) != null){
                //把服务器文件列表返回
                if (fromClientData.startsWith("loadFileList")) {
                    File dir = new File(Share.path);
                    if (dir.isDirectory()){
                        String[] list = dir.list();
                        String filelist = "GroupFileList[";
                        System.out.println(list.length);

                        if (list.length==0){
                            filelist = filelist+" "+"]";
                        }else {
                            for (int i = 0; i < list.length; i++) {
                                if (i == list.length-1){
                                    filelist  = filelist + list[i]+"]";
                                }else {
                                    filelist  = filelist + list[i]+":";
                                }
                            }
                        }
                        server_out.println(filelist);
                        System.out.println("服务器文件列表:" + filelist);
                        logger.info("服务器文件列表:{}", filelist);
                    }
                }
                this.upLoad(fromClientData, uploadFileName, uploadFileSize);
                this.downLoad(fromClientData);
                this.delete(fromClientData);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void upLoad(String fromClientData, String uploadFileName, String uploadFileSize) {
        //请求上传文件
        if (fromClientData.startsWith("Upload")) {
            uploadFileName = ParseDataUtil.getUploadFileName(fromClientData);
            uploadFileSize = ParseDataUtil.getUploadFileSize(fromClientData);
            File file = new File(Share.path, uploadFileName);
            //文件是否已存在
            if (file.exists()) {
                //文件已经存在无需上传
                server_out.println("Upload[null:null:NO]");
            } else {
                //通知客户端开可以上传文件
                server_out.println("Upload[" + uploadFileName + ":" + uploadFileSize + ":YES]");
                logger.info("开启一个上传文件的线程");
                //开启新线程上传文件
                /**
                 * 动态代理
                 */
                HandleFileThread hf = new HandleFileThread(1, uploadFileName, uploadFileSize, server_out);
                new Thread(hf).start();
            }
        }
    }

    public void downLoad(String fromClientData) {
        //请求下载文件
        if (fromClientData.startsWith("Download")) {
            String fileName = ParseDataUtil.getDownFileName(fromClientData);
            File file = new File(Share.path, fileName);
            if (!file.exists()) {
                server_out.println("Download[null:null:文件不存在]");
            } else {
                //通知客户端开可以下载文件
                server_out.println("Download[" + file.getName() + ":" + file.length() + ":OK]");
                logger.info("开启一个下载文件的线程");
                //开启新线程处理下载文件
                /**
                 * 动态代理
                 */
                HandleFileThread hf = new HandleFileThread(0, file.getName(), file.length() + "", server_out);
                new Thread(hf).start();
            }
        }

    }

    public void delete(String fromClientData) {
        // 请求删除文件
        if (fromClientData.startsWith("Delete")) {
            String fileName = ParseDataUtil.getDeleteFileName(fromClientData);
            File file = new File(Share.path, fileName);
            System.out.println("服务器端 要删除的文件是:" + fileName);
            if (file.exists()) {
                logger.info("开始删除");
                System.out.println("开始删除");
                String fileTpe = fileName.substring(fileName.indexOf(".") + 1);
                System.out.println("文件类型:" + fileTpe);
                // 文本文件
                if (Share.textTypes.contains(fileTpe)) {
                    logger.info("开始删除{}文本文件", fileName);
                    IDeleteFileDao deleteFileDao = new DeleteFileDaoImpl();
                    deleteFileDao.delete("table_text", fileName);
                    logger.info("删除{}文本文件成功", fileName);
                }
                // 图像文件
                if (Share.imageTypes.contains(fileTpe)) {
                    logger.info("开始删除{}图像文件", fileName);
                    IDeleteFileDao deleteFileDao = new DeleteFileDaoImpl();
                    deleteFileDao.delete("table_image", fileName);
                    logger.info("删除{}图像文件成功", fileName);
                }
                // 视频文件
                if (Share.videoTypes.contains(fileTpe)) {
                    logger.info("开始删除{}视频文件", fileName);
                    IDeleteFileDao deleteFileDao = new DeleteFileDaoImpl();
                    deleteFileDao.delete("table_video", fileName);
                    logger.info("删除{}视频文件成功", fileName);
                }
                // excel文件
                if (Share.excelTypes.contains(fileTpe)) {
                    logger.info("开始删除{}excel文件", fileName);
                    IDeleteFileDao deleteFileDao = new DeleteFileDaoImpl();
                    deleteFileDao.deleteExcel(fileName);
                    logger.info("删除{}excel文件成功", fileName);
                }
                server_out.println("Delete[null:null:文件已删除]");
                System.out.println("服务器端 删除文件成功");
            } else {
                server_out.println("Delete[null:null:文件不存在]");
                System.out.println("服务器端 文件不存在");
            }
        }
    }
}