package utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.util.StringUtils;

import java.io.*;

/**
 * @Package utils
 * @ClassName ParseTextTypeUtil
 * @Description 处理文本文件
 * @Date 19/12/11 19:36
 * @Author LIM
 * @Version V1.0
 */
public class ParseTextTypeUtil {
    private static final String DEFULTCHARSET = "GB2312";

    /**
     * 读取文件统一入口
     * @param path
     * @return
     */
    public static String importFile(String path,String charset){
        String resTextString = null;
        if (StringUtils.isEmpty(path)) {
            return resTextString;
        }
        try {
            int lastIndexOf = path.lastIndexOf(".");
            String type = path.substring(lastIndexOf+1);
            if ("txt".equals(type)) {
                resTextString = importTxt(path,charset);
            }else if ("doc".equals(type)) {
                resTextString = importWord3(path);
            }else if ("docx".equals(type)) {
                resTextString = importWord7(path);
            }else if ("pptx".equals(type)||"ppt".equals(type)){
                resTextString = ppt2String(path);
            }else if ("pdf".equals(type)){
                resTextString = pdf2String(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resTextString;
    }


    /**
     * 读取word2003
     * @throws
     */
    private static String importWord3(String path){
        String textString=null;
        FileInputStream inputStream =null;
        try {
            inputStream = new FileInputStream(path);
            HWPFDocument doc = new HWPFDocument(inputStream);
            textString = doc.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream!=null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return textString;
    }

    /**
     * 读取word2007
     * @param path
     * @return
     */
    private static String importWord7(String path){
        String text = null;
        OPCPackage openPackage =null;
        try {
            openPackage = POIXMLDocument.openPackage(path);
            XWPFWordExtractor word = new XWPFWordExtractor(openPackage);
            text = word.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (openPackage!=null) {
                try {
                    openPackage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return text;
    }

    /**
     * 导入txt 文件
     * @param path
     * @return
     */
    private static String importTxt(String path,String charset){
        String resText=null;
        if (StringUtils.isEmpty(charset)) {
            charset=DEFULTCHARSET;
        }
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(path),charset);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line=br.readLine())!=null) {
                resText= resText + line +"\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader!=null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resText;
    }

    /**
     * 读取ppt
     */
    private static String ppt2String(String path) throws IOException{
        FileInputStream fi=new FileInputStream(path);
        PowerPointExtractor ppExtractor=new PowerPointExtractor(fi);

        return ppExtractor.getText();
    }

    /**
     * 读取pdf
     * @return
     * @throws IOException
     */
    private static String pdf2String(String path) throws IOException {
        File file = new File(path);
        PDDocument document = PDDocument.load(file);
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setSortByPosition(false);
        String result = stripper.getText(document);
        document.close();
        return result;
    }

}
