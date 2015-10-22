package zy.com.leetcode;

import android.app.Application;
import android.os.Looper;
import android.test.ApplicationTestCase;

import network.LeetCodeSpider;
import utils.Logger;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testLogin(){
        new Thread(){
            @Override
            public void run() {
                String username = "zy0x5f3759df@outlook.com";
                String pwd = "zhangyi5";
                String html = LeetCodeSpider.getInstance().login(username,pwd);
                Logger.d(html);
            }
        }.start();
    }
}