package com.example.learn_english;

public class Tenses {
    private int presentSimpleProgress;
    private int presentContinuousProgress;

    public Tenses() {
        // Default constructor required for Firebase serialization
    }

    public Tenses(int presentSimpleProgress, int presentContinuousProgress) {
        this.presentSimpleProgress = presentSimpleProgress;
        this.presentContinuousProgress = presentContinuousProgress;
    }

    public int getPresentSimpleProgress() {
        return presentSimpleProgress;
    }

    public void setPresentSimpleProgress(int presentSimpleProgress) {
        this.presentSimpleProgress = presentSimpleProgress;
    }

    public int getPresentContinuousProgress() {
        return presentContinuousProgress;
    }

    public void setPresentContinuousProgress(int presentContinuousProgress) {
        this.presentContinuousProgress = presentContinuousProgress;
    }
}
