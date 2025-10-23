package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizResults extends AppCompatActivity {

    TextView correct;
    TextView incorrect;
    ProgressBar progressBar;
    private String getSelectedTopicName;
    private TextView textView;

    // Animation variables
    private Animations animations;
    private ImageView waveHeaderA, waveHeaderB;
    private ImageView waveLayer2A, waveLayer2B;
    private ImageView bubble1, bubble2, bubble3;
    private MaterialButton shareBtn, homeBtn;
    private View resultsContentCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);

        initializeViews();
        setupAnimations();
        setupResults();
    }

    private void initializeViews() {
        correct = findViewById(R.id.correct);
        incorrect = findViewById(R.id.incorrect);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.txt);
        homeBtn = findViewById(R.id.homeBtn);
        resultsContentCard = findViewById(R.id.results_content_card);

        // Initialize animation views
        waveHeaderA = findViewById(R.id.waveHeaderA);
        waveHeaderB = findViewById(R.id.waveHeaderB);
        waveLayer2A = findViewById(R.id.waveLayer2A);
        waveLayer2B = findViewById(R.id.waveLayer2B);
        bubble1 = findViewById(R.id.bubble1);
        bubble2 = findViewById(R.id.bubble2);
        bubble3 = findViewById(R.id.bubble3);

        // Set initial states for animations
        setInitialAnimationStates();
    }

    private void setInitialAnimationStates() {
        if (resultsContentCard != null) {
            resultsContentCard.setAlpha(0f);
            resultsContentCard.setScaleX(0.9f);
            resultsContentCard.setScaleY(0.9f);
            resultsContentCard.setTranslationY(50f);
        }

        if (correct != null) correct.setAlpha(0f);
        if (incorrect != null) incorrect.setAlpha(0f);
        if (progressBar != null) progressBar.setAlpha(0f);
        if (textView != null) textView.setAlpha(0f);
        if (shareBtn != null) shareBtn.setAlpha(0f);
        if (homeBtn != null) homeBtn.setAlpha(0f);
    }

    private void setupAnimations() {
        animations = new Animations();
        View root = findViewById(android.R.id.content);

        animations.waitForLayout(root, () -> {
            if (waveHeaderA != null && waveHeaderB != null) {
                animations.startWaveAnimations(waveHeaderA, waveHeaderB,
                        waveLayer2A, waveLayer2B, null, null);
            }

            if (bubble1 != null && bubble2 != null && bubble3 != null) {
                animations.startBubbleAnimations(root, bubble1, bubble2, bubble3);
            }

            // Start the results card animation after waves are set up
            startResultsCardAnimation();
        });
    }

    private void startResultsCardAnimation() {
        if (resultsContentCard != null) {
            resultsContentCard.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .translationY(0f)
                    .setDuration(600)
                    .setInterpolator(new OvershootInterpolator(1.0f))
                    .withStartAction(() -> {
                        // Start staggered animations for content inside the card
                        startStaggeredContentAnimation();
                    })
                    .start();
        } else {
            // Fallback if card is null
            startStaggeredContentAnimation();
        }
    }

    private void startStaggeredContentAnimation() {
        new Handler().postDelayed(() -> {
            if (textView != null) {
                textView.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(400)
                        .setInterpolator(new DecelerateInterpolator())
                        .start();
            }
        }, 200);

        new Handler().postDelayed(() -> {
            if (progressBar != null) {
                progressBar.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(400)
                        .setInterpolator(new DecelerateInterpolator())
                        .start();
            }
        }, 400);

        new Handler().postDelayed(() -> {
            animateScoreCounter(correct, getIntent().getIntExtra("correct", 0));
            animateScoreCounter(incorrect, getIntent().getIntExtra("incorrect", 0));
        }, 600);

        new Handler().postDelayed(() -> {
            if (shareBtn != null) {
                shareBtn.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(300)
                        .setInterpolator(new OvershootInterpolator())
                        .start();
            }

            if (homeBtn != null) {
                homeBtn.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(300)
                        .setInterpolator(new OvershootInterpolator())
                        .withEndAction(this::setupButtonInteractions)
                        .start();
            }
        }, 800);
    }

    private void animateScoreCounter(TextView textView, int targetValue) {
        if (textView == null) return;

        final int duration = 1500;
        final int frameDuration = 30;
        final int totalFrames = duration / frameDuration;
        final double increment = (double) targetValue / totalFrames;

        new Handler().post(new Runnable() {
            private int frame = 0;
            private double currentValue = 0;

            @Override
            public void run() {
                if (frame < totalFrames) {
                    currentValue += increment;
                    textView.setText(String.valueOf((int) currentValue));
                    textView.setAlpha((float) frame / totalFrames);
                    frame++;
                    textView.postDelayed(this, frameDuration);
                } else {
                    textView.setText(String.valueOf(targetValue));
                    textView.setAlpha(1f);

                    // Add a subtle scale animation when counter completes
                    textView.animate()
                            .scaleX(1.1f)
                            .scaleY(1.1f)
                            .setDuration(150)
                            .withEndAction(() -> textView.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(150)
                                    .start())
                            .start();
                }
            }
        });
    }

    private void setupButtonInteractions() {
        if (shareBtn != null) {
            shareBtn.setOnClickListener(v -> animateButtonClick(shareBtn, this::shareResults));
        }

        if (homeBtn != null) {
            homeBtn.setOnClickListener(v -> animateButtonClick(homeBtn, this::goHome));
        }
    }

    private void animateButtonClick(MaterialButton button, Runnable action) {
        button.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(() -> button.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .setInterpolator(new OvershootInterpolator())
                        .withEndAction(action)
                        .start())
                .start();
    }

    private void setupResults() {
        getSelectedTopicName = getIntent().getStringExtra("selectedTopicName");
        if (getSelectedTopicName != null) {
            getSelectedTopicName = getSelectedTopicName.replace(" ", "");
        }

        int correctAnswers = getIntent().getIntExtra("correct", 0);
        int incorrectAnswers = getIntent().getIntExtra("incorrect", 0);
        int totalQuestions = getIntent().getIntExtra("size", 0);

        // Set initial text (counters will animate later)
        if (correctAnswers <= 5) {
            textView.setText("You can do better!");
        } else {
            textView.setText("You've completed quiz successfully");
        }

        // Calculate and set progress with animation
        int progressPercentage = totalQuestions > 0 ? (int) ((correctAnswers / (float) totalQuestions) * 100) : 0;

        new Handler().postDelayed(() -> {
            progressBar.setProgress(progressPercentage);
            saveProgressToFirebase(progressPercentage);
        }, 1000);
    }

    private void saveProgressToFirebase(int progressPercentage) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userId == null) return;

        DatabaseReference tensesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("tenses");
        HashMap<String, Object> tensesMap = new HashMap<>();
        tensesMap.put(getSelectedTopicName, progressPercentage);
        tensesRef.updateChildren(tensesMap);

        DatabaseReference prepRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("prepositions");
        HashMap<String, Object> prepMap = new HashMap<>();
        prepMap.put(getSelectedTopicName, progressPercentage);
        prepRef.updateChildren(prepMap);

        DatabaseReference wordRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("words");
        HashMap<String, Object> wordMap = new HashMap<>();
        wordMap.put(getSelectedTopicName, progressPercentage);
        wordRef.updateChildren(wordMap);
    }

    private void shareResults() {
        // Implement share functionality
        // Example: Share the quiz results
    }

    private void goHome() {
        // Animate exit before going home
        View root = findViewById(android.R.id.content);
        if (root != null) {
            root.animate()
                    .alpha(0f)
                    .setDuration(400)
                    .withEndAction(() -> {
                        finish();
                        // Add any additional navigation here
                    })
                    .start();
        } else {
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
                    waveLayer2A, waveLayer2B, null, null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animations != null) {
            animations.cleanupAnimations();
            animations = null;
        }
    }
}