package com.atguigu.exam_afterfirstproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class WelcomeActivity extends Activity {

    private static final int TOMAIN = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TOMAIN:
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    removeMessages(TOMAIN);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initView();
    }

    private void initView() {
        handler.sendEmptyMessageDelayed(TOMAIN, 2000);
    }
}
