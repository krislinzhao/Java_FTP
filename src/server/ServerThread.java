package server;


import dao.IAddFileDao;
import dao.IDeleteFileDao;
import dao.impl.AddFileDaoImpl;
import dao.impl.DeleteFileDaoImpl;
import pojo.Image;
import pojo.Text;
import pojo.Video;
import utils.CreateSQL;
import utils.ParseDataUtil;
import utils.ParseTextTypeUtil;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * @Package file
 * @ClassName ServerThread
 * @Description 启用一个线程处理客户端的请求
 * @Date 19/12/8 14:51
 * @Author LIM
 * @Version V1.0
 */
public class ServerThread extends Thread{
    private Socket client_socket;
    private BufferedReader server_in;
    private PrintWriter server_out;

    /**
     * image格式
     */
    String imageTypes ="jpg png";
    /**
     * video格式
     */
    String videoTypes = "mp4 avi dat mkv flv vob";
    /**
     * 文本文件格式
     */
    String textTypes = "txt docx doc ppt pptx pdf";
    /**
     * excel格式
     */
    String excelTypes = "xls xlsx";

    public ServerThread(Socket client_socket) {
        try {
            //初始化
            this.client_socket = client_socket;
            server_in = new BufferedReader(new InputStreamReader(this.client_socket.getInputStream()));
            server_out = new PrintWriter(this.client_socket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String uploadFileName = null;
            String uploadFileSize = null;
            String fromClientData ;

            while((fromClientData = server_in.readLine()) != null){
                //把服务器文件列表返回
                if(fromClientData.startsWith("@action=loadFileList")){
                    File dir = new File(Share.path);
                    if (dir.isDirectory()){
                        String[] list = dir.list();
                        String filelist = "@action=GroupFileList[";
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
                        System.out.println("把服务器文件列表返回"+filelist);
                    }
                }

                //请求上传文件
                if (fromClientData.startsWith("@action=Upload")){
                    uploadFileName = ParseDataUtil.getUploadFileName(fromClientData);
                    uploadFileSize = ParseDataUtil.getUploadFileSize(fromClientData);
                    File file = new File(Share.path,uploadFileName);
                    //文件是否已存在
                    if (file.exists()){
                        //文件已经存在无需上传
                        server_out.println("@action=Upload[null:null:NO]");
                    }else {
                        //通知客户端开可以上传文件
                        server_out.println("@action=Upload["+uploadFileName+":"+uploadFileSize+":YES]");
                        //开启新线程上传文件
                        new HandleFileThread(1,uploadFileName,uploadFileSize).start();
                    }
                }

                //请求下载文件
                if(fromClientData.startsWith("@action=Download")){
                    String fileName = ParseDataUtil.getDownFileName(fromClientData);
                    File file = new File(Share.path,fileName);
                    if(!file.exists()){
                        server_out.println("@action=Download[null:null:文件不存在]");
                    }else {
                        //通知客户端开可以下载文件
                        server_out.println("@action=Download["+file.getName()+":"+file.length()+":OK]");
                        //开启新线程处理下载文件
                        new HandleFileThread(0,file.getName(),file.length()+"").start();

                    }
                }
                // 请求删除文件
                if (fromClientData.startsWith("@action=Delete")){
                    String fileName = ParseDataUtil.getDeleteFileName(fromClientData);
                    File file = new File(Share.path,fileName);
                    System.out.println("服务器端 要删除的文件是:"+fileName);
                    if (file.exists()){
                        System.out.println("开始删除");
                        String fileTpe = fileName.substring(fileName.indexOf(".")+1);
                        System.out.println("文件类型:"+fileTpe);
                        // 文本文件
                        if (textTypes.contains(fileTpe)){
                            IDeleteFileDao deleteFileDao = new DeleteFileDaoImpl();
                            deleteFileDao.delete("table_text",fileName);
                        }
                        // 图像文件
                        if (imageTypes.contains(fileTpe)){
                            IDeleteFileDao deleteFileDao = new DeleteFileDaoImpl();
                            deleteFileDao.delete("table_image",fileName);
                        }
                        // 视频文件
                        if (videoTypes.contains(fileTpe)){
                            IDeleteFileDao deleteFileDao = new DeleteFileDaoImpl();
                            deleteFileDao.delete("table_video",fileName);
                        }
                        // excel文件
                        if (excelTypes.contains(fileTpe)){
                            IDeleteFileDao deleteFileDao = new DeleteFileDaoImpl();
                            deleteFileDao.deleteExcel(fileName);
                        }
                        server_out.println("@action=Delete[null:null:文件已删除]");
                        System.out.println("服务器端 删除文件成功");
                    }else {
                        server_out.println("@action=Delete[null:null:文件不存在]");
                        System.out.println("服务器端 文件不存在");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *     文件传输线程
     */
     class HandleFileThread extends Thread{
        private String filename;
        private String fileSize;
        //文件传输模式 0-下载 1-上传
        private int mode;

        Socket socket;

        public HandleFileThread(int mode,String name,String size){

            try {
                socket = Share.fileServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            filename = name;
            fileSize = size;
            this.mode = mode;
            System.out.println("文件名:"+filename);
            System.out.println("模式:"+mode);
        }

        @Override
        public void run() {
            try {
                //下载文件模式
                if(mode == 0){
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(Share.path,filename)));
                    BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

                    System.out.println("服务器：开始下载");
                    int len;
                    byte[] arr =new byte[8192];
                    while((len = bis.read(arr)) != -1){
                        bos.write(arr,0,len);
                        bos.flush();
                    }

                    System.out.println("服务器:下载完成");
                    socket.shutdownOutput();
                }

                //上传文件模式
                if(mode == 1){
                    //开始接收上传
                    BufferedInputStream file_in = new BufferedInputStream(socket.getInputStream());
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(Share.path,filename)));

                    int len;
                    byte[] arr = new byte[8192];

                    // 保存到服务器
                    while ((len = file_in.read(arr)) != -1){
                        bos.write(arr,0,len);
                        bos.flush();
                    }
                    server_out.println("@action=Upload[null:null:上传完成]");
                    server_out.println("\n");
                    bos.close();

                    String fileTpe = filename.substring(filename.indexOf(".")+1);
                    System.out.println("文件类型:"+fileTpe);

                    String str=null;

                    // 文本文件
                    if (textTypes.contains(fileTpe)){
                        System.out.println("开始上传的文件:"+filename);
                        String file = Share.path+filename;
                        str = ParseTextTypeUtil.importFile(file,null);
                        System.out.println("文本内容:"+str);
                        IAddFileDao addText = new AddFileDaoImpl();
                        Text text = new Text();
                        text.setFilename(filename);
                        text.setContext(str);
                        text.setTime(String.valueOf(new Date()));
                        addText.textAdd(text);
                    }
                    // 图像文件
                    if (imageTypes.contains(fileTpe)){
                        System.out.println("开始上传的文件名"+filename);
                        String file = Share.path+filename;
                        FileInputStream fileInputStream = new FileInputStream(file);
                        IAddFileDao addImage = new AddFileDaoImpl();
                        Image image = new Image();
                        image.setFilename(filename);
                        image.setImage(fileInputStream);
                        image.setTime(String.valueOf(new Date()));
                        addImage.imageAdd(image);
                    }
                    // 视频文件
                    if (videoTypes.contains(fileTpe)){
                        System.out.println("开始上传的文件名"+filename);
                        //插入全文件
                        String file = Share.path+filename;
                        FileInputStream fileInputStream = new FileInputStream(file);
                        IAddFileDao addVideo = new AddFileDaoImpl();
                        Video video = new Video();
                        video.setFilename(filename);
                        video.setVideo(fileInputStream);
                        video.setTime(String.valueOf(new Date()));
                        addVideo.videoAdd(video);
                        //// 插入名字
                        //IAddFileDao addVideoName = new AddFileDaoImpl();
                        //VideoName videoName = new VideoName();
                        //videoName.setFilename(filename);
                        //videoName.setVideoName(filename);
                        //videoName.setTime(String.valueOf(new Date()));
                        //addVideoName.videoNameAdd(videoName);
                    }
                    // excel格式
                    if (excelTypes.contains(fileTpe)){
                        System.out.println("开始上传的文件名"+filename);
                        System.out.println(Share.path+filename);
                        try{
                            CreateSQL.createTable(Share.path+filename);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
