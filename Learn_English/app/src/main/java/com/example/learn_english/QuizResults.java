package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
public class QuizResults extends AppCompatActivity {
    TextView correct, incorrect;
    ProgressBar progressBar;

    TensesRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);

        correct = findViewById(R.id.correct);
        incorrect = findViewById(R.id.incorrect);
        progressBar = findViewById(R.id.progressBar);

        int correctAnswers = getIntent().getIntExtra("correct", 0);
        int incorrectAnswers = getIntent().getIntExtra("incorrect", 0);

        correct.setText(correctAnswers + "");
        incorrect.setText(incorrectAnswers + "");

        int totalQuestions = getIntent().getIntExtra("size", 0); // Replace questionsLists with your actual list name
        int progressPercentage = (int) ((correctAnswers / (float) totalQuestions) * 100);
        progressBar.setProgress(progressPercentage);


        int itemPosition = getIntent().getIntExtra("itemPosition", -1); // Replace with the actual item position
        int progress = progressPercentage; // Replace with the actual progress value

        // Update the progress of the specific item in the adapter
        if (adapter != null) {
            adapter.updateProgress(itemPosition, progress);
        }

        // Save the progress value in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("progress", progress);
        editor.apply();
    }
}*/

public class QuizResults extends AppCompatActivity {

    TextView correct;
    TextView incorrect;
    ProgressBar progressBar;
    private String getSelectedTopicName;

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);

        correct = findViewById(R.id.correct);
        incorrect = findViewById(R.id.incorrect);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.txt);
        getSelectedTopicName = getIntent().getStringExtra("selectedTopicName");

        getSelectedTopicName = getSelectedTopicName.replace(" ", "");
        int correctAnswers = getIntent().getIntExtra("correct", 0);
        int incorrectAnswers = getIntent().getIntExtra("incorrect", 0);

        if (correctAnswers <= 5) {
            textView.setText("You can do better!");
        }else {
            textView.setText("You've completed quiz successfully");
        }
        correct.setText(String.valueOf(correctAnswers));
        incorrect.setText(String.valueOf(incorrectAnswers));

        int totalQuestions = getIntent().getIntExtra("size", 0);
        int progressPercentage = (int) ((correctAnswers / (float) totalQuestions) * 100);

        // Get the current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


       DatabaseReference tensesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("tenses");
        HashMap<String, Object> tensesMap = new HashMap<>();
        tensesMap.put(getSelectedTopicName, progressPercentage);
        tensesRef.updateChildren(tensesMap);

        DatabaseReference prepRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("prepositions");
        HashMap<String, Object> prepMap = new HashMap<>();
        prepMap.put(getSelectedTopicName, progressPercentage);
        prepRef.updateChildren(prepMap);

        DatabaseReference wordRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("words");
        HashMap<String, Object> wordMap = new HashMap<>();
        wordMap.put(getSelectedTopicName, progressPercentage);
        wordRef.updateChildren(prepMap);
//        }
        progressBar.setProgress(progressPercentage);

//        int itemPosition = getIntent().getIntExtra("itemPosition", -1);
//        int progress = progressPercentage;
//
//        if (adapter != null) {
//            adapter.updateProgress(itemPosition, progress);
//        }
//
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("progress", progress);
//        editor.apply();
    }
}
