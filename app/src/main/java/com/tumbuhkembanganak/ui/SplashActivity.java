package com.tumbuhkembanganak.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.view.WindowInsetsControllerCompat;

import com.tumbuhkembanganak.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    public static final int second = 5;
    public static final int milisecond = second * 1000;
    public static final int delay =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.setAppearanceLightStatusBars(true);

        binding.progressBar.setMax(maximis_progress());
        setLoading();
    }

    private void setLoading() {
        new CountDownTimer(milisecond,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.progressBar.setProgress(proses_progress(millisUntilFinished));
            }
            @Override
            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }.start();
    }

    private int proses_progress(long miliseconds){
        return (int)((milisecond-miliseconds)/1000);
    }

    private int maximis_progress() {
        return second-delay;
    }
}