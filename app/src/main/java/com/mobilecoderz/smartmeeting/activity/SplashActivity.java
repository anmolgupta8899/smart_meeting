package com.mobilecoderz.smartmeeting.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mobilecoderz.smartmeeting.utils.AppConstant;
import com.mobilecoderz.smartmeeting.utils.CommonUtils;
import com.mobilecoderz.smartmeeting.R;

public class SplashActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context=SplashActivity.this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, OnBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
               /* if(CommonUtils.getPreferences(context, AppConstant.USER_ID)!=null&&!CommonUtils.getPreferences(context,AppConstant.USER_ID).equalsIgnoreCase("")){
                    if(CommonUtils.getPreferences(context,AppConstant.FROM).equalsIgnoreCase("saveProfile")) {
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(context, ProfileDetailsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }*/
            }
        }, 2000);
    }
}
