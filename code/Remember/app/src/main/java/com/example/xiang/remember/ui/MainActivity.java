package com.example.xiang.remember.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiang.remember.model.MyUser;
import com.example.xiang.remember.model.main_list;
import com.example.xiang.remember.R;
import com.example.xiang.remember.adapter.*;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
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

    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
//      bmob注册
        Bmob.initialize(this, "bb54bce7bd52e042e7de4ce0df7d1ecb");


        init();

    }

    public void init(){

        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        if(userInfo != null){
            username = (String) userInfo.getObjectByKey("username");
            showToast(username);
            //userNmae.setText(username);
            //headView.setImageURI(userInfo.getObjectByKey("headPortrait").toUri());
        }

        mNvMenu = (NavigationView) findViewById(R.id.main_nav_view);
        // 获取HeaderView
        mHeaderView = mNvMenu.getHeaderView(0);
        userNmae = (TextView) mHeaderView.findViewById(R.id.navUserName);
        userNmae.setText(username);

        //点击图标显示菜单
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //侧边栏 点击关闭
        NavigationView navView = (NavigationView) findViewById(R.id.main_nav_view);
        //navView.setCheckedItem(R.id.nav_notes);//预先选定
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });


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
}
