package com.example.demo5.java;

import com.example.demo5.bmob.BBManger;
import com.example.demo5.bmob.MyUser;
import com.example.demo5.manager.UserManager;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class Login {
    private LoginCallBack mLoginCallBack;
    private String phone;

    public void checkPhoneCode(String phone, String code, LoginCallBack callBack) {
        this.mLoginCallBack = callBack;
        this.phone = phone;
        BBManger.get().checkPhoneCode(phone, code, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    findUserByPhone();
                } else {
                    mLoginCallBack.error(e.getMessage(), e.getErrorCode());

                }
            }
        });
    }

    private void findUserByPhone() {
        BmobQuery<MyUser> query = new BmobQuery<>();
        query.addWhereEqualTo("phone", phone);
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        MyUser myUser = object.get(0);
                        UserManager.get().saveUser(myUser);
                        mLoginCallBack.done(UserManager.get().getUser().getObjectId());

                    } else {
                        createUser();

                    }
                } else {
                    mLoginCallBack.error(e.getMessage(), e.getErrorCode());

                }
            }
        });

    }

    private void createUser() {
        MyUser user = new MyUser.Builder()
                .setName("鳄鱼搓背")
                .setPhone(phone)
                .setPhotoUrl("null")
                .setPayPwd("0000")
                .setMoney(0.0)
                .setUpMoney(0.0)
                .build();
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e ==null) {
                    UserManager.get().saveUser(user);
                    mLoginCallBack.done(UserManager.get().getUser().getObjectId());
                }else{
                    mLoginCallBack.error(e.getMessage(),e.getErrorCode());
                }
            }
        });
    }

    public interface LoginCallBack {
        void done(String id);

        void error(String msg, int errorCode);
    }
}
