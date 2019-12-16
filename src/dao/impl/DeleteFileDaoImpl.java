package dao.impl;

import dao.IDeleteFileDao;
import server.Share;
import utils.ConnectDB;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *@Package dao.impl
 *@ClassName DeleteFileDaoImpl
 *@Description TODO
 *@Date 19/12/14 20:18
 *@Author LIM
 *@Version V1.0
 */
public class DeleteFileDaoImpl implements IDeleteFileDao {
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
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
