package com.example.xiang.remember.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiang.remember.R;
import com.example.xiang.remember.model.MyUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText useremail;
    private EditText userpassword;
    private String useremailText;
    private String userpasswordText;
    private TextView signUp;
    private Button signIn;
    private TextView forgetPsw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();

    }

    public void init(){
        // findView
        useremail = (EditText) findViewById(R.id.userEmail);
        userpassword = (EditText) findViewById(R.id.userPassword);
        signUp = (TextView) findViewById(R.id.goSignUp);
        forgetPsw = (TextView) findViewById(R.id.forgetPsw);
        signIn = (Button) findViewById(R.id.signIn);

        // 点击事件
        signUp.setOnClickListener(this);
        signIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.goSignUp:
                Intent it = new Intent(LoginActivity.this, RegisterActivity.class);
                //返回数据
                startActivityForResult(it,1);
                break;
            case R.id.signIn:
                go_signin();
                break;
        }
    }

    public void go_signin(){
        useremailText=useremail.getText().toString();
        userpasswordText=userpassword.getText().toString();
        BmobUser bu2 = new BmobUser();
        bu2.setEmail(useremailText);
        bu2.setPassword(userpasswordText);

        bu2.loginByAccount(useremailText, userpasswordText, new LogInListener<MyUser>() {

            @Override
            public void done(MyUser user, BmobException e) {
                if(user!=null){
                    Log.d("LoginActivity","success");
                    showToast("登陆成功");
                    //finish();
                    Intent it = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(it);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }
                else
                    showToast(e.toString());
            }
        });

    }

    //Toast
    public void showToast(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 判断请求码和返回码是不是正确的
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String name = data.getStringExtra("email");
            String pwd = data.getStringExtra("pwd");
            // 把得到的数据显示到输入框内
            useremail.setText(name);
            userpassword.setText(pwd);
        }
    }
}
