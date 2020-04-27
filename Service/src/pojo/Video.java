package pojo;

import java.io.FileInputStream;

/**
 * @Package file.pojo
 * @ClassName Video
 * @Description 视频格式的实体类
 * @Date 19/12/10 10:55
 * @Author LIM
 * @Version V1.0
 */
public class Video {
    int id;
    String filename;
    FileInputStream video;
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

    public FileInputStream getVideo() {
        return video;
    }

    public void setVideo(FileInputStream video) {
        this.video = video;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", video='" + video + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
