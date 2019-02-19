package com.example.todolists;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    Thread splashThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = (ImageView) findViewById(R.id.todo);
        TextView textView = (TextView) findViewById(R.id.text);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.todoanim);
        imageView.startAnimation(animation);

        splashThread = new Thread(){
            @Override
            public void run() {
                try{
                    int waited = 0;
                    while(waited<1500){
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    SplashActivity.this.finish();
                }
            }
        };
        splashThread.start();
    }
}
