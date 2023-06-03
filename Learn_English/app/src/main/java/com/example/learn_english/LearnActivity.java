package com.example.learn_english;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class LearnActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextAdapter textAdapter;
    private List<TextItem> textList;
//    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

// Inside your onCreate() or onCreateView() method
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        textList = new ArrayList<>(); // Initialize the textList
        textAdapter = new TextAdapter(textList);
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(textAdapter);
        loadText();



    }

    /*private class LoadLearn extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String textFromResource = getResources().getString(R.string.PresentSimple);
            int chunkSize = 1000;

            List<String> textChunks = new ArrayList<>();
            for (int i = 0; i < textFromResource.length(); i += chunkSize) {
                int endIndex = Math.min(i + chunkSize, textFromResource.length());
                String chunk = textFromResource.substring(i, endIndex);
                textChunks.add(chunk);
            }
            DatabaseReference learnRef = FirebaseDatabase.getInstance().getReference("tenses/learn");

            final CountDownLatch latch = new CountDownLatch(1);

            for (int i = 0; i < textChunks.size(); i++) {
                String chunk = textChunks.get(i);
                learnRef.child("chunk" + (i + 1)).setValue(chunk);
            }
            final String[] fullText = new String[1];
            learnRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (DataSnapshot chunkSnapshot : dataSnapshot.getChildren()) {
                        String chunk = chunkSnapshot.getValue(String.class);
                        stringBuilder.append(chunk);
                    }

                     fullText[0] = stringBuilder.toString();
                    // Display the full text in your TextView
//                    textView.setText(fullText);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during the data retrieval
                    Log.e("Firebase", "Error retrieving data: " + databaseError.getMessage());
                }
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return fullText[0];
        }

        @Override
        protected void onPostExecute(List<QuestionsList> questionsLists) {
            // Shuffle the questions and options
            textView.setText(fullText);
        }
    }


    private void loadQuestions() {
        new QuizActivityTenses.LoadLearn().execute();
    }*/

    /*private class LoadLearn extends AsyncTask<Void, Void, String> {
        private TextView textView;

        public LoadLearn(TextView textView) {
            this.textView = textView;
        }
        @Override
        protected String doInBackground(Void... voids) {
            String textFromResource = getResources().getString(R.string.PresentSimple);
            int chunkSize = 1000;

            List<String> textChunks = new ArrayList<>();
            for (int i = 0; i < textFromResource.length(); i += chunkSize) {
                int endIndex = Math.min(i + chunkSize, textFromResource.length());
                String chunk = textFromResource.substring(i, endIndex);
                textChunks.add(chunk);
            }

            DatabaseReference learnRef = FirebaseDatabase.getInstance().getReference("tenses/learn");

            for (int i = 0; i < textChunks.size(); i++) {
                String chunk = textChunks.get(i);
                learnRef.child("chunk" + (i + 1)).setValue(chunk);
            }

            final StringBuilder stringBuilder = new StringBuilder();
            final CountDownLatch latch = new CountDownLatch(1);

            learnRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot chunkSnapshot : dataSnapshot.getChildren()) {
                        String chunk = chunkSnapshot.getValue(String.class);
                        stringBuilder.append(chunk);
                    }


                    latch.countDown();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during the data retrieval
                    Log.e("eee", "Error retrieving data: " + databaseError.getMessage());
                    latch.countDown();
                }
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            Log.e("eee", "data: " + stringBuilder.toString());

            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String fullText) {
                        Log.e("eee", "data: " + fullText);

            textView.setText(fullText);


        }
    }
*/

    private class LoadLearn extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {
            String textFromResource = getResources().getString(R.string.PresentSimple);
            int chunkSize = 50;

            List<String> textChunks = new ArrayList<>();
            for (int i = 0; i < textFromResource.length(); i += chunkSize) {
                int endIndex = Math.min(i + chunkSize, textFromResource.length());
                String chunk = textFromResource.substring(i, endIndex);
                textChunks.add(chunk);
            }

            DatabaseReference learnRef = FirebaseDatabase.getInstance().getReference("tenses/learn");

            for (int i = 0; i < textChunks.size(); i++) {
                String chunk = textChunks.get(i);
                learnRef.child("chunk" + (i + 1)).setValue(chunk);
            }

            final StringBuilder stringBuilder = new StringBuilder();
            final CountDownLatch latch = new CountDownLatch(1);

            learnRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot chunkSnapshot : dataSnapshot.getChildren()) {
                        String chunk = chunkSnapshot.getValue(String.class);
                        stringBuilder.append(chunk);
                    }
                    latch.countDown();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during the data retrieval
                    Log.e("Firebase", "Error retrieving data: " + databaseError.getMessage());
                    latch.countDown();
                }
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String fullText) {
            Log.e("eee", "data: " + fullText);

            // Display the full text in your TextView
            textList.add(new TextItem(fullText));
            textAdapter.notifyDataSetChanged();
        }
    }

    private void loadText() {
        new LoadLearn().execute();
    }

}