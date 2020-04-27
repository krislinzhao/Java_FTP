package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static utils.ReadExcel.getColumnNumber;
import static utils.ReadExcel.readExcel;

/**
 * @Package utils
 * @ClassName ParseExcl
 * @Description 动态创建数据库
 * @Date 19/12/11 19:43
 * @Author LIM
 * @Version V1.0
 */
public class CreateSQL {
    private static final Logger logger = LoggerFactory.getLogger(CreateSQL.class);

    public static void createTable(String path) throws Exception {

        String[] fileNames = path.split("/");
        String fileName = fileNames[fileNames.length-1];
        String tableName = fileName.substring(0,fileName.indexOf("."));
        logger.info("tableName:{}", tableName);


        List<List<Object>> excleDataList=readExcel(new File(path));



        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();

        sb1.append("DROP TABLE IF EXISTS ").append(tableName).append(";");

        //创建表
        sb.append("CREATE TABLE ").append(tableName).append("( ");
        sb.append("\n");
        for (int j=0;j<getColumnNumber()-1;j++){
            sb.append(excleDataList.get(0).get(j)).append(" ").append("varchar(255)").append(",").append("\n");
        }
        sb.append(excleDataList.get(0).get(getColumnNumber()-1)).append(" ").append("varchar(255)").append("\n");
        sb.append(")DEFAULT CHARSET=utf8;");

        System.out.println(sb.toString());
        System.out.println(sb1.toString());
        logger.info("动态创建的SQL语句:{}", sb.toString());

        try(Connection c= ConnectDB.getContext();
            Statement statement = c.createStatement()) {
            statement.execute(sb1.toString());
            statement.execute(sb.toString());
            logger.info("excel动态创建表成功");
        }catch (Exception e){
            logger.info(e.getMessage());
            e.printStackTrace();
        }

        //插入数据
        for(int i=1;i<excleDataList.size();i++){
            StringBuffer sb2 = new StringBuffer();
            sb2.append("insert into ").append(tableName).append(" values(");
            for (int j=0;j<getColumnNumber()-1;j++){
                sb2.append("'").append(excleDataList.get(i).get(j)).append("'").append(",");
            }
            sb2.append("'").append(excleDataList.get(i).get(getColumnNumber() - 1)).append("'").append(");");
            System.out.println(sb2.toString());
            logger.info("动态插入的SQL语句:{}", sb2.toString());
            try(Connection c = ConnectDB.getContext();
                Statement statement = c.createStatement()) {
                statement.execute(sb2.toString());
            }catch (Exception e){
                logger.info(e.getMessage());
                e.printStackTrace();
            }
        }
        logger.info("excel动态插入数据成功");
    }
}
