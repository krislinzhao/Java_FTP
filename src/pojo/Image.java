package pojo;

import java.io.FileInputStream;

/**
 * @Package file.pojo
 * @ClassName Image
 * @Description 图片格式的实体类
 * @Date 19/12/10 10:55
 * @Author LIM
 * @Version V1.0
 */
public class Image {
    int id;
    String filename;
    FileInputStream image;
    String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public FileInputStream getImage() {
        return image;
    }

    public void setImage(FileInputStream image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", context='" + image + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
