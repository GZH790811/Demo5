package com.example.demo5.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.demo5.BaseActivity;
import com.example.demo5.R;
import com.example.demo5.bmob.BBManger;
import com.example.demo5.bmob.MyUser;
import com.example.demo5.manager.UserManager;
import com.xuexiang.xui.widget.toast.XToast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class StatActivity extends BaseActivity {
    private Intent intent;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1000) {
                intent.setClass(StatActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            } else {
                intent.setClass(StatActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
    private String id;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srat);
        intent = new Intent();
        SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
        id = user.getString("phone", "");
        if (!TextUtils.isEmpty(id)) {
            findUser();

        } else {
            handler.sendEmptyMessageDelayed(1001, 3000);
        }

    }

    private void findUser() {
        BBManger.get().findUser(id, new QueryListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException e) {
                if (e == null) {
                    UserManager.get().saveUser(user);
                    handler.sendEmptyMessageDelayed(1000, 2000);


                } else {
                    XToast.warning(StatActivity.this, "失败" + e.getErrorCode()).show();

                }
            }
        });
    }

    @Override
    public void init() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
