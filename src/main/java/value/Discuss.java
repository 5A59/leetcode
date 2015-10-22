package value;

import java.io.Serializable;

/**
 * Created by zy on 15-10-18.
 */
public class Discuss implements Serializable{
    private String title;
    private String url;

    public Discuss() {
    }

    public Discuss(String title, String url) {

        this.title = title;
        this.url = url;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
