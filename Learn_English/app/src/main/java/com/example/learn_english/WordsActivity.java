package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;

public class WordsActivity extends AppCompatActivity {
    ArrayList<WordsModel> wordsModels = new ArrayList<>();
    private int progress;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        setUpTenses();
        WordsRecyclerViewAdapter adapter = new WordsRecyclerViewAdapter(this, wordsModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        progress = sharedPreferences.getInt("progress", 0);
        int itemPosition = getIntent().getIntExtra("itemPosition", -1);
        int quizProgress = getIntent().getIntExtra("quizProgress", 0);

        // Update the progress of the specific item in the adapter
        if (itemPosition != -1) {
            WordsModel model = wordsModels.get(itemPosition);
            model.setProgress(quizProgress);
            adapter.notifyItemChanged(itemPosition);
        }
    }
    private void setUpTenses() {
        String[] nameOfTense = getResources().getStringArray(R.array.words);
        for (int i = 0; i < nameOfTense.length; i++) {
            wordsModels.add(new WordsModel(nameOfTense[i], progress));
        }
    }
}