package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    AppCompatButton sign_in, sign_up;

    private ImageView waveHeaderA, waveHeaderB;
    private ImageView waveLayer2A, waveLayer2B;
    private ImageView waveLayer3A, waveLayer3B;
    private ImageView bubble1, bubble2, bubble3, bubble4;

    private Animations animations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        animations = Animations.getInstance();
        initializeViews();
        setupClickListeners();
        setupAnimations();
        checkLoginStatus();
    }

    private void initializeViews() {
        waveHeaderA = findViewById(R.id.waveHeaderA);
        waveHeaderB = findViewById(R.id.waveHeaderB);
        waveLayer2A = findViewById(R.id.waveLayer2A);
        waveLayer2B = findViewById(R.id.waveLayer2B);
        waveLayer3A = findViewById(R.id.waveLayer3A);
        waveLayer3B = findViewById(R.id.waveLayer3B);

        bubble1 = findViewById(R.id.bubble1);
        bubble2 = findViewById(R.id.bubble2);
        bubble3 = findViewById(R.id.bubble3);
        bubble4 = findViewById(R.id.bubble4);

        sign_in = findViewById(R.id.sign_in);
        sign_up = findViewById(R.id.sign_up);
    }

    private void setupClickListeners() {
        sign_in.setOnClickListener(v -> {
            // Simple scale animation with Handler for navigation
            v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(80).start();

            new Handler().postDelayed(() -> {
                v.animate().scaleX(1f).scaleY(1f).setDuration(80).start();

                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }, 80);
            }, 80);
        });

        sign_up.setOnClickListener(v -> {
            // Simple scale animation with Handler for navigation
            v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(80).start();

            new Handler().postDelayed(() -> {
                v.animate().scaleX(1f).scaleY(1f).setDuration(80).start();

                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }, 80);
            }, 80);
        });
    }

    private void setupAnimations() {
        View root = findViewById(android.R.id.content);

        animations.waitForLayout(root, () -> {
            animations.startWaveAnimations(waveHeaderA, waveHeaderB,
                    waveLayer2A, waveLayer2B, waveLayer3A, waveLayer3B);

            animations.startBubbleAnimations(root, bubble1, bubble2, bubble3, bubble4);
        });
    }

    private void checkLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn", false);
        if (hasLoggedIn) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animations != null) {
            animations.pauseAnimations();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animations != null && waveHeaderA != null && waveHeaderB != null) {
            animations.resumeWaveAnimations(waveHeaderA, waveHeaderB,
                    waveLayer2A, waveLayer2B, waveLayer3A, waveLayer3B);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animations != null) {
            animations.cleanupAnimations();
        }
    }
}