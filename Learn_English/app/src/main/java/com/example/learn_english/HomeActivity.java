package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class HomeActivity extends AppCompatActivity {
    Button button;
    MaterialCardView tenses, prepositions, words;

    private Animations animations;
    private ImageView waveHeaderA, waveHeaderB;
    private ImageView waveLayer2A, waveLayer2B;
    private ImageView waveLayer3A, waveLayer3B;
    private ImageView bubble1, bubble2, bubble3, bubble4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize animations
        animations = new Animations();
        initializeViews();
        setupAnimations();
        setupClickListeners();
    }

    private void initializeViews() {
        // Wave views
        waveHeaderA = findViewById(R.id.waveHeaderA);
        waveHeaderB = findViewById(R.id.waveHeaderB);
        waveLayer2A = findViewById(R.id.waveLayer2A);
        waveLayer2B = findViewById(R.id.waveLayer2B);
        waveLayer3A = findViewById(R.id.waveLayer3A);
        waveLayer3B = findViewById(R.id.waveLayer3B);

        // Bubble views
        bubble1 = findViewById(R.id.bubble1);
        bubble2 = findViewById(R.id.bubble2);
        bubble3 = findViewById(R.id.bubble3);
        bubble4 = findViewById(R.id.bubble4);

        // Button views
        tenses = findViewById(R.id.tenses);
        prepositions = findViewById(R.id.prepositions);
        words = findViewById(R.id.words);
        button = findViewById(R.id.button);
    }

    private void setupAnimations() {
        View root = findViewById(android.R.id.content);

        animations.waitForLayout(root, () -> {
            // Start wave animations if wave views exist
            if (waveHeaderA != null && waveHeaderB != null) {
                animations.startWaveAnimations(waveHeaderA, waveHeaderB,
                        waveLayer2A, waveLayer2B, waveLayer3A, waveLayer3B);
            }

            // Start bubble animations if bubble views exist
            if (bubble1 != null && bubble2 != null && bubble3 != null && bubble4 != null) {
                animations.startBubbleAnimations(root, bubble1, bubble2, bubble3, bubble4);
            }

            // Apply entrance animations to buttons
            animateContentEntrance();
        });
    }

    private void animateContentEntrance() {
        // Staggered animation for buttons
        new Handler().postDelayed(() -> {
            if (tenses != null) animations.fadeInView(tenses, 400);
        }, 200);

        new Handler().postDelayed(() -> {
            if (words != null) animations.fadeInView(words, 400);
        }, 400);

        new Handler().postDelayed(() -> {
            if (prepositions != null) animations.fadeInView(prepositions, 400);
        }, 600);

        new Handler().postDelayed(() -> {
            if (button != null) animations.fadeInView(button, 400);
        }, 800);
    }

    private void setupClickListeners() {
        // Apply button click animations
        animations.applyButtonClickAnimation(tenses);
        animations.applyButtonClickAnimation(words);
        animations.applyButtonClickAnimation(prepositions);
        animations.applyButtonClickAnimation(button);

        tenses.setOnClickListener(v -> {
            // Add navigation animation
            v.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        v.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .withEndAction(() -> {
                                    Intent intent = new Intent(HomeActivity.this, TensesActivity.class);
                                    startActivity(intent);
                                })
                                .start();
                    })
                    .start();
        });

        prepositions.setOnClickListener(v -> {
            v.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        v.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .withEndAction(() -> {
                                    Intent intent = new Intent(HomeActivity.this, PrepositionsActivity.class);
                                    startActivity(intent);
                                })
                                .start();
                    })
                    .start();
        });

        words.setOnClickListener(v -> {
            v.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        v.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .withEndAction(() -> {
                                    Intent intent = new Intent(HomeActivity.this, WordsActivity.class);
                                    startActivity(intent);
                                })
                                .start();
                    })
                    .start();
        });

        /* Log out */
        button.setOnClickListener(v -> {
            v.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        v.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .withEndAction(() -> performLogout())
                                .start();
                    })
                    .start();
        });
    }

    private void performLogout() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("hasLoggedIn", false);
        editor.apply(); // Use apply() instead of commit() for better performance

        // Fade out animation before navigation
        View root = findViewById(android.R.id.content);
        if (root != null) {
            root.animate()
                    .alpha(0f)
                    .setDuration(500)
                    .withEndAction(() -> {
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .start();
        } else {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
            animations.startWaveAnimations(waveHeaderA, waveHeaderB,
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