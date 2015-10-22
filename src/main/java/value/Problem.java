package value;

import java.io.Serializable;

/**
 * Created by zy on 15-10-17.
 */
public class Problem implements Serializable{
    private String id;
    private String title;
    private String difficulty;
    private String url;
    private String acc;

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    private boolean ac;

    public Problem() {

    }

    public Problem(String id, String title, String difficulty, String url, String acc, boolean ac) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.url = url;
        this.acc = acc;
        this.ac = ac;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getUrl() {
        return url;
    }

    public boolean isAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }
}
