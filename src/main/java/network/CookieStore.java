package network;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by zy on 15-10-17.
 */
public class CookieStore {
    private static Map<String,String> cookie = new HashMap<>();

    public synchronized static void setCookie(Map<String,String> cookie){
        CookieStore.cookie = cookie;
    }

    public synchronized static Map<String,String> getCookie(){
        return CookieStore.cookie;
    }

    public static String myToString(){
        StringBuilder builder = new StringBuilder();
        Set<String> set = cookie.keySet();

        for(String s : set){
            builder.append(s)
                    .append("    :   ")
                    .append(cookie.get(s))
                    .append("\n");
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        String s = "";

        Set<String> set = cookie.keySet();

        return super.toString();
    }
}
