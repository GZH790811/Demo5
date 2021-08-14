package com.example.demo5.app;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.xuexiang.xui.XUI;

import cn.bmob.v3.Bmob;

public class App extends Application {
    private String APP_KEY= "9271b5d92308ca39d21070bc3e41259e";
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化Bmob
        Bmob.initialize(this,APP_KEY);

        //初始化XUI
        XUI.init(this);
        XUI.debug(true);

        //初始化百度sdk
        SDKInitializer.initialize(this);
        //设置坐标类型
        SDKInitializer.setCoordType(CoordType.BD09LL);



    }
}
