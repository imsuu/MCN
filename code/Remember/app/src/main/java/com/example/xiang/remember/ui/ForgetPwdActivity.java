package com.example.xiang.remember.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xiang.remember.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetPwdActivity extends AppCompatActivity {
    private EditText userEmail;
    private Button verification_code;
    private String userEmailText;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // findView
        userEmail = (EditText) findViewById(R.id.userEmail);
        verification_code = (Button) findViewById(R.id.verification_code);
        back = (ImageView) findViewById(R.id.back);

        //获取用户邮箱
        get_intent();
        // 点击事件
        clickEvents();
    }

    public void clickEvents() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        verification_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmailText = userEmail.getText().toString();
                final String email = userEmailText;
                if (email.equals("")) {
                    Toast.makeText(getApplication(), "请输入您的邮箱喔~", Toast.LENGTH_SHORT).show();
                } else {
                    BmobUser.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(getApplication(), "重置密码请求成功，请到" + email + "邮箱进行密码重置操作", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplication(), "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void get_intent(){
        Intent it = getIntent();
        String data = it.getStringExtra("user_email");
        userEmail.setText(data);
    }

    //Toast
    public void showToast(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

}
