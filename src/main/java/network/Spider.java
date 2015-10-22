package network;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zy on 15-9-25.
 */
public class Spider {

    static Spider spider = null;

    private Spider(){

    }

    static public Spider getInstance(){
        if (spider == null){
            spider = new Spider();
        }

        return spider;
    }

    /**
     *
     * @param url
     * @param header
     * @param cookie if cookie is null,we do not add a cookie
     * @param data
     * @param oCode the charset of webpage , if oCode is null,we use the default code of webpage
     * @param nCode the charset we want to translate from oCode
     * @return
     * @throws IOException
     */
    public String get(String url, Map<String, String> header,Map<String,String> cookie,Map<String,String> data,
                          String oCode,String nCode,boolean saveCookie) throws IOException {

        return connect(url,header,cookie,data,oCode,nCode, Connection.Method.GET,saveCookie);
    }

    public String post(String url,Map<String,String> header,Map<String,String> cookie,Map<String,String> data,
                       String oCode,String nCode,boolean saveCookie) throws IOException {

        return connect(url, header, cookie, data, oCode, nCode, Connection.Method.POST, saveCookie);
    }

    private String connect(String url,Map<String,String> header,Map<String,String> cookie,Map<String,String> data,
                          String oCode,String nCode,Connection.Method method,boolean saveCookie) throws IOException{

        Connection connection = Jsoup.connect(url);

        setHeader(connection, header);
        setCookie(connection, cookie);
        setData(connection, data);

        connection.method(method);
        Connection.Response response = connection.execute();

        if (saveCookie){
            CookieStore.setCookie(response.cookies());
        }

        return codeChange(response.body(),response.charset(),oCode,nCode);
    }

    public String codeChange(String data,String defaultCode,String oCode,String nCode)
            throws UnsupportedEncodingException {

        if (nCode == null){
            return data;
        }

        if (oCode == null){
            return new String(data.getBytes(defaultCode),nCode);
        }

        return new String(data.getBytes(oCode),nCode);
    }

    public void setHeader(Connection connection,Map<String,String> header){
        Map<String,String> tmp;
        if (header == null){
            tmp = HttpHeader.getHeader();
        }else {
            tmp = header;
        }

        Set<String> set = tmp.keySet();

        for (String s : set){
            connection.header(s, tmp.get(s));
        }
    }

    public void setCookie(Connection connection,Map<String,String> cookie){
        if (cookie == null){
            return ;
        }
        connection.cookies(cookie);
    }

    public void setData(Connection connection,Map<String,String> data){
        if (data == null){
            return ;
        }
        connection.data(data);
    }

    public List<Element> spider(String url,List<String> tags,String code) throws Exception{
        if (tags == null || url == null){
            return null;
        }

        if (code == null){
            code = "utf-8";
        }
        Document document = Jsoup.parse(new URL(url).openConnection().getInputStream(), code, url);

        return spider(document,tags);
    }

    public List<Element> spider(Document document,List<String> tags){

        Elements elements = document.getAllElements();

        for (String e : tags){
            elements = elements.select(e);
        }

        return elements;
    }

    public List<Element> spider(String html,List<String> tags){
        Document document = Jsoup.parse(html);

        return spider(document,tags);
    }

    public static class HttpHeader{
        //        GET /accounts/login/ HTTP/1.1
//        Host: leetcode.com
//        User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:41.0) Gecko/20100101 Firefox/41.0
//        Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
//        Accept-Language: zh,en-US;q=0.7,en;q=0.3
//        Accept-Encoding: gzip, deflate
//        Referer: https://leetcode.com/
//        Cookie: csrftoken=nSanoPSbxSsmTr4SgmWFAMj2wQpzHBKQ; _ga=GA1.2.749777524.1443791315; __atuvc=5%7C39; _gat=1
//        Connection: keep-alive


        public static String userAgentTitle = "User-Agent";
        public static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0";

        public static String acceptTitle = "Accept";
        public static String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";

        public static String acceptLangTitle = "Accept-Language";
        public static String acceptLang = "zh,en-US;q=0.7,en;q=0.3";

        public static String acceptCodeTitle = "Accept-Encoding";
        public static String acceptCode = "gzip, deflate";

        public static Map<String,String> header;

        public static Map<String,String> getHeader(){

            if (header == null){
                header = new HashMap<>();
            }
            header.put(acceptTitle,accept);
            header.put(acceptLangTitle,acceptLang);
            header.put(acceptCodeTitle,acceptCode);
            header.put(userAgentTitle,userAgent);

            return header;
        }
    }

}
