package z.android.communication.datasharingandcommunication.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import z.android.communication.datasharingandcommunication.R;
import z.android.communication.datasharingandcommunication.interfaces.Constants;
import z.android.communication.datasharingandcommunication.utils.SharedPreferenceUtil;

public class WelcomeActivity extends AppCompatActivity {
    private SharedPreferenceUtil sharedPreferenceUtil;
    private int go = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        sharedPreferenceUtil = new SharedPreferenceUtil(this, Constants.SAVE_USER);
        if (sharedPreferenceUtil.getIsFirst()){
            sharedPreferenceUtil.setIsFirst(false);
            goLoginActivity();
            go++;
        }
        initView();
    }

    public void initView(){
        if (sharedPreferenceUtil.getIsRunning()){
            goHomePageActivity();
        }else if (go==0){
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goLoginActivity();
                }
            },1000);
        }
    }

    public void goLoginActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },1000);
    }

    public void goHomePageActivity(){
        sharedPreferenceUtil.setIsRunning(false);
        Intent intent = new Intent();
        intent.setClass(this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }
}
