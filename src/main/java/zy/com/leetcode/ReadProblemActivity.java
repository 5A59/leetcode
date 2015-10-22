package zy.com.leetcode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

import network.LeetCodeSpider;
import network.UrlSet;
import utils.Logger;
import value.DetailPro;
import value.Problem;

/**
 * Created by zy on 15-10-17.
 */
public class ReadProblemActivity extends AppCompatActivity implements RapidFloatingActionContentLabelList
        .OnRapidFloatingActionContentLabelListListener {

    private Problem problem;
    private DetailPro detailPro;

    private TextView proText;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;
    private RapidFloatingActionContentLabelList rfabActionContentLabelList;

    private ExitActivityTransition exitActivityTransition;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            detailPro = (DetailPro) msg.obj;

            if (detailPro.getHtml() != null){
                proText.setText(Html.fromHtml(detailPro.getHtml()));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_pro);

        exitActivityTransition = ActivityTransition.with(getIntent())
                .to(findViewById(R.id.rfab)).start(savedInstanceState);

        init();
    }

    private void init(){
        initData();
        initToolBar();
        initView();
        getProblem();
    }

    private void initData(){
        problem = (Problem) getIntent().getSerializableExtra("pro");
    }

    private void initToolBar(){
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFinish();
//                finish();
            }
        });
    }

    private void initView(){
        collapsingToolbarLayout = (CollapsingToolbarLayout) this.findViewById(R.id.coll);
        collapsingToolbarLayout.setTitle(problem.getTitle());

        proText = (TextView) this.findViewById(R.id.text_pro);

        rfaBtn = (RapidFloatingActionButton) this.findViewById(R.id.rfab);
        rfaLayout = (RapidFloatingActionLayout) this.findViewById(R.id.rfal);

        List<RFACLabelItem> items = new ArrayList<>();

        items.add(new RFACLabelItem<Integer>()
                        .setLabel("baidu")
                        .setResId(R.mipmap.loading)
                        .setIconNormalColor(getResources().getColor(R.color.colorPrimary))
                        .setIconPressedColor(getResources().getColor(R.color.blue))
                        .setWrapper(0)
        );

        items.add(new RFACLabelItem<Integer>()
                        .setLabel("Discuss")
                        .setResId(R.mipmap.loading)
                        .setIconNormalColor(getResources().getColor(R.color.colorPrimary))
                        .setIconPressedColor(getResources().getColor(R.color.blue))
                        .setWrapper(0)
        );

        items.add(new RFACLabelItem<Integer>()
                        .setLabel("share")
                        .setResId(R.mipmap.loading)
                        .setIconNormalColor(getResources().getColor(R.color.colorPrimary))
                        .setIconPressedColor(getResources().getColor(R.color.blue))
                        .setWrapper(0)
        );

        initFLAB(items);
    }

    private void initFLAB(List<RFACLabelItem> items){

        rfabActionContentLabelList =
                new RapidFloatingActionContentLabelList(this);

        rfabActionContentLabelList.setOnRapidFloatingActionContentLabelListListener(this);
        rfabActionContentLabelList.setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(this, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(this, 5));
        rfabHelper = new RapidFloatingActionHelper(this,rfaLayout,
                rfaBtn,rfabActionContentLabelList).build();

    }

    private void getProblem(){
        new Thread(){
            @Override
            public void run() {
                Logger.d("url  " + problem.getUrl());
                DetailPro detailPro = LeetCodeSpider.getInstance().getPro(UrlSet.mainUrl + problem.getUrl());
                handler.sendMessage(handler.obtainMessage(0, detailPro));
            }
        }.start();
    }

    @Override
    public void onRFACItemLabelClick(int i, RFACLabelItem rfacLabelItem) {
        clickItem(i);
    }

    @Override
    public void onRFACItemIconClick(int i, RFACLabelItem rfacLabelItem) {
        clickItem(i);
    }

    private void clickItem(int i){
        switch (i){
            case 0:
                clickBaidu();
                break;
            case 1:
                clickDiss();
                break;
            case 2:
                clickShare();
                break;
        }
    }

    private void clickBaidu(){
        closeOrOpenMent();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse(UrlSet.baiduUrl + detailPro.getSearchWord());
        intent.setData(uri);
        startActivity(intent);
    }

    private void clickDiss(){
        closeOrOpenMent();
        Intent intent = new Intent(this, DiscussActivity.class);
        intent.putExtra("pro", detailPro);
        startActivity(intent);
    }

    private void clickShare(){
        closeOrOpenMent();
    }

    private void closeOrOpenMent(){
        if (rfaLayout.isExpanded()){
            rfaLayout.collapseContent();
            return;
        }
        rfaLayout.expandContent();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (rfaLayout.isExpanded()){
            rfaLayout.collapseContent();
            return;
        }
        myFinish();
    }

    private void myFinish(){
        exitActivityTransition.exit(ReadProblemActivity.this);
    }
}
