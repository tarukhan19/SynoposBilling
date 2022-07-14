package com.proj.synoposbilling.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import com.proj.synoposbilling.R;
import com.proj.synoposbilling.Utils.MyApplication;
import com.proj.synoposbilling.databinding.ActivityMainBinding;
import com.proj.synoposbilling.session.SessionManager;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
    SessionManager session;
    private static int SPLASH_TIME_OUT =3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initialize();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                     Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    finish();

            }
        }, SPLASH_TIME_OUT);





    }

    private void initialize()
    {
        session = new SessionManager(getApplicationContext());
        session.setAddressData("","","");

        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
    }



}