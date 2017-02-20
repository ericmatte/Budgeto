package com.endless.activities.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.endless.activities.welcome.SetupActivity;
import com.endless.activities.home.BudgetActivity;
import com.endless.budgeto.R;
import com.endless.tools.DeviceDataSaver;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Splash animations
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.move_up);
        findViewById(R.id.imageView).setAnimation(anim);
        anim = AnimationUtils.loadAnimation(this, R.anim.move_down);
        findViewById(R.id.textView).setAnimation(anim);

        final DeviceDataSaver deviceDataSaver = new DeviceDataSaver(this.getBaseContext());

        // Next screen handler
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (deviceDataSaver.retrievePIN() == -1)
//                    startActivity(new Intent(SplashActivity.this, SetupActivity.class));
//                else
//                    startActivity(new Intent(SplashActivity.this, BudgetActivity.class));
                startActivity(new Intent(SplashActivity.this, SigninActivity.class));
                finish();
            }
        }, 1000);
    }
}
