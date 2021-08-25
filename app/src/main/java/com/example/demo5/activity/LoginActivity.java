package com.example.demo5.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.demo5.BaseActivity;
import com.example.demo5.R;
import com.example.demo5.bmob.BBManger;
import com.example.demo5.manager.UserManager;
import com.xuexiang.xui.widget.toast.XToast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivClose;
    private ImageView ivSend;
    private EditText etPhone;
    private String phone;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initView();
        initData();
        initEvent();
    }

    @Override
    public void init() {
      intent = new Intent();
    }

    @Override
    public void initView() {
        ivClose = findViewById(R.id.iv_close_login);
        ivSend = findViewById(R.id.iv_send_login);
        etPhone = findViewById(R.id.et_phone_login);
        ivSend.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_login:
                finish();
                break;
            case R.id.iv_send_login:
                sendCode();
                break;

        }
    }

    private void sendCode() {
        phone = etPhone.getText().toString();
        if (phone.length() != 11) {
            XToast.warning(this, "手机号输入错误").show();
            return;
        }
        BBManger.get().sendCode(phone, new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                  intent.setClass(LoginActivity.this, CheckActivity.class);
                    intent.putExtra("phone", phone);
                    startActivityForResult(intent, 1000);

                } else {
                    XToast.warning(LoginActivity.this, "失败").show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            intent.setClass(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
            Log.e("LoginActivity", UserManager.get().getUser().getObjectId());
        }
    }
}
