package com.example.xiang.remember.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiang.remember.R;
import com.example.xiang.remember.model.MyUser;
import com.example.xiang.remember.model.main_list;
import com.example.xiang.remember.ui.*;

import java.util.List;

import cn.bmob.v3.BmobUser;


/**
 * Created by XIANG! on 2018/4/1.
 */

public class mainlistAdapter extends RecyclerView.Adapter<mainlistAdapter.ViewHolder>{

    private List<main_list> mMemoList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View memoView;
        TextView memoName;
        TextView memoIntro;

        public ViewHolder(View view) {
            super(view);
            memoView = view;
            memoName = (TextView) view.findViewById(R.id.memo_name);
            memoIntro = (TextView) view.findViewById(R.id.memo_intro);
        }
    }

    public mainlistAdapter(List<main_list> memoList) {
        mMemoList = memoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.memoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                main_list memo = mMemoList.get(position);

                if (memo.getMemo_name().equals("> > 笔记")) {
                    Toast.makeText(v.getContext(),"aaa",Toast.LENGTH_SHORT).show();
                } else if (memo.getMemo_name().equals("> > 待办事项")) {
                    Toast.makeText(v.getContext(),"aaaa",Toast.LENGTH_SHORT).show();
                } else if (memo.getMemo_name().equals("> > 生日备忘")) {
                    Toast.makeText(v.getContext(),"aaaa",Toast.LENGTH_SHORT).show();
                } else if (memo.getMemo_name().equals("> > 密码管理")) {
                    Toast.makeText(v.getContext(),"aaaa",Toast.LENGTH_SHORT).show();
                }else if (memo.getMemo_name().equals("> > 隐私笔记")) {
                    Toast.makeText(v.getContext(),"aaaa",Toast.LENGTH_SHORT).show();

                }else if (memo.getMemo_name().equals("> > 回收站")) {
                    Toast.makeText(v.getContext(),"aaaa",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        main_list memo = mMemoList.get(position);
        holder.memoName.setText(memo.getMemo_name());
        holder.memoIntro.setText(memo.getMemo_intro());
    }

    @Override
    public int getItemCount() {
        return mMemoList.size();
    }


}