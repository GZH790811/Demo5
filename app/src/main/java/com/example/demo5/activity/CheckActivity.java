package com.example.demo5.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.demo5.BaseActivity;
import com.example.demo5.R;
import com.example.demo5.bmob.BBManger;
import com.example.demo5.java.Login;
import com.google.android.material.button.MaterialButton;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.verify.VerifyCodeEditText;
import com.xuexiang.xui.widget.toast.XToast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class CheckActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivClose;
    private TextView tvPhone;
    private ImageView ivUp;
    private VerifyCodeEditText etCode;
    private MaterialButton btnSend;
    private String phone;
    private int count = 60;
    private MiniLoadingDialog dialog;

    private boolean isSend =true;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (isSend){
                if (msg.what == 1) {
                    btnSend.setEnabled(true);
                    btnSend.setText("重新发送");
                    btnSend.setBackgroundColor(ActivityCompat.getColor(CheckActivity.this, R.color.main_blue));
                    isSend = false;
                    count = 60;
                } else {
                    count--;
                    btnSend.setText(count + "s");
                    handler.sendEmptyMessageDelayed(count, 1000);
                }
            }
            if (msg.what == 1001) {
                dialog.dismiss();
//                Intent intent = new Intent(CheckActivity.this,HomeActivity.class);
                setResult(RESULT_OK);
                finish();
            }

        }
    };

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        init();
        initView();
        initData();
        initEvent();
    }

    @Override
    public void init() {
        dialog = WidgetUtils.getMiniLoadingDialog(this);
        dialog.setDialogSize(200, 200);

    }

    @Override
    public void initView() {
        ivClose = findViewById(R.id.iv_close_check);
        tvPhone = findViewById(R.id.tv_phone_check);
        ivUp = findViewById(R.id.iv_up_check);
        etCode = findViewById(R.id.et_code_check);
        btnSend = findViewById(R.id.btn_send_check);

        ivClose.setOnClickListener(this);
        ivUp.setOnClickListener(this);
        btnSend.setOnClickListener(this);

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        tvPhone.setText(phone);  //显示传过来的电话

    }

    @Override
    public void initEvent() {
        etCode.setOnInputListener(new VerifyCodeEditText.OnInputListener() {
            @Override
            public void onComplete(String input) {
                checkPhoneCode(input);

            }

            @Override
            public void onChange(String input) {

            }

            @Override
            public void onClear() {

            }
        });

    }

    private void checkPhoneCode(String code) {
        dialog.show();
        Login login = new Login();
        login.checkPhoneCode(phone, code, new Login.LoginCallBack() {
            @Override
            public void done(String id) {
                putUserId(id);
            }
            @Override
            public void error(String msg, int errorCode) {
                dialog.dismiss();
                XToast.warning(CheckActivity.this, msg + errorCode);
                Log.e("CheckActivity", " 错误信息 " + msg + " 错误码 " + errorCode);
            }
        });
    }

    private void putUserId(String id) {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("id", id);
        edit.commit();
        handler.sendEmptyMessageDelayed(1001, 500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_check:
                finish();
                break;
            case R.id.iv_up_check:
                finish();
                break;
            case R.id.btn_send_check:
                sendCode();
                break;
        }
    }

    private void sendCode() {
        isSend = true;
        BBManger.get().sendCode(phone, new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    XToast.success(CheckActivity.this, "发送成功").show();

                } else {
                    XToast.success(CheckActivity.this, "发送失败" + e.getErrorCode()).show();

                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessage(count);
    }
}
