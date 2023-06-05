package com.example.learn_english;

public class WordsModel {
    String wordsName;
    private int progress;
    private String itemId;

    public WordsModel(String wordsName, int progress) {
        this.wordsName = wordsName;
        this.progress = progress;
    }

    public String getWordsName() {
        return wordsName;
    }

    public String getItemId() {
        return itemId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}