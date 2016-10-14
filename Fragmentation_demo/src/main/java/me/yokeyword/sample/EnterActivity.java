package me.yokeyword.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import me.yokeyword.sample.demo_flow.MainActivity;

/**
 * Created by YoKeyword on 16/6/5.
 */
public class EnterActivity extends Activity {

    private static final int TOMAIN = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TOMAIN:
                    startActivity(new Intent(EnterActivity.this, MainActivity.class));
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
