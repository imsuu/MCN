package com.example.xiang.remember.model;

/**
 * Created by XIANG! on 2018/4/1.
 */

public class main_list {

        private String memo_name;
        private String memo_intro;

        public main_list(String memo_name, String memo_intro) {
            this.memo_name = memo_name;
            this.memo_intro = memo_intro;
        }

        public String getMemo_name() {
            return memo_name;
        }

        public String getMemo_intro() {
            return memo_intro;
        }

}
