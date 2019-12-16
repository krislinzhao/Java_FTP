package pojo;

/**
 * @Package file.pojo
 * @ClassName VideoName
 * @Description TODO
 * @Date 19/12/10 16:29
 * @Author LIM
 * @Version V1.0
 */
public class VideoName {
    int id;
    String filename;
    String videoName;
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

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
