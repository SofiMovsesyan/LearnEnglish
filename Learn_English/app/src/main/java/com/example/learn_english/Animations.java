package com.example.learn_english;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animations {

    private static Animations instance;
    private final Random random = new Random();

    private ValueAnimator wave1Animator, wave2Animator, wave3Animator;
    private final List<ValueAnimator> bubbleAnimators = new ArrayList<>();

    public interface OnGlobalLayoutListener {
        void onGlobalLayout();
    }

    public static Animations getInstance() {
        if (instance == null) {
            instance = new Animations();
        }
        return instance;
    }

    public void startWaveAnimations(ImageView waveHeaderA, ImageView waveHeaderB,
                                    ImageView waveLayer2A, ImageView waveLayer2B,
                                    ImageView waveLayer3A, ImageView waveLayer3B) {
        startLayerAnimator(waveHeaderA, waveHeaderB, 12000, 0f);
        startLayerAnimator(waveLayer2A, waveLayer2B, 9000, 0f);
        startLayerAnimator(waveLayer3A, waveLayer3B, 6000, 0f);
    }

    private void startLayerAnimator(ImageView a, ImageView b, long duration, float yOffset) {
        if (a == null || b == null) return;

        final int width = a.getWidth();
        if (width == 0) {
            a.post(() -> startLayerAnimator(a, b, duration, yOffset));
            return;
        }

        a.setTranslationX(0f);
        b.setTranslationX(width);

        float yPixels = yOffset * a.getHeight();
        a.setTranslationY(yPixels);
        b.setTranslationY(yPixels);

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(animation -> {
            float fraction = (float) animation.getAnimatedValue();
            float x = -fraction * width;
            a.setTranslationX(x);
            b.setTranslationX(x + width);
        });

        animator.start();

        if (a.getId() == R.id.waveHeaderA) wave1Animator = animator;
        else if (a.getId() == R.id.waveLayer2A) wave2Animator = animator;
        else if (a.getId() == R.id.waveLayer3A) wave3Animator = animator;
    }

    public void startBubbleAnimations(View rootView, ImageView... bubbles) {
        if (rootView == null) return;

        final int containerHeight = rootView.getHeight();
        final int containerWidth = rootView.getWidth();

        for (int i = 0; i < bubbles.length; i++) {
            if (bubbles[i] != null) {
                int minDuration = 2500 + (i * 500);
                int maxDuration = 5500 + (i * 500);
                startSingleBubbleLoop(bubbles[i], containerWidth, containerHeight, minDuration, maxDuration);
            }
        }
    }

    private void startSingleBubbleLoop(ImageView bubble, int containerWidth, int containerHeight,
                                       int minDurationMs, int maxDurationMs) {
        if (bubble == null) return;

        if (bubble.getWidth() == 0 || bubble.getHeight() == 0) {
            bubble.post(() -> startSingleBubbleLoop(bubble, containerWidth, containerHeight, minDurationMs, maxDurationMs));
            return;
        }

        int bubbleWidth = bubble.getWidth();
        int maxX = Math.max(1, containerWidth - bubbleWidth);
        float targetX = random.nextInt(maxX);
        bubble.setX(targetX);

        float startY = containerHeight + random.nextInt(80);
        float endY = -bubble.getHeight() - 20f;

        int duration = minDurationMs + random.nextInt(Math.max(1, maxDurationMs - minDurationMs + 1));
        long startDelay = random.nextInt(900);

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
                bubbleAnimators.remove(animation);
                startSingleBubbleLoop(bubble, containerWidth, containerHeight, minDurationMs, maxDurationMs);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                bubbleAnimators.remove(animation);
            }
        });

        bubbleAnimators.add(animator);
        animator.start();
    }

    public void applyButtonClickAnimation(View button) {
        if (button == null) return;

        button.setOnClickListener(v -> {
            v.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .withEndAction(() -> v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start())
                    .start();
        });
    }

    public void fadeInView(View view, long duration) {
        if (view == null) return;

        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1f)
                .setDuration(duration)
                .setListener(null)
                .start();
    }

    public void slideUpView(View view, long duration) {
        if (view == null) return;

        view.setTranslationY(100f);
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(duration)
                .setListener(null)
                .start();
    }

    public void waitForLayout(View view, OnGlobalLayoutListener listener) {
        if (view == null || listener == null) return;

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                listener.onGlobalLayout();
            }
        });
    }

    public void cleanupAnimations() {
        cancelWaveAnimators();
        cancelBubbleAnimators();
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
    }

    private void cancelBubbleAnimators() {
        for (ValueAnimator animator : new ArrayList<>(bubbleAnimators)) {
            if (animator != null) {
                animator.cancel();
            }
        }
        bubbleAnimators.clear();
    }

    public void pauseAnimations() {
        cancelWaveAnimators();
        cancelBubbleAnimators();
    }

    public void resumeWaveAnimations(ImageView waveHeaderA, ImageView waveHeaderB,
                                     ImageView waveLayer2A, ImageView waveLayer2B,
                                     ImageView waveLayer3A, ImageView waveLayer3B) {
        if ((wave1Animator == null || !wave1Animator.isRunning())
                && waveHeaderA != null && waveHeaderA.getWidth() > 0) {
            startWaveAnimations(waveHeaderA, waveHeaderB, waveLayer2A, waveLayer2B, waveLayer3A, waveLayer3B);
        }
    }

    public void showErrorWithAnimation(View errorView, String message) {
        if (errorView == null) return;

        if (errorView instanceof TextView) {
            TextView textView = (TextView) errorView;
            textView.setText(message);
            textView.setBackgroundResource(R.drawable.error_background);
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0);
        }

        errorView.setAlpha(0f);
        errorView.setScaleX(0.8f);
        errorView.setScaleY(0.8f);
        errorView.setTranslationY(-20f);
        errorView.setVisibility(View.VISIBLE);

        errorView.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .translationY(0f)
                .setDuration(400)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

    public void showSuccessWithAnimation(View successView, String message) {
        if (successView == null) return;

        if (successView instanceof TextView) {
            TextView textView = (TextView) successView;
            textView.setText(message);
            textView.setBackgroundResource(R.drawable.success_background);
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_success, 0, 0, 0);
            textView.setTextColor(0xFF4ECDC4); // Success color
        }

        successView.setAlpha(0f);
        successView.setScaleX(0.8f);
        successView.setScaleY(0.8f);
        successView.setTranslationY(-20f);
        successView.setVisibility(View.VISIBLE);

        successView.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .translationY(0f)
                .setDuration(400)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

    public void hideMessageWithAnimation(View messageView) {
        if (messageView == null || messageView.getVisibility() != View.VISIBLE) return;

        messageView.animate()
                .alpha(0f)
                .scaleX(0.8f)
                .scaleY(0.8f)
                .translationY(-20f)
                .setDuration(300)
                .withEndAction(() -> messageView.setVisibility(View.GONE))
                .start();
    }

    public void shakeView(View view) {
        if (view == null) return;

        view.animate().cancel();
        view.setTranslationX(0);

        view.animate()
                .translationX(20f)
                .setDuration(50)
                .withEndAction(() -> view.animate()
                        .translationX(-20f)
                        .setDuration(50)
                        .withEndAction(() -> view.animate()
                                .translationX(15f)
                                .setDuration(50)
                                .withEndAction(() -> view.animate()
                                        .translationX(-15f)
                                        .setDuration(50)
                                        .withEndAction(() -> view.animate()
                                                .translationX(0f)
                                                .setDuration(50)
                                                .start())
                                        .start())
                                .start())
                        .start())
                .start();
    }
}