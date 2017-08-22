package com.hunfrit.geolocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;



/**
 * Created by Artem Shapovalov on 15.08.2017.
 */

public class SplashScreen extends Activity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    java.util.concurrent.TimeUnit.MILLISECONDS.sleep(3000);
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
