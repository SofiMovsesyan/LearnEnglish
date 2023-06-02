package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);

        correct = findViewById(R.id.correct);
        incorrect = findViewById(R.id.incorrect);
        progressBar = findViewById(R.id.progressBar);
        getSelectedTopicName = getIntent().getStringExtra("selectedTopicName");

        Toast.makeText(this, "" + getSelectedTopicName, Toast.LENGTH_SHORT).show();
        getSelectedTopicName = getSelectedTopicName.replace(" ", "");
        int correctAnswers = getIntent().getIntExtra("correct", 0);
        int incorrectAnswers = getIntent().getIntExtra("incorrect", 0);

        correct.setText(String.valueOf(correctAnswers));
        incorrect.setText(String.valueOf(incorrectAnswers));

        int totalQuestions = getIntent().getIntExtra("size", 0);
        int progressPercentage = (int) ((correctAnswers / (float) totalQuestions) * 100);

        // Get the current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


// Get a reference to the user's tenses node in the Firebase Realtime Database
        if (getSelectedTopicName.equals("PresentSimple") || getSelectedTopicName.equals("PresentContinuous") ||
        getSelectedTopicName.equals("PresentPerfect")|| getSelectedTopicName.equals("PresentPerfectContinuous")||
        getSelectedTopicName.equals("PastSimple") || getSelectedTopicName.equals("PastContinuous") ||
        getSelectedTopicName.equals("PastPerfect")  || getSelectedTopicName.equals("PastPerfectContinuous") ||
        getSelectedTopicName.equals("FutureSimple") || getSelectedTopicName.equals("FutureContinuous") ||
        getSelectedTopicName.equals("FuturePerfect") || getSelectedTopicName.equals("FuturePerfectContinuous")) {
            DatabaseReference tensesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("tenses");

// Create a HashMap to store the tenses values
            HashMap<String, Object> tensesMap = new HashMap<>();
            tensesMap.put(getSelectedTopicName, progressPercentage);

// Update the tenses values in the Firebase Realtime Database
            tensesRef.updateChildren(tensesMap);
        }else {
            Toast.makeText(this, "else", Toast.LENGTH_SHORT).show();
            DatabaseReference prepRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("prepositions");

// Create a HashMap to store the tenses values
            HashMap<String, Object> prepMap = new HashMap<>();
            prepMap.put(getSelectedTopicName, progressPercentage);

// Update the tenses values in the Firebase Realtime Database
            prepRef.updateChildren(prepMap);
        }
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

