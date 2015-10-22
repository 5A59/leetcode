package zy.com.leetcode;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import network.UrlSet;
import value.Discuss;

/**
 * Created by zy on 15-10-18.
 */
public class ReadDiscussActivity extends AppCompatActivity{

//    private TextView proText;
    private WebView webView;

    private Discuss discuss;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            proText.setText(Html.fromHtml((String)msg.obj));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_disc);

        init();
    }


    private void init(){
        initData();
        initToolBar();
        initView();
        getDetailDisc(UrlSet.discUrl + discuss.getUrl().replace("../..",""));
    }

    private void initData(){
        discuss = (Discuss) getIntent().getSerializableExtra("dis");
    }

    private void initToolBar(){
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        webView = (WebView) this.findViewById(R.id.web_pro);
        webView.getSettings().setJavaScriptEnabled(true);
    }

    private void getDetailDisc(final String url){
        webView.loadUrl(url);
//        new Thread(){
//            @Override
//            public void run() {
//                String data = LeetCodeSpider.getInstance().getDetailDisc(url);
//                handler.sendMessage(handler.obtainMessage(0,data));
//            }
//        }.start();
    }

}
