package utils;

import dao.IDeleteFileDao;
import dao.impl.DeleteFileDaoImpl;
import org.junit.Test;

/**
 * @Package utils
 * @ClassName DeleteTest
 * @Description TODO
 * @Date 19/12/15 10:01
 * @Author LIM
 * @Version V1.0
 */
public class DeleteTest {
    @Test
    public void testDelete(){
        IDeleteFileDao deleteFileDao = new DeleteFileDaoImpl();
        deleteFileDao.delete("table_text","第二章作业.docx");
    }
    @Test
    public void testDeleteExcel(){
        IDeleteFileDao deleteFileDao = new DeleteFileDaoImpl();
        deleteFileDao.deleteExcel("username.xls");
    }
}
