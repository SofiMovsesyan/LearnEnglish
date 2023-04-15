package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class TensesActivity extends AppCompatActivity {
    ArrayList<TensesModel> tensesModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenses);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        setUpTenses();
        NRecyclerViewAdapter adapter = new NRecyclerViewAdapter(this, tensesModels);
        Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpTenses() {
        String[] nameOfTense = getResources().getStringArray(R.array.filters);
        for (int i = 0; i < nameOfTense.length; i++) {
            tensesModels.add(new TensesModel(nameOfTense[i]));
        }
    }
}