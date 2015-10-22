package zy.com.leetcode;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.shamanland.fab.FloatingActionButton;
import com.shamanland.fab.ShowHideOnScroll;
import com.srx.widget.PullCallback;
import com.srx.widget.PullToLoadView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import adapter.MyProblemAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import network.CookieStore;
import network.LeetCodeSpider;
import utils.Logger;
import utils.MyPreference;
import value.Login;
import value.Problem;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private CircleImageView headImg;
    private TextView nameText;

    private RecyclerView recyclerView;
    private MyProblemAdapter adapter;
    private PullToLoadView pullToLoadView;

    //floating action button
    private FloatingActionButton floatingActionButton;

    private List<Problem> problemList;
    private List<Problem> copyProList;

    private boolean isLoading;
    private boolean close;

    private String name;

    private Random random;

    private int curItem;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isLoading = false;
            pullToLoadView.setComplete();

            List<Problem> p = (List<Problem>) msg.obj;
            if (p != null && !p.isEmpty()){
                p.remove(0);
                p.remove(0);
                problemList.clear();
                problemList.addAll(p);
            }

            if (curItem == 0){
                adapter.notifyDataSetChanged();
            }else if (curItem == 1){
                getAc();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        initToolBar();
        initData();
        initView();
    }

    private void initToolBar(){
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeOrOpenDrawer();
            }
        });
    }

    private void initData(){
        problemList = new ArrayList<>();
        copyProList = new ArrayList<>();
        isLoading = false;
        name = getIntent().getStringExtra("name");

        random = new Random();
        curItem = 0;
    }

    private void initView(){
        drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer);
        navigationView = (NavigationView) this.findViewById(R.id.navigation);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,0,0);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);

        pullToLoadView = (PullToLoadView) this.findViewById(R.id.recycler);
        pullToLoadView.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefresh() {
                getAlgo();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return false;
            }
        });
        pullToLoadView.initLoad();

        floatingActionButton = (FloatingActionButton) this.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomPro();
            }
        });

        recyclerView = pullToLoadView.getRecyclerView();
        adapter = new MyProblemAdapter(this,problemList);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MyItemDecoration(10));
        recyclerView.setOnTouchListener(new ShowHideOnScroll(floatingActionButton));

        initDrawer();
    }

    private void initDrawer(){
        nameText = (TextView) navigationView.findViewById(R.id.text_name);

        if (name != null && !name.isEmpty()){
            nameText.setText(name);
        }

        headImg = (CircleImageView) navigationView.findViewById(R.id.img_head);
        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Login.isLogin) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                closeOrOpenDrawer();
                switch (menuItem.getItemId()) {
                    case R.id.menu_algo:
                        getAllPro();
                        menuItem.setChecked(true);
                        break;
                    case R.id.menu_ac:
                        getAc();
                        menuItem.setChecked(true);
                        break;
                    case R.id.menu_setting:
                        setting();
                        break;
                    case R.id.menu_logout:
                        logout();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if (resultCode == RESULT_OK){
                    name = data.getStringExtra("name");
                    nameText.setText(name);
                    pullToLoadView.initLoad();
                }else{
                    Toast.makeText(MainActivity.this,"login failed,please check your network or username and pwd",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void closeOrOpenDrawer(){
        if (drawerLayout.isDrawerOpen(navigationView)){
            drawerLayout.closeDrawer(navigationView);
            return ;
        }
        drawerLayout.openDrawer(navigationView);
    }

    private void getAlgo(){
        isLoading = true;
        new Thread(){
            @Override
            public void run() {
                List<Problem> problemList = LeetCodeSpider.getInstance().getAlgo();
                handler.sendMessage(handler.obtainMessage(0,problemList));
            }
        }.start();

    }

    public static class MyItemDecoration extends RecyclerView.ItemDecoration{

        int dec = 0;

        public MyItemDecoration(int dec){
            this.dec = dec;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, dec);
        }
    }

    private void back(){
        if (close){
            System.exit(0);
            return;
        }

        Toast.makeText(this,"again to exit",Toast.LENGTH_SHORT).show();

        close = true;
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                close = false;
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        back();
    }

    private void randomPro(){
        if (problemList == null || problemList.isEmpty()){
            Toast.makeText(this,"we are sorry , there is no algo",Toast.LENGTH_SHORT).show();
            return;
        }
        int i = random.nextInt(problemList.size());

        Intent intent = new Intent(MainActivity.this, ReadProblemActivity.class);
        intent.putExtra("pro",problemList.get(i));


        ActivityTransitionLauncher.with(MainActivity.this)
                .from(floatingActionButton).launch(intent);
//        startActivity(intent);
    }

    private void getAllPro(){
        if (curItem == 0){
            return;
        }

        curItem = 0;
        if (problemList.isEmpty() && copyProList.isEmpty()){
            getAlgo();
            return;
        }

        if (!copyProList.isEmpty()){
            problemList.clear();
            problemList.addAll(copyProList);
            adapter.notifyDataSetChanged();
        }

    }

    private void getAc(){
        if (curItem == 1){
            return;
        }

        curItem = 1;
        copyProList.clear();
        copyProList.addAll(problemList);

        List<Problem> tmp = new ArrayList<>();
        for(Problem p : problemList){
            if (p.isAc()){
                tmp.add(p);
            }
        }

        problemList.clear();
        problemList.addAll(tmp);
        tmp.clear();

        adapter.notifyDataSetChanged();
    }

    private void logout(){
        nameText.setText("login");
        MyPreference.getInstance().setNameAndPwd(this, "", "");
        CookieStore.setCookie(null);
        pullToLoadView.initLoad();
    }

    private void setting(){
        Toast.makeText(this,"there is no setting , just look at the algo",Toast.LENGTH_SHORT).show();
    }

}
