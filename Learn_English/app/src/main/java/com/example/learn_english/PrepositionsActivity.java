package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;

public class PrepositionsActivity extends AppCompatActivity {
    ArrayList<PrepositionsModel> prepositionsModels = new ArrayList<>();
    private int progress;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepositions);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        setUpTenses();
        PrepositionsRecyclerViewAdapter adapter = new PrepositionsRecyclerViewAdapter(this, prepositionsModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        progress = sharedPreferences.getInt("progress", 0);
        int itemPosition = getIntent().getIntExtra("itemPosition", -1);
        int quizProgress = getIntent().getIntExtra("quizProgress", 0);

        // Update the progress of the specific item in the adapter
        if (itemPosition != -1) {
            PrepositionsModel model = prepositionsModels.get(itemPosition);
            model.setProgress(quizProgress);
            adapter.notifyItemChanged(itemPosition);
        }

    }
    private void setUpTenses() {
        String[] nameOfTense = getResources().getStringArray(R.array.prepositions);
        for (int i = 0; i < nameOfTense.length; i++) {
            prepositionsModels.add(new PrepositionsModel(nameOfTense[i], progress));
        }
    }
}