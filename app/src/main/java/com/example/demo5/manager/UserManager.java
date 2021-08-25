package com.example.demo5.manager;

import com.example.demo5.bmob.MyUser;

public class UserManager {
    private static UserManager mUserManager;

    private UserManager() {
    }

    public static UserManager get() {
        if (mUserManager == null) {
            mUserManager = new UserManager();
        }
        return mUserManager;
    }

    private MyUser myUser;

    public void saveUser(MyUser user) {
        this.myUser = user;
    }

    public MyUser getUser() {
        return myUser;
    }

    public void removeUser() {
        myUser = null;
    }
}
