package com.example.learn_english;

public class PrepositionsModel {
    String prepostionName;
    private int progress;

    public PrepositionsModel(String prepostionName, int progress) {
        this.prepostionName = prepostionName;
        this.progress = progress;
    }

    public String getPrepostionName() {
        return prepostionName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}