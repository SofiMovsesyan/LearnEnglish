package com.example.learn_english;
public class TensesModel {
    private String tenseName;
    private int progress;
    private String itemId;
    // Other fields and methods


    public TensesModel(String tenseName, int progress) {
        this.tenseName = tenseName;
        this.progress = progress;
    }
    public String getItemId() {
        return itemId;
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
