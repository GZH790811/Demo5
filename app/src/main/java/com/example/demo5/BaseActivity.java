package com.example.demo5;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.StatusBarUtils;

public abstract class BaseActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        XUI.initTheme(this);
        StatusBarUtils.initStatusBarStyle(this,false, ActivityCompat.getColor(this,R.color.white));

    }
    public abstract void init();
    public abstract void initView();
    public abstract void initData();
    public abstract void initEvent();


}
