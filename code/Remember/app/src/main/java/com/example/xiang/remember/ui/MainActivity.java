package com.example.xiang.remember.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiang.remember.model.MyUser;
import com.example.xiang.remember.model.main_list;
import com.example.xiang.remember.R;
import com.example.xiang.remember.adapter.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class MainActivity extends AppCompatActivity {

    //笔记本列表
    private List<main_list> mainList = new ArrayList<>();
    private RecyclerView recyclerView;

    private View mHeaderView;
    private NavigationView mNvMenu;

    private TextView userNmae;
    private CircleImageView headView;

    private String username;
    private CircleImageView userhead;

    private DrawerLayout mDrawerLayout;

    private long curTimeMills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);

        init();

    }

    public void init(){

        //获取当前用户
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);

        mNvMenu = (NavigationView) findViewById(R.id.main_nav_view);
        // 获取HeaderView
        mHeaderView = mNvMenu.getHeaderView(0);

        //用户名，头像修改
        if(userInfo != null){
            username = (String) userInfo.getObjectByKey("username");
            //showToast(username);
            userNmae = (TextView) mHeaderView.findViewById(R.id.navUserName);
            userNmae.setText(username);


            BmobFile now_user_img = userInfo.getHeadPortrait();
            String url = now_user_img.getFileUrl();
            //Bitmap bmp = returnBitMap(url);
            showToast(url);
            headView = (CircleImageView) mHeaderView.findViewById(R.id.navImgUserHead);
            //headView.setImageBitmap(bmp);
        }
        //侧边栏
        //navView.setCheckedItem(R.id.nav_notes);//预先选定
        mNvMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.out_login) {
                    loginout();
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        //点击图标显示菜单
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }


        //创建列表ScaleInAnimationAdapterOvershootInLeftAnimator
        initMainList();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mainlistAdapter adapter = new mainlistAdapter(mainList);
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter);
        animationAdapter.setDuration(1000);
        recyclerView.setAdapter(animationAdapter);
        recyclerView.setItemAnimator(new OvershootInLeftAnimator());

    }

    //退出用户
    public void loginout(){
        BmobUser.logOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_note_search:
                showToast("点击搜索");
                break;
            case android.R.id.home:
//                菜单按钮
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    public void showToast(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    void initMainList() {
        main_list books_notes = new main_list("> > 笔记", "信息时代 记忆爆炸"+"\n"+"在此写下你的各项账号与对应的密码吧");
        mainList.add(books_notes);
        main_list books_todo = new main_list("> > 待办事项", "乱七八糟的思绪"+"\n"+"还有经历的每个瞬间"+"\n"+"尝试用文字承载记忆吧");
        mainList.add(books_todo);
        main_list books_birth_memo = new main_list("> > 生日备忘", "不要再忽视身边人的生日啦"+"\n"+"悄咪咪地准备惊喜吧");
        mainList.add(books_birth_memo);
        main_list books_pwd_management = new main_list("> > 密码管理", "Let your life be filled with B 数"+"\n"+"不再落后或错过 让生活更有规划");
        mainList.add(books_pwd_management);
        main_list books_private_notes = new main_list("> > 隐私笔记", "Let your life be filled with B 数"+"\n"+"不再落后或错过 让生活更有规划");
        mainList.add(books_private_notes);
        main_list books_recycle_bin = new main_list("> > 回收站", "Let your life be filled with B 数"+"\n"+"不再落后或错过 让生活更有规划");
        mainList.add(books_recycle_bin);
    }

    //加载图片
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // 关闭程序
            exitAPP();
        }
    }

    /**
     * 两秒内单击两下即可关闭APP
     */
    private void exitAPP() {

        if (System.currentTimeMillis() - curTimeMills > 2000) {
            Snackbar.make(mDrawerLayout, "再按一次退出程序", Snackbar.LENGTH_SHORT).show();
            curTimeMills = System.currentTimeMillis();
        } else {
            finish();
        }

    }
}
