package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Package utils
 * @ClassName ConnectDB
 * @Description 连接数据的工具类
 * @Date 19/12/14 20:32
 * @Author LIM
 * @Version V1.0
 */
public class ConnectDB {
    public ConnectDB(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Connection getContext() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/file?characterEncoding=UTF-8",
                "root", "123456");
    }
}
