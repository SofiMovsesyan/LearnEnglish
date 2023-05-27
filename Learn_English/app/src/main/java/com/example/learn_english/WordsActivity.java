package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class WordsActivity extends AppCompatActivity {
    ArrayList<WordsModel> wordsModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        setUpTenses();
        WordsRecyclerViewAdapter adapter = new WordsRecyclerViewAdapter(this, wordsModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    private void setUpTenses() {
        String[] nameOfTense = getResources().getStringArray(R.array.words);
        for (int i = 0; i < nameOfTense.length; i++) {
            wordsModels.add(new WordsModel(nameOfTense[i]));
        }
    }
}