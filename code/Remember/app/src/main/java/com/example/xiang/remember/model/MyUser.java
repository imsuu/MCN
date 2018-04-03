package com.example.xiang.remember.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2018/3/22.
 */

public class MyUser extends BmobUser {
    //头像
    private BmobFile headPortrait;

    public BmobFile getHeadPortrait() {
        return headPortrait;
    }

    public void setheadPortrait(BmobFile headPortrait) {
        this.headPortrait = headPortrait;
    }
}