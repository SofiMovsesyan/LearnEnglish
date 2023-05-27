
package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;
/*
public class TensesActivity extends AppCompatActivity{
    ArrayList<TensesModel> tensesModels = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private int progress;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenses);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        setUpTenses();
        TensesRecyclerViewAdapter adapter = new TensesRecyclerViewAdapter(this, tensesModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        progress = sharedPreferences.getInt("progress", 0);


        // Pass the progress value to the adapter

//        String yourClikedItem = adapter.yourClikedItem;
//        Toast.makeText(this, yourClikedItem, Toast.LENGTH_SHORT).show();


//        Tense = findViewById(R.id.Tense);
//        Tense.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(TensesActivity.this, "AAAA", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void setUpTenses() {
        String[] nameOfTense = getResources().getStringArray(R.array.tenses);
        for (int i = 0; i < nameOfTense.length; i++) {
            tensesModels.add(new TensesModel(nameOfTense[i], 0));
        }
    }

}*/
public class TensesActivity extends AppCompatActivity {
    ArrayList<TensesModel> tensesModels = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenses);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        setUpTenses();
        TensesRecyclerViewAdapter adapter = new TensesRecyclerViewAdapter(this, tensesModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        progress = sharedPreferences.getInt("progress", 0);

        int itemPosition = getIntent().getIntExtra("itemPosition", -1);
        int quizProgress = getIntent().getIntExtra("quizProgress", 0);

        // Update the progress of the specific item in the adapter
        if (itemPosition != -1) {
            TensesModel model = tensesModels.get(itemPosition);
            model.setProgress(quizProgress);
            adapter.notifyItemChanged(itemPosition);
        }
    }

    private void setUpTenses() {
        String[] nameOfTense = getResources().getStringArray(R.array.tenses);
        for (int i = 0; i < nameOfTense.length; i++) {
            tensesModels.add(new TensesModel(nameOfTense[i], 0));
        }
    }
}
