package com.example.xiang.remember.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.xiang.remember.R;
import com.example.xiang.remember.model.MyUser;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class WelcomeActivity extends AppCompatActivity {
    private Intent it;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);

        //      bmob注册
        Bmob.initialize(this, "bb54bce7bd52e042e7de4ce0df7d1ecb");

        imageView = (ImageView) findViewById(R.id.iv_welcome);

        // 实现渐变效果
        Animation animation = new AlphaAnimation(0.5f, 1f);
        animation.setDuration(3000);
        imageView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 判断是否存在当前用户，如果存在，则直接进入主界面，不用再次登录
                MyUser current_user = BmobUser.getCurrentUser(MyUser.class);
                if (current_user != null) {
                    it = new Intent(WelcomeActivity.this, MainActivity.class);
                } else {
                    // 不存在当前用户，转向登录界面
                    it = new Intent(WelcomeActivity.this, LoginActivity.class);
                }
                startActivity(it);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
