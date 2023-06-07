package com.example.learn_english;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class LearnActivity extends AppCompatActivity {
    private List<TextItem> textList;

    private String getSelectedTopicName;

    TextView tvLearn, topicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);


        textList = new ArrayList<>();
        getSelectedTopicName = getIntent().getStringExtra("selectedTopic");

        topicName = findViewById(R.id.topicName);
        topicName.setText(getSelectedTopicName);
        getSelectedTopicName = getSelectedTopicName.replace(" ", "");
        tvLearn = findViewById(R.id.learn);
        loadText();




    }


    private class LoadLearn extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {

            DatabaseReference learnRef = FirebaseDatabase.getInstance().getReference("learn").child(getSelectedTopicName);

            final String[] text = {""};

            final CountDownLatch latch = new CountDownLatch(1);

            learnRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot chunkSnapshot : dataSnapshot.getChildren()) {

                        String learn = chunkSnapshot.getValue(String.class);
                        text[0] = learn;
//                        stringBuilder.append(chunk);
                    }
                    latch.countDown();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error retrieving data: " + databaseError.getMessage());
                    latch.countDown();
                }
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return text[0];
        }

        @Override
        protected void onPostExecute(String fullText) {
            Log.e("eee", "data: " + fullText);
            fullText = fullText.replace("/n", "\n");
            tvLearn.setText(fullText);
        }
    }

    private void loadText() {
        new LoadLearn().execute();
    }

}