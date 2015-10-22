package value;

import java.io.Serializable;

/**
 * Created by zy on 15-10-18.
 */
public class DetailPro implements Serializable{
    private String disUrl;
    private String searchWord;
    private String html;

    public DetailPro() {

    }

    public DetailPro(String disUrl, String searchWord, String html) {

        this.disUrl = disUrl;
        this.searchWord = searchWord;
        this.html = html;
    }

    public String getDisUrl() {
        return disUrl;
    }

    public void setDisUrl(String disUrl) {
        this.disUrl = disUrl;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

}
