package com.example.learn_english;

public class User {
    private String id;
    private String email;
    //    private int progress;
    private Tenses tenses;
    private Prepositions prepositions;
    public User() {
        // Default constructor required for Firebase
    }

    public User(String id, String email) {
        this.id = id;
        this.email = email; // Initialize progress to 0
    }

    // Getters and setters for the properties
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public int getProgress() {
//        return progress;
//    }
//
//    public void setProgress(int progress) {
//        this.progress = progress;
//    }

    public Tenses getTenses() {
        return tenses;
    }

    public void setTenses(Tenses tenses) {
        this.tenses = tenses;
    }

    public Prepositions getPrepositions() {
        return prepositions;
    }

    public void setPrepositions(Prepositions prepositions) {
        this.prepositions = prepositions;
    }
}