package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

import adapter.MyDissAdapter;

/**
 * Created by zy on 15-10-20.
 */
public class MyPreference {
    private static MyPreference preference;
    private final static String name = "leetcode";
    private final int mode = Context.MODE_PRIVATE;

    private final static String nameKey = "username";
    private final static String pwdKey = "pwd";

    private MyPreference(){

    }

    public static MyPreference getInstance(){
        if (preference == null){
            preference = new MyPreference();
        }

        return preference;
    }

    public String getUsername(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, mode);
        String username = sharedPreferences.getString(nameKey,"");
        Logger.d("username in pre  " + username);

        return username;
    }

    public String getPwd(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,mode);
        String pwd = sharedPreferences.getString(pwdKey, "");

        return pwd;
    }

    public void setNameAndPwd(Context context,String usernmae,String pwd){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,mode);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(nameKey,usernmae).commit();
        editor.putString(pwdKey,pwd).commit();
    }

    public void setName(Context context,String username){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,mode);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(nameKey,username).commit();
    }

    public void setPwd(Context context,String pwd){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,mode);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(pwdKey,pwd).commit();
    }

}
