package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class TensesActivity extends AppCompatActivity{
    ArrayList<TensesModel> tensesModels = new ArrayList<>();
    private String selectedTopicName = "";
    CardView Tense;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenses);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        setUpTenses();
        NRecyclerViewAdapter adapter = new NRecyclerViewAdapter(this, tensesModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        String yourClikedItem = adapter.yourClikedItem;
//        Toast.makeText(this, yourClikedItem, Toast.LENGTH_SHORT).show();


        Tense = findViewById(R.id.Tense);
        Tense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(TensesActivity.this, "AAAA", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpTenses() {
        String[] nameOfTense = getResources().getStringArray(R.array.filters);
        for (int i = 0; i < nameOfTense.length; i++) {
            tensesModels.add(new TensesModel(nameOfTense[i]));
        }
    }
}