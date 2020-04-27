package dao.impl;

import dao.IAddFileDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.Image;
import pojo.Text;
import pojo.Video;
import utils.ConnectDB;

import java.io.IOException;
import java.sql.*;

/**
 * @Package file.dao.impl
 * @ClassName FilDaoImpl
 * @Description 添加接口实现类
 * @Date 19/12/8 16:19
 * @Author LIM
 * @Version V1.0
 */
public class AddFileDaoImpl implements IAddFileDao {
    private static final Logger logger = LoggerFactory.getLogger(AddFileDaoImpl.class);
    @Override
    public void textAdd(Text text) {
        String sql="insert into table_text values(null,?,?,?)";
        try(Connection c= ConnectDB.getContext();PreparedStatement ps=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,text.getFilename());
            ps.setString(2,text.getContext());
            ps.setString(3, text.getTime());

            ps.execute();
            logger.info("文本文件插入成功");

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                int id = rs.getInt(1);
                text.setId(id);
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 插入image文件
     *
     * @param image
     */
    @Override
    public void imageAdd(Image image) {
        String sql="insert into table_image values(null,?,?,?)";
        try(Connection c=ConnectDB.getContext();PreparedStatement ps=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,image.getFilename());
            ps.setBinaryStream(2,image.getImage(),image.getImage().available());
            ps.setString(3, image.getTime());

            ps.execute();

            logger.info("图像文件插入成功");

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                int id = rs.getInt(1);
                image.setId(id);
            }
        } catch (SQLException | IOException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 插入video文件
     *
     * @param video
     */
    @Override
    public void videoAdd(Video video) {
        String sql="insert into table_video values(null,?,?,?)";
        try(Connection c=ConnectDB.getContext();PreparedStatement ps=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,video.getFilename());
            ps.setBinaryStream(2,video.getVideo(),video.getVideo().available());
            ps.setString(3, video.getTime());
            ps.execute();
            logger.info("视频文件插入成功");

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                int id = rs.getInt(1);
                video.setId(id);
            }
        } catch (SQLException | IOException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }
}
