package utils;

/**
 * @Package utils
 * @ClassName ParseDataUtil
 * @Description 处理客户端发送过来的消息
 * @Date 19/12/11 19:36
 * @Author LIM
 * @Version V1.0
 */
public class ParseDataUtil {
    /**
     *
     * 下载文件消息格式： Download["fileName":"fileSize":"result"]
     * 参数说明：
     *        fileName   要下载的文件的名字
     *        fileSize   要下载的文件的大小
     *        result     结果
     */
    public static String getDownFileName(String data){
        return data.substring(data.indexOf("[")+1,data.indexOf(":"));
    }
    public static String getDownFileSize(String data){
        return data.substring(data.indexOf(":")+1,data.lastIndexOf(":"));
    }
    public static String getDownResult(String data){
        return data.substring(data.lastIndexOf(":")+1,data.length()-1);
    }



    /**
     *
     *    上传文件消息格式： Upload["fileName":"fileSize":result]
     *     参数说明：
     *          fileName   要上传的文件的名字
     *          fileSize   要上传的文件的大小
     *          result     结果
     */
    public static String getUploadFileName(String data){
        return data.substring(data.indexOf("[")+1,data.indexOf(":"));
    }
    public static String getUploadFileSize(String data){
        return data.substring(data.indexOf(":")+1,data.lastIndexOf(":"));
    }
    public static String getUploadResult(String data){
        return data.substring(data.lastIndexOf(":")+1,data.length()-1);
    }

    /**
     *
     *    删除文件消息格式： Delete["fileName"]
     *     参数说明：
     *          fileName   要删除的文件的名字
     *          fileSize   要删除的的文件的大小
     *          result     结果
     */
    public static String getDeleteFileName(String data){
        return data.substring(data.indexOf("[")+1,data.indexOf(":"));
    }
    public static String getDeleteFileSize(String data){
        return data.substring(data.indexOf(":")+1,data.lastIndexOf(":"));
    }
    public static String getDeleteResult(String data){
        return data.substring(data.lastIndexOf(":")+1,data.length()-1);
    }

    /**
     * 返回文件列表格式 GroupFileList["fileName":"fileName":"fileName"...]
     */
    public static String[] getFileList(String data){
        if (data.length()==0){
            return null;
        }else {
            int i = data.length()-1;
            System.out.println("data.length()-1:"+i);
            System.out.println(data);
            System.out.println(data.indexOf("[")+1);
            System.out.println(data.length()-1);
            String list = data.substring(data.indexOf("[")+1,data.length()-1);
            System.out.println(list);
            return  list.split(":");
        }
    }
}