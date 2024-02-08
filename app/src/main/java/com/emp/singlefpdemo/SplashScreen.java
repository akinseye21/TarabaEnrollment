package com.emp.singlefpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {

    protected static final int Timer_Runtime=3000;
    protected boolean mbActive;
    protected ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mProgressBar = findViewById(R.id.progressBar);
        final Thread timerThread = new Thread(){
            @Override
            public void run(){
                mbActive = true;
                try{
                    int waited = 0;
                    while(mbActive && (waited < Timer_Runtime)){
                        sleep(200);
                        if (mbActive){
                            waited += 200;
                            updateProgress(waited);
                        }
                    }
                }catch(InterruptedException e){
                    //error
                }finally{
                    onContinue();
                }
            }
        };
        timerThread.start();
    }

    public void updateProgress(final int timePassed){
        if (null != mProgressBar){

            final int progress = mProgressBar.getMax() * timePassed / Timer_Runtime;
            mProgressBar.setProgress(progress);
        }
    }

    public void onContinue(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}