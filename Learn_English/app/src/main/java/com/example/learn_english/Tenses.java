package com.example.learn_english;

public class Tenses {
    private int PresentSimple;
    private int PresentContinuous;
    private int PresentPerfect;
    private int PresentPerfectContinuous;
    private int PastSimple;
    private int PastContinuous;
    private int PastPerfect;
    private int PastPerfectContinuous;
    private int FutureSimple;
    private int FutureContinuous;
    private int FuturePerfect;
    private int FuturePerfectContinuous;


    public Tenses() {
        // Default constructor required for Firebase serialization
    }

    public Tenses(int presentSimple, int presentContinuous, int presentPerfect, int presentPerfectContinuous, int pastSimple, int pastContinuous, int pastPerfect, int pastPerfectContinuous, int futureSimple, int futureContinuous, int futurePerfect, int futurePerfectContinuous) {
        this.PresentSimple = presentSimple;
        this.PresentContinuous = presentContinuous;
        this.PresentPerfect = presentPerfect;
        this.PresentPerfectContinuous = presentPerfectContinuous;
        this.PastSimple = pastSimple;
        this.PastContinuous = pastContinuous;
        this.PastPerfect = pastPerfect;
        this.PastPerfectContinuous = pastPerfectContinuous;
        this.FutureSimple = futureSimple;
        this.FutureContinuous = futureContinuous;
        this.FuturePerfect = futurePerfect;
        this.FuturePerfectContinuous = futurePerfectContinuous;
    }

    public int getPresentPerfect() {
        return PresentPerfect;
    }

    public void setPresentPerfect(int presentPerfect) {
        PresentPerfect = presentPerfect;
    }

    public int getPresentPerfectContinuous() {
        return PresentPerfectContinuous;
    }

    public void setPresentPerfectContinuous(int presentPerfectContinuous) {
        PresentPerfectContinuous = presentPerfectContinuous;
    }

    public int getPastSimple() {
        return PastSimple;
    }

    public void setPastSimple(int pastSimple) {
        PastSimple = pastSimple;
    }

    public int getPastContinuous() {
        return PastContinuous;
    }

    public void setPastContinuous(int pastContinuous) {
        PastContinuous = pastContinuous;
    }

    public int getPastPerfect() {
        return PastPerfect;
    }

    public void setPastPerfect(int pastPerfect) {
        PastPerfect = pastPerfect;
    }

    public int getPastPerfectContinuous() {
        return PastPerfectContinuous;
    }

    public void setPastPerfectContinuous(int pastPerfectContinuous) {
        PastPerfectContinuous = pastPerfectContinuous;
    }

    public int getFutureSimple() {
        return FutureSimple;
    }

    public void setFutureSimple(int futureSimple) {
        FutureSimple = futureSimple;
    }

    public int getFutureContinuous() {
        return FutureContinuous;
    }

    public void setFutureContinuous(int futureContinuous) {
        FutureContinuous = futureContinuous;
    }

    public int getFuturePerfect() {
        return FuturePerfect;
    }

    public void setFuturePerfect(int futurePerfect) {
        FuturePerfect = futurePerfect;
    }

    public int getFuturePerfectContinuous() {
        return FuturePerfectContinuous;
    }

    public void setFuturePerfectContinuous(int futurePerfectContinuous) {
        FuturePerfectContinuous = futurePerfectContinuous;
    }

    public int getPresentSimple() {
        return PresentSimple;
    }

    public void setPresentSimple(int presentSimple) {
        PresentSimple = presentSimple;
    }

    public int getPresentContinuous() {
        return PresentContinuous;
    }

    public void setPresentContinuous(int presentContinuous) {
        PresentContinuous = presentContinuous;
    }
}