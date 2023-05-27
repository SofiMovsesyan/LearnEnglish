package com.example.learn_english;
public class TensesModel {
    private String tenseName;
    private int progress;

    public TensesModel(String tenseName, int progress) {
        this.tenseName = tenseName;
        this.progress = progress;
    }

    public String getTenseName() {
        return tenseName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
