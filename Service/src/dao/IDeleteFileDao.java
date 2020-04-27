package dao;

/**
 *@Package dao
 *@InterfaceName IDeleteFileDao
 *@Description 删除接口
 *@Date 19/12/14 20:17
 *@Author LIM
 *@Version V1.0
 */
public interface IDeleteFileDao {
    /**
     * 通过文件名删除
     * @param fileName
     * @param table
     */
    void delete(String table,String fileName);

    /**
     * 删除excel表格
     * @param fileName
     */
    void deleteExcel(String fileName);
}
