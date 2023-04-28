package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class QuizResults extends AppCompatActivity {
TextView correct, incorrect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);
        correct = findViewById(R.id.correct); 
        correct.setText(getIntent().getStringExtra("correct"));
        incorrect = findViewById(R.id.incorrect);
        incorrect.setText(getIntent().getStringExtra("correct"));
    }
}