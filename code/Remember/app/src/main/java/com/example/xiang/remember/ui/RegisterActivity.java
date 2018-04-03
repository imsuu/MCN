package com.example.xiang.remember.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xiang.remember.R;
import com.example.xiang.remember.model.MyUser;
import com.nanchen.compresshelper.CompressHelper;
import com.nanchen.compresshelper.FileUtil;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private CircleImageView headView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private File oldFile;
    private File newFile;

    private EditText userName;
    private EditText userEmail;
    private EditText userPassword;
    private EditText pwdAgain;

    private String userNameText;
    private String userEmailText;
    private String userPasswordText;
    private String pwdAgainText;
    private Button signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

// findView
        headView = (CircleImageView) findViewById(R.id.imgUserHead);
        userName = (EditText) findViewById(R.id.userName);
        userEmail = (EditText) findViewById(R.id.userEmail);
        userPassword = (EditText) findViewById(R.id.userPassword);
        pwdAgain = (EditText) findViewById(R.id.pwdAgain);
        signUp = (Button) findViewById(R.id.signUp);

        signUp.setOnClickListener(this);
        headView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signUp:
                userNameText = userName.getText().toString();
                userEmailText = userEmail.getText().toString();
                userPasswordText = userPassword.getText().toString();
                pwdAgainText = pwdAgain.getText().toString();
                if (userNameText.equals("") || userEmailText.equals("") || userPasswordText.equals("") || pwdAgainText.equals(""))
                    showToast("请填写完整");
                else if (!userPasswordText.equals(pwdAgainText))
                    showToast("请注意：您的两次密码填写不一致");
                else
                    go_signup();
                break;
            case R.id.imgUserHead:
                showToast("asadasda");
                takePhoto();
                break;
        }
    }


    public void takePhoto(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                showToast("Failed to open picture!");
                return;
            }
            try {
                oldFile = FileUtil.getTempFile(this, data.getData());
                clearImage();
                compress();
            } catch (IOException e) {
                showToast("Failed to read picture data!");
                e.printStackTrace();
            }
        }
    }

    public void compress() {
        // 默认的压缩方法，多张图片只需要直接加入循环即可
        newFile = CompressHelper.getDefault(getApplicationContext()).compressToFile(oldFile);
        Uri uri= Uri.parse(newFile.getAbsolutePath());
        headView.setImageURI(uri);
        // headView.setImageBitmap(BitmapFactory.decodeFile(newFile.getAbsolutePath()));
        showToast(String.format("Size : %s", getReadableFileSize(newFile.length())));
    }

    private int getRandomColor() {
        Random rand = new Random();
        return Color.argb(100, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    //Toast
    public void showToast(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    //清除
    private void clearImage() {
        headView.setImageDrawable(null);
        headView.setBackgroundColor(getRandomColor());
    }

    //获得
    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    //注册
    public void go_signup() {
        final BmobFile bmobFile = new BmobFile(new File(newFile.getAbsolutePath()));

        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    showToast("上传文件成功:" );
                    MyUser bu = new MyUser();
                    //数据插入
                    bu.setheadPortrait(bmobFile);
                    bu.setUsername(userNameText);
                    bu.setPassword(userPasswordText);
                    bu.setEmail(userEmailText);
                    //注意：不能用save方法进行注册
                    bu.signUp(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser s, BmobException e) {
                            if (e == null) {
                                showToast("注册成功:" + s.toString());
                                Intent intent = new Intent();
                                intent.putExtra("email", userEmailText);
                                intent.putExtra("pwd", userPasswordText);
                                setResult(RESULT_OK, intent);
                                finish();
                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                            } else {
                                showToast("注册失败:" + e);
                            }
                        }
                    });
                }else{
                    showToast("上传文件失败：" + e.getMessage());
                }

            }
            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });

    }
}
