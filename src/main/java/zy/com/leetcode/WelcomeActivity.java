package zy.com.leetcode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import network.LeetCodeSpider;
import utils.Logger;
import utils.MyPreference;

/**
 * Created by zy on 15-10-20.
 */
public class WelcomeActivity extends AppCompatActivity{

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String name = (String) msg.obj;
            Logger.d("name  :  " + name);
            timerStart(name);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        init();
    }

    private void init(){
        String username = MyPreference.getInstance().getUsername(this);
        String pwd = MyPreference.getInstance().getPwd(this);

        Logger.d("iiii " + username + "   " + pwd);

        if (username.isEmpty() || pwd.isEmpty()){
            timerStart("");
            return;
        }

        login(username,pwd);
    }

    private void timerStart(final String name){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                intent.putExtra("name",name);
                startActivity(intent);
                finish();
            }
        },1500);
    }

    private void login(final String username, final String pwd){
        new Thread(){
            @Override
            public void run() {
                String name = LeetCodeSpider.getInstance().login(username,pwd);
                Logger.d("username  " + username + "   " + pwd);
                handler.sendMessage(handler.obtainMessage(0,name));
            }
        }.start();
    }

}
