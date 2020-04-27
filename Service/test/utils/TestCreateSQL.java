package utils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static utils.ReadExcel.getColumnNumber;
import static utils.ReadExcel.readExcel;

/**
 * @Package util
 * @ClassName TestParseExcl
 * @Description TODO
 * @Date 19/12/11 19:44
 * @Author LIM
 * @Version V1.0
 */
public class TestCreateSQL {
    static String path = "C:\\Users\\LIM\\Desktop\\username2.xls";
    @Test
    public void testExclHelper(){
       try{
           //CreateSQL.createTable(Share.path+"username2.xls");
           CreateSQL.createTable(path);
       } catch (Exception e) {
           e.printStackTrace();
       }
    }

    @Test
    public void testReadExcel(){
        try {
            List<List<Object>> excleDataList=readExcel(new File(path));
            for(int i=0;i<excleDataList.size();i++){
                for (int j=0;j<getColumnNumber();j++){
                    System.out.printf("excel中的第%d行,第%d列的值是%s%n",i+1,j+1,excleDataList.get(i).get(j).toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/file?characterEncoding=UTF-8",
                    "root", "123456");

            Statement statement = connection.createStatement();

      String sql = "CREATE TABLE username\n" +
               "(\n" +
              "姓名 VARCHAR(255),\n" +
              "性别 VARCHAR(255),\n" +
              "年龄 VARCHAR(255),\n" +
              "学号 VARCHAR(255),\n" +
              "籍贯 VARCHAR(255),\n" +
              "个人简介 VARCHAR(255),\n" +
              "生活照 VARCHAR(255)\n" +
              ")";

            String sql1 = "CREATE TABLE Persons\n" +
                    "(\n" +
                    "Id_P int,\n" +
                    "LastName varchar(255),\n" +
                    "FirstName varchar(255),\n" +
                    "Address varchar(255),\n" +
                    "City varchar(255)\n" +
                    ")";

            statement.execute("DROP TABLE IF EXISTS username");
            statement.execute(sql);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
