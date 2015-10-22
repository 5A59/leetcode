package zy.com.leetcode;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.srx.widget.PullCallback;
import com.srx.widget.PullToLoadView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapter.MyDissAdapter;
import network.LeetCodeSpider;
import network.UrlSet;
import utils.Logger;
import value.DetailPro;
import value.Discuss;

/**
 * Created by zy on 15-10-18.
 */
public class DiscussActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private PullToLoadView pullToLoadView;

    private DetailPro problem;
    private List<Discuss> discussList;

    private MyDissAdapter adapter;

    private String next;
    private boolean isLoading;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            pullToLoadView.setComplete();
            isLoading = false;

            switch (msg.what){
                case 0: //refresh
                    discussList.clear();
                    break;
                case 1: //next
                    break;
            }

            if (msg.obj != null){
                Logger.d("handler");
                Map<String,Object> map = (Map<String, Object>) msg.obj;

                discussList.addAll((List<Discuss>) map.get("list"));
                next = (String) map.get("next");
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_diss);

        init();
    }

    private void init(){
        initData();
        initToolbar();
        initView();
    }

    private void initData(){
        problem = (DetailPro) getIntent().getSerializableExtra("pro");
        discussList = new ArrayList<>();
        adapter = new MyDissAdapter(this, discussList);
        isLoading = false;
        next = "";
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitle("diss");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView(){
        pullToLoadView = (PullToLoadView) this.findViewById(R.id.recycler);

        recyclerView = pullToLoadView.getRecyclerView();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MainActivity.MyItemDecoration(10));

        pullToLoadView.isLoadMoreEnabled(true);
        pullToLoadView.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                if (next == null){
                    return;
                }
                getDiss(UrlSet.discUrl + next.replace("../..", ""),1);
            }

            @Override
            public void onRefresh() {
                getDiss(UrlSet.mainUrl + problem.getDisUrl(),0);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return next == null ? true : false;
            }
        });
        pullToLoadView.initLoad();
    }

    private void getDiss(final String url, final int what){
        isLoading = true;
        new Thread(){
            @Override
            public void run() {
                Map<String,Object> map = LeetCodeSpider.getInstance().getDisc(url);
                handler.sendMessage(handler.obtainMessage(what, map));
            }
        }.start();
    }

    private void getNext(final String url){
        isLoading = true;
        new Thread(){
            @Override
            public void run() {
                next(url);
            }
        }.start();
    }

    private void next(String url){
        String next = LeetCodeSpider.getInstance().getNext(url);
        handler.sendMessage(handler.obtainMessage(1, next));
    }
}

