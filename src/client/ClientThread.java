package client;

import utils.ParseDataUtil;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

/**
 * @Package client
 * @ClassName ClientThread
 * @Description 监听服务器消息的线程
 * @Date 19/12/15 16:00
 * @Author LIM
 * @Version V1.0
 */
public class ClientThread extends Thread {
    @Override
    public void run() {
        try {
            String fromServer_data;
            int flag = 0;

            while ((fromServer_data = Share.client_in.readLine()) != null) {
                System.out.println(fromServer_data);
                //读取群文件列表
                if (fromServer_data.startsWith("@action=GroupFileList")) {
                    //重新渲染顶部面板
                    FileClient.renderTop();

                    //注册监听
                    FileClient.registerListener();

                    //渲染文件面板
                    String[] fileList = ParseDataUtil.getFileList(fromServer_data);
                    for (String filename : fileList) {
                        FileClient.addToFileList(filename);
                    }
                }

                //文件上传
                if (fromServer_data.startsWith("@action=Upload")) {
                    String res = ParseDataUtil.getUploadResult(fromServer_data);
                    if ("NO".equals(res)) {
                        JOptionPane.showMessageDialog(null, "文件已存在!");
                    } else if ("YES".equals(res)) {
                        //开始上传
                        if (Share.currentUploadFile != null) {
                            //开启新线程传输文件
                            new HandelFileThread(1).start();
                        }

                    } else if ("上传完成".equals(res)) {
                        JOptionPane.showMessageDialog(null, res);
                        FileClient.loadGroupFile();
                    }
                }

                //文件下载
                if (fromServer_data.startsWith("@action=Download")) {
                    String res = ParseDataUtil.getDownResult(fromServer_data);
                    if ("文件不存在".equals(res)) {
                        JOptionPane.showMessageDialog(null, "该文件不存在404");
                    } else {
                        String downFileName = ParseDataUtil.getDownFileName(fromServer_data);
                        String downFileSize = ParseDataUtil.getDownFileSize(fromServer_data);
                        //开启新线程传输文件
                        new HandelFileThread(0, downFileName, downFileSize).start();
                    }
                }

                // 文件删除
                if (fromServer_data.startsWith("@action=Delete")){
                    System.out.println("客户端 删除文件成功");
                    String res = ParseDataUtil.getDeleteResult(fromServer_data);
                    if ("文件已删除".equals(res)){
                        JOptionPane.showMessageDialog(null,"文件删除成功");
                    }
                    FileClient.loadGroupFile();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件传输线程
     */
    class HandelFileThread extends Thread {
        //文件传输模式  0:下载 1:上传
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
                if (this.mode == 0) {
                    Socket socket = new Socket(Share.ip, 8888);
                    BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(Share.downloadSavePath + "/" + filename));

                    int len;
                    byte[] arr = new byte[8192];
                    int i = 0;

                    System.out.println("客户端开始下载 ");
                    while ((len = bis.read(arr)) != -1) {
                        bos.write(arr, 0, len);
                        bos.flush();
                    }
                    bos.close();
                    bis.close();
                    socket.close();
                    System.out.println("客户端下载完成");
                }

                //上传文件模式
                if (this.mode == 1) {
                    Socket socket = new Socket(Share.ip, 8888);
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(Share.currentUploadFile));
                    BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

                    int len;
                    byte[] arr = new byte[8192];

                    System.out.println("开始上传--文件大小为：" + Share.currentUploadFile.length());

                    while ((len = bis.read(arr)) != -1) {
                        bos.write(arr, 0, len);
                        bos.flush();
                    }
                    //上传完成
                    socket.shutdownOutput();
                    System.out.println("上传完成");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}