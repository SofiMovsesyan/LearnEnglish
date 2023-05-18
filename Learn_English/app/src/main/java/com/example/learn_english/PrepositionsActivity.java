package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class PrepositionsActivity extends AppCompatActivity {
    ArrayList<PrepositionsModel> prepositionsModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepositions);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        setUpTenses();
        PrepositionsRecyclerViewAdapter adapter = new PrepositionsRecyclerViewAdapter(this, prepositionsModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
    private void setUpTenses() {
        String[] nameOfTense = getResources().getStringArray(R.array.prepositions);
        for (int i = 0; i < nameOfTense.length; i++) {
            prepositionsModels.add(new PrepositionsModel(nameOfTense[i]));
        }
    }
}