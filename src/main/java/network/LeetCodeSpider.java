package network;

import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.Logger;
import value.DetailPro;
import value.Discuss;
import value.Problem;

/**
 * Created by zy on 15-10-16.
 */
public class LeetCodeSpider {

    private static LeetCodeSpider leetCodeSpider = null;

    private static Map<String,String> header;

    private Spider spider;

    private LeetCodeSpider(){
        spider = Spider.getInstance();

        header = new HashMap<>();
        header.put(Spider.HttpHeader.userAgentTitle,Spider.HttpHeader.userAgent);
        header.put("Referer",UrlSet.loginUrl);
        header.put("Host","leetcode.com");
    }

    public static LeetCodeSpider getInstance(){
        if (leetCodeSpider == null){
            leetCodeSpider = new LeetCodeSpider();
        }

        return leetCodeSpider;
    }

    /**
     *
     * @param username
     * @param passward
     * @return
     */
    public String login(String username,String passward){
        Logger.d("login  :  " + username + "  " + passward);

        if (username == null || passward == null){
            return "";
        }

        List<String> tags = new ArrayList<>();

        String csrfKey = "csrfmiddlewaretoken";
        String userKey = "login";
        String pwdKey = "password";
        String remKey = "remember";

        tags.add("[name=" + csrfKey + "]");

        Map<String,String> data = new HashMap<>();

        try {
            spider.get(UrlSet.loginUrl, null, null, null, null, null, true);

            data.put(csrfKey, CookieStore.getCookie().get("csrftoken"));

        } catch (Exception e) {
            e.printStackTrace();
            Logger.d("except when get csrf");
            data.put(csrfKey, "");
        }

        data.put(userKey,username);
        data.put(pwdKey,passward);
        data.put(remKey,"");

        try {
            Logger.d("start login");
            String html = spider.post(UrlSet.loginUrl,header,CookieStore.getCookie(),data,null,null,true);

            tags.clear();
            tags.add("[class=dropdown-toggle]");
            List<Element> elementList = spider.spider(html,tags);
            if (elementList != null && elementList.size() > 1){
                Element e = elementList.get(1);
                return e.text();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("except when login");
        }

        return null;
    }

    public int register(String username,String passward){
        return ErrorCode.OK;
    }

    public List<Problem> getAlgo(){

        List<String> tags = new ArrayList<>();
        tags.add("tr");

        try {
            String html = spider.get(UrlSet.algoUrl, header, CookieStore.getCookie(), null, null, null, false);

            List<Element> elementList = spider.spider(html,tags);

            List<Problem> problemList = new ArrayList<>();

            for (Element e : elementList){
                Problem p = new Problem();
                p.setAc(e.child(0).html().contains("ac"));
                p.setId(e.child(1).text());
                p.setTitle(e.child(2).text());
                p.setUrl(e.child(2).child(0).attr("href"));
                p.setAcc(e.child(3).text());
                p.setDifficulty(e.child(4).text());

                problemList.add(p);
            }

            return problemList;
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("except in get algo");
        }

        return null;
    }

    public DetailPro getPro(String url){
        List<String> tags = new ArrayList<>();
        tags.add("[class=question-content]");

        DetailPro pro = new DetailPro();

        try {
            String html = spider.get(url, header, CookieStore.getCookie(), null, null, null, false);
            List<Element> elementList = spider.spider(html, tags);
//            StringBuilder builder = new StringBuilder();

            if (elementList != null && !elementList.isEmpty()){
//                builder.append(elementList.get(0).html());

                pro.setHtml(elementList.get(0).html());
                pro.setSearchWord(elementList.get(0).text());
            }

            tags.clear();
            tags.add("[class=action]");
            elementList = spider.spider(html,tags);
            if (elementList != null && !elementList.isEmpty()){
                Element e = elementList.get(0);
                pro.setDisUrl(e.child(0).attr("href"));
            }
            return pro;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String,Object> getDisc(String url){
        List<String> tags = new ArrayList<>();
        tags.add("[class=qa-q-item-title]");

        try {
            String html = spider.get(url, header, CookieStore.getCookie(), null, null, null, false);

            List<Element> elementList = spider.spider(html, tags);

            List<Discuss> discussList = new ArrayList<>();

            for (Element e : elementList){
                Discuss d = new Discuss();
                Element ee = e.child(0);

                d.setTitle(ee.child(0).text());
                d.setUrl(ee.attr("href"));

                discussList.add(d);
            }

            Map<String,Object> map = new HashMap<>();
            map.put("list", discussList);

            String next = getNext(html);

            if (next == null){
                return map;
            }
            map.put("next",next.replace("../..",""));

            return map;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getNext(String html){
        List<String> tags = new ArrayList<>();
        tags.add("[class=qa-page-next]");

        List<Element> elementList = spider.spider(html,tags);

        if (elementList != null && !elementList.isEmpty()){
            Element e = elementList.get(0);

            return e.attr("href");
        }

        return null;
    }

    public String getDetailDisc(String url){
        List<String> tags = new ArrayList<>();
        tags.add("pre");

        try {
            String html = spider.get(url,header,CookieStore.getCookie(),null,null,null,false);
            List<Element> elementList = spider.spider(html, tags);
            if (elementList != null && !elementList.isEmpty()){
                Element e = elementList.get(0);

                return e.html();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "none";
    }
}
