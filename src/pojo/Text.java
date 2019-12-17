package pojo;

/**
 * @Package file.pojo
 * @ClassName Text
 * @Description 文本格式的实体类
 * @Date 19/12/8 16:08
 * @Author LIM
 * @Version V1.0
 */
public class Text {
    int id;
    String filename;
    String context;
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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Text{" +
                "id=" + id +
                ", context='" + context + '\'' +
                ", date=" + time +
                '}';
    }
}
