package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QuizResults extends AppCompatActivity {
    TextView correct, incorrect;
    String crc;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);

        correct = findViewById(R.id.correct);
        incorrect = findViewById(R.id.incorrect);
        progressBar = findViewById(R.id.progressBar);

        int correctAnswers = getIntent().getIntExtra("correct", 0);
        int incorrectAnswers = getIntent().getIntExtra("incorrect", 0);

        correct.setText("You answered " + correctAnswers + "correct");
        incorrect.setText("You answered " + incorrectAnswers + "incorrect");

        int totalQuestions = getIntent().getIntExtra("size", 0); // Replace questionsLists with your actual list name
        int progressPercentage = (int) ((correctAnswers / (float) totalQuestions) * 100);
        progressBar.setProgress(progressPercentage);
    }
}