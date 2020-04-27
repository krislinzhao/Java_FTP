package dao.impl;

import dao.IDeleteFileDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Share;
import utils.ConnectDB;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *@Package dao.impl
 *@ClassName DeleteFileDaoImpl
 *@Description 删除接口实现类
 *@Date 19/12/14 20:18
 *@Author LIM
 *@Version V1.0
 */
public class DeleteFileDaoImpl implements IDeleteFileDao {
    private static final Logger logger = LoggerFactory.getLogger(DeleteFileDaoImpl.class);
    /**
     * 通过文件名删除
     *
     * @param fileName
     */
    @Override
    public void delete(String table,String fileName) {
        String sql="delete from "+table+" where filename="+"'"+ fileName +"'" ;
        try(Connection c= ConnectDB.getContext();
            Statement statement=c.createStatement()){
            statement.execute(sql);
            File file = new File(Share.path,fileName);
            file.delete();
            logger.info("删除成功");
        } catch (SQLException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 删除excel表格
     *
     * @param fileName
     */
    @Override
    public void deleteExcel(String fileName) {
        String name = fileName.substring(0,fileName.indexOf("."));
        System.out.println(name);
        String sql="DROP TABLE "+name;
        try(Connection c=ConnectDB.getContext();
        Statement statement=c.createStatement()) {
            statement.execute(sql);
            File file = new File(Share.path,fileName);
            file.delete();
            logger.info("excel删除成功");
        } catch (SQLException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }
}
