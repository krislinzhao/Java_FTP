package dao;


import pojo.Image;
import pojo.Text;
import pojo.Video;

/**
 * @Package file.dao
 * @InterfaceName FileDao
 * @Description 添加接口
 * @Date 19/12/8 16:12
 * @Author LIM
 * @Version V1.0
 */
public interface IAddFileDao {
    /**
     * 插入text文件
     * @param text
     */
    void textAdd(Text text);

    /**
     * 插入image文件
     * @param image
     */
    void imageAdd(Image image);

    /**
     * 插入video文件
     * @param video
     */
    void videoAdd(Video video);
}
