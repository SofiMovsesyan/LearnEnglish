package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    AppCompatButton sign_in, sign_up;

    private ImageView waveHeaderA, waveHeaderB;
    private ImageView waveLayer2A, waveLayer2B;
    private ImageView waveLayer3A, waveLayer3B;

    private ValueAnimator wave1Animator, wave2Animator, wave3Animator;

    private final List<ValueAnimator> bubbleAnimators = new ArrayList<>();
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        waveHeaderA = findViewById(R.id.waveHeaderA);
        waveHeaderB = findViewById(R.id.waveHeaderB);
        waveLayer2A = findViewById(R.id.waveLayer2A);
        waveLayer2B = findViewById(R.id.waveLayer2B);
        waveLayer3A = findViewById(R.id.waveLayer3A);
        waveLayer3B = findViewById(R.id.waveLayer3B);

        sign_in = findViewById(R.id.sign_in);
        sign_up = findViewById(R.id.sign_up);

        sign_in.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        sign_up.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        View root = findViewById(android.R.id.content);
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                startWaveAnimations();
                startBubbleAnimations();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn", false);
        if (hasLoggedIn) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void startWaveAnimations() {
        startLayerAnimator(waveHeaderA, waveHeaderB, 12000, 0f);
        startLayerAnimator(waveLayer2A, waveLayer2B, 9000, 0f);
        startLayerAnimator(waveLayer3A, waveLayer3B, 6000, 0f);
    }

    private void startLayerAnimator(ImageView a, ImageView b, long duration, float yOffset) {

        final int width = a.getWidth();
        if (width == 0) {
            a.post(() -> startLayerAnimator(a, b, duration, yOffset));
            return;
        }

        // Position b directly to the right of a
        a.setTranslationX(0f);
        b.setTranslationX(width);

        // Apply small vertical offset if requested
        float yPixels = yOffset * a.getHeight();
        a.setTranslationY(yPixels);
        b.setTranslationY(yPixels);

        // Use a repeating ValueAnimator from 0..1 mapping to translation -0..-width
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(animation -> {
            float fraction = (float) animation.getAnimatedValue();
            float x = -fraction * width;
            // Move first image
            a.setTranslationX(x);
            // Place second immediately to its right
            b.setTranslationX(x + width);
        });

        animator.start();

        // Keep references for cleanup
        if (a == waveHeaderA) wave1Animator = animator;
        else if (a == waveLayer2A) wave2Animator = animator;
        else if (a == waveLayer3A) wave3Animator = animator;
    }

    private void startBubbleAnimations() {
        // Find bubbles
        ImageView bubble1 = findViewById(R.id.bubble1);
        ImageView bubble2 = findViewById(R.id.bubble2);
        ImageView bubble3 = findViewById(R.id.bubble3);
        ImageView bubble4 = findViewById(R.id.bubble4);

        View root = findViewById(android.R.id.content);
        final int containerHeight = root.getHeight();
        final int containerWidth = root.getWidth();

        // Start independent infinite loops for each bubble
        startSingleBubbleLoop(bubble1, containerWidth, containerHeight, 3200, 6000);
        startSingleBubbleLoop(bubble2, containerWidth, containerHeight, 3500, 7000);
        startSingleBubbleLoop(bubble3, containerWidth, containerHeight, 3000, 6500);
        startSingleBubbleLoop(bubble4, containerWidth, containerHeight, 2500, 5500);
    }

    /**
     * Starts an infinite loop animation for one bubble:
     * - positions horizontally at a random x
     * - positions vertically just below the bottom
     * - animates smoothly upward to above the top
     * - when finished, schedules a restart with a small random delay
     */
    private void startSingleBubbleLoop(ImageView bubble, int containerWidth, int containerHeight,
                                       int minDurationMs, int maxDurationMs) {
        if (bubble == null) return;

        // Ensure bubble measured
        if (bubble.getWidth() == 0 || bubble.getHeight() == 0) {
            bubble.post(() -> startSingleBubbleLoop(bubble, containerWidth, containerHeight, minDurationMs, maxDurationMs));
            return;
        }

        // Place bubble at a random horizontal X within container
        int bubbleWidth = bubble.getWidth();
        int maxX = Math.max(1, containerWidth - bubbleWidth);
        float targetX = random.nextInt(maxX);
        // setX sets the absolute X position relative to parent — good for moving while keeping constraints
        bubble.setX(targetX);

        // Starting Y is just below the bottom edge
        float startY = containerHeight + random.nextInt(80); // slight variation
        // Ending Y is above the top by bubble height
        float endY = -bubble.getHeight() - 20f;

        // Randomize duration between provided min/max
        int duration = minDurationMs + random.nextInt(Math.max(1, maxDurationMs - minDurationMs + 1));
        long startDelay = random.nextInt(900); // staggered start

        // Reset translationY to start
        bubble.setTranslationY(startY);

        ValueAnimator animator = ValueAnimator.ofFloat(startY, endY);
        animator.setDuration(duration);
        animator.setStartDelay(startDelay);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> {
            float y = (float) animation.getAnimatedValue();
            bubble.setTranslationY(y);
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Cleanup finished animator from list
                bubbleAnimators.remove(animation);

                // If activity still running, restart this bubble after a small random pause
                if (!isFinishing() && !isDestroyed()) {
                    long pause = 200 + random.nextInt(800);
                    bubble.postDelayed(() ->
                                    startSingleBubbleLoop(bubble, containerWidth, containerHeight, minDurationMs, maxDurationMs),
                            pause);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                bubbleAnimators.remove(animation);
            }
        });

        bubbleAnimators.add(animator);
        animator.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelWaveAnimators();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure wave animators are running if views are measured
        if ((wave1Animator == null || !wave1Animator.isRunning())
                && waveHeaderA.getWidth() > 0) {
            startWaveAnimations();
        }
        // Bubbles will restart on layout if needed; they are managed in startBubbleAnimations.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelWaveAnimators();
    }

    private void cancelWaveAnimators() {
        if (wave1Animator != null) {
            wave1Animator.cancel();
            wave1Animator = null;
        }
        if (wave2Animator != null) {
            wave2Animator.cancel();
            wave2Animator = null;
        }
        if (wave3Animator != null) {
            wave3Animator.cancel();
            wave3Animator = null;
        }

        // Cancel bubble animators
        for (ValueAnimator a : new ArrayList<>(bubbleAnimators)) {
            if (a != null) {
                a.cancel();
            }
        }
        bubbleAnimators.clear();
    }
}