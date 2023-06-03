
package com.example.learn_english;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
/*
public class QuizActivityWords extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView questions, question;
    private AppCompatButton nextBtn, check;
    private EditText word;
    final List<QuestionsList> questionsLists = new ArrayList<>();
    private String getSelectedTopicName;
    private int rightAnswers = 0;
    private ImageView imageView;

    private int curQuestPos = 0;
    private String selectedOptionByUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_second);


        final TextView selectedTopicName = findViewById(R.id.topicName);
        getSelectedTopicName = getIntent().getStringExtra("selectedTopic");
        questions = findViewById(R.id.questions);
        question = findViewById(R.id.question);
        word = findViewById(R.id.word);
        nextBtn = findViewById(R.id.nextBtn);
        progressBar = findViewById(R.id.progressBar2);
        check = findViewById(R.id.check);
        selectedTopicName.setText(getSelectedTopicName);
        imageView = findViewById(R.id.imageView);


        loadQuestions();


        check.setOnClickListener(v -> {
            selectedOptionByUser = word.getText().toString().toLowerCase();
            revealAnswer();
            questionsLists.get(curQuestPos).setUserSelected(selectedOptionByUser);
        });

        nextBtn.setOnClickListener(v -> {
            if (selectedOptionByUser.isEmpty()) {
                Toast.makeText(QuizActivityWords.this, "Pls select an option", Toast.LENGTH_SHORT).show();
            } else {
                changeNextQuestion();
            }
        });
    }

    private boolean flag = false;

    private void changeNextQuestion() {
        curQuestPos++;

        if (curQuestPos == (questionsLists.size() - 1)) {
            nextBtn.setText("Finish");
        } else if (curQuestPos == questionsLists.size()) {
            flag = true;
//                ForRes();
        }
        if (flag == true) {

            ForRes();
        }
        if (curQuestPos < questionsLists.size()) {

            selectedOptionByUser = "";

//            option4.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_borderes_options));
//            option4.setTextColor(Color.parseColor("#1F6BB8"));
            question.setText(questionsLists.get(curQuestPos).getQuestion());

            questions.setText((curQuestPos + 1) + "/" + questionsLists.size());

//            option4.setText(questionsLists.get(curQuestPos).getOption4());

//                if (flag) {
//                    ForRes();
//                }
//                curQuestPos++;
        } else {
            ForRes();
        }
    }

    private void ForRes() {
        Intent intent = new Intent(QuizActivityWords.this, QuizResults.class);
        intent.putExtra("correct", getCorrectAnswers());
        intent.putExtra("incorrect", getInCorrectAnswers());
        intent.putExtra("size", questionsLists.size());
        startActivity(intent);
        finish();


    }

    private int getCorrectAnswers() {
        int correctAnswers = 0;
        for (int i = 0; i < questionsLists.size(); i++) {
            final String getUserSelectedAnswer = questionsLists.get(i).getUserSelected();
            final String getAnswer = questionsLists.get(i).getAnswer();
            if (getUserSelectedAnswer.equals(getAnswer)) {
                correctAnswers++;
            }
        }
        return correctAnswers;
    }

    private int getInCorrectAnswers() {
        int correctAnswers = 0;
        for (int i = 0; i < questionsLists.size(); i++) {
            final String getUserSelectedAnswer = questionsLists.get(i).getUserSelected();
            final String getAnswer = questionsLists.get(i).getAnswer();
            if (!getUserSelectedAnswer.equals(getAnswer)) {
                correctAnswers++;
                Log.i("correct", "" + correctAnswers);
            }
        }
        return correctAnswers;
    }


    @SuppressLint("ResourceAsColor")
    private void revealAnswer() {

        final String getAnswer = questionsLists.get(curQuestPos).getAnswer();

        if (word.getText().toString().toLowerCase().equals(getAnswer)) {
            word.setTextColor(Color.parseColor("#62B865"));
            word.setEnabled(false);
            // image to firebase
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lion);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//            byte[] imageBytes = baos.toByteArray();
//
//            String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//
//            // Save the Base64 image string to Firebase Realtime Database
//            DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference().child("words")
//                    .child("quiz").child(getSelectedTopicName).child("question2");
//            imageRef.child("image").setValue(base64Image);
//            Toast.makeText(this, "Image saved to Firebase!", Toast.LENGTH_SHORT).show();
        } else {
            word.setTextColor(Color.parseColor("#ff0000"));
            word.setText(getAnswer);
            word.setEnabled(false);

//            Toast.makeText(this, "wrong", Toast.LENGTH_SHORT).show();

//            word.setBackgroundColor(R.color.my_red);
        }

    }

    //    private class LoadQuestionsTask extends AsyncTask<Void, Void, List<QuestionsList>> {
//
//        @Override
//        protected List<QuestionsList> doInBackground(Void... voids) {
//
//            question.setVisibility(View.GONE);
//            questions.setVisibility(View.GONE);
//            word.setVisibility(View.GONE);
//            check.setVisibility(View.GONE);
////            option1.setVisibility(View.GONE);
////            option2.setVisibility(View.GONE);
////            option3.setVisibility(View.GONE);
////            option4.setVisibility(View.GONE);
//            nextBtn.setVisibility(View.GONE);
//
//
//            DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("words").child("quiz").child(getSelectedTopicName);
//
//            final CountDownLatch latch = new CountDownLatch(1);
//            firebaseDatabase.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    questionsLists.clear();
//                    for (DataSnapshot child : snapshot.getChildren()) {
//                        QuestionsList questionsList1 = child.getValue(QuestionsList.class);
//                        if (questionsList1 != null) {
//                            questionsLists.add(questionsList1);
//                        }
//                    }
//                    latch.countDown();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    questionsLists.add(new QuestionsList("q", "1", "2", "3", "4", "1"));
//                    latch.countDown();
//                }
//            });
//
//            try {
//                latch.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            return questionsLists;
//        }
//
//
//
//        @Override
//        protected void onPostExecute(List<QuestionsList> questionsLists) {
//            question.setVisibility(View.VISIBLE);
//            questions.setVisibility(View.VISIBLE);
//            word.setVisibility(View.VISIBLE);
//            check.setVisibility(View.VISIBLE);
//            nextBtn.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.GONE);
//
//            questions.setText((curQuestPos + 1) + "/" + questionsLists.size());
//            question.setText(questionsLists.get(0).getQuestion());
//            DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference()
//                    .child("words").child("quiz").child(getSelectedTopicName).child("question1").child("image");
////            imageRef.addValueEventListener(new ValueEventListener() {
////                                               @Override
////                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
////                                                   String base64Image = snapshot.getValue(String.class);
////                                                   if (base64Image != null) {
////                                                       byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
////                                                       Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
////                                                       imageView.setImageBitmap(bitmap);
////                                                   }
////                                               }
//                                           }
////
//////            option4.setText(questionsLists.get(0).getOption4());
////
//    }
//
//
//            private void loadQuestions () {
//                new LoadQuestionsTask().execute();
//            }
//        }
    private class LoadQuestionsTask extends AsyncTask<Void, Void, List<QuestionsList>> {

        @Override
        protected List<QuestionsList> doInBackground(Void... voids) {

            question.setVisibility(View.GONE);
            questions.setVisibility(View.GONE);
            word.setVisibility(View.GONE);
            check.setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);

            DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("words").child("quiz").child(getSelectedTopicName);

            final CountDownLatch latch = new CountDownLatch(1);
            firebaseDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    questionsLists.clear();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        QuestionsList questionsList1 = child.getValue(QuestionsList.class);
                        if (questionsList1 != null) {
                            questionsLists.add(questionsList1);
                        }
                    }
                    latch.countDown();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    questionsLists.add(new QuestionsList("q", "1", "2", "3", "4", "1"));
                    latch.countDown();
                }
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return questionsLists;
        }

        @Override
        protected void onPostExecute(List<QuestionsList> questionsLists) {
            question.setVisibility(View.VISIBLE);
            questions.setVisibility(View.VISIBLE);
            word.setVisibility(View.VISIBLE);
            check.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            questions.setText((curQuestPos + 1) + "/" + questionsLists.size());
            question.setText(questionsLists.get(0).getQuestion());
            DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference()
                    .child("words").child("quiz").child(getSelectedTopicName).child("image");
            imageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String base64Image = snapshot.getValue(String.class);
                    if (base64Image != null) {
                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle any errors or interruptions
                }
            });

            // Update the UI with the loaded questions and options
            // ...

        }
    }

    private void loadQuestions() {
        new LoadQuestionsTask().execute();
    }
}*/

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuizActivityWords extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView questions, question;
    private AppCompatButton nextBtn, check;
    private EditText word;
    private List<QuestionsList> questionsLists = new ArrayList<>();
    private String getSelectedTopicName;
    private int rightAnswers = 0;
    private ImageView imageView;

    private int curQuestPos = 0;
    private String selectedOptionByUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_second);

        final TextView selectedTopicName = findViewById(R.id.topicName);
        getSelectedTopicName = getIntent().getStringExtra("selectedTopic");
        questions = findViewById(R.id.questions);
        question = findViewById(R.id.question);
        word = findViewById(R.id.word);
        nextBtn = findViewById(R.id.nextBtn);
        progressBar = findViewById(R.id.progressBar2);
        check = findViewById(R.id.check);
        selectedTopicName.setText(getSelectedTopicName);
        imageView = findViewById(R.id.imageView);

        loadQuestions();

        check.setOnClickListener(v -> {
            selectedOptionByUser = word.getText().toString().toLowerCase();
            revealAnswer();
            questionsLists.get(curQuestPos).setUserSelected(selectedOptionByUser);
        });

        nextBtn.setOnClickListener(v -> {
            if (selectedOptionByUser.isEmpty()) {
                Toast.makeText(QuizActivityWords.this, "Please select an option", Toast.LENGTH_SHORT).show();
            } else {
                changeNextQuestion();
            }
        });
    }

    private void changeNextQuestion() {
        curQuestPos++;

        if (curQuestPos == (questionsLists.size() - 1)) {
            nextBtn.setText("Finish");
        } else if (curQuestPos == questionsLists.size()) {
            ForRes();
        }

        if (curQuestPos < questionsLists.size()) {
            selectedOptionByUser = "";
            question.setText(questionsLists.get(curQuestPos).getQuestion());
            questions.setText((curQuestPos + 1) + "/" + questionsLists.size());
            DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference()
                    .child("words").child("quiz").child(getSelectedTopicName).child("question" + (curQuestPos +1)).child("image");
            imageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String base64Image = snapshot.getValue(String.class);
                    if (base64Image != null) {
                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle any errors or interruptions
                }
            });
        } else {
            ForRes();
        }
    }

    private void ForRes() {
        Intent intent = new Intent(QuizActivityWords.this, QuizResults.class);
        intent.putExtra("correct", getCorrectAnswers());
        intent.putExtra("incorrect", getInCorrectAnswers());
        intent.putExtra("size", questionsLists.size());
        startActivity(intent);
        finish();
    }

    private int getCorrectAnswers() {
        int correctAnswers = 0;
        for (int i = 0; i < questionsLists.size(); i++) {
            final String getUserSelectedAnswer = questionsLists.get(i).getUserSelected();
            final String getAnswer = questionsLists.get(i).getAnswer();
            if (getUserSelectedAnswer.equals(getAnswer)) {
                correctAnswers++;
            }
        }
        return correctAnswers;
    }

    private int getInCorrectAnswers() {
        int correctAnswers = 0;
        for (int i = 0; i < questionsLists.size(); i++) {
            final String getUserSelectedAnswer = questionsLists.get(i).getUserSelected();
            final String getAnswer = questionsLists.get(i).getAnswer();
            if (!getUserSelectedAnswer.equals(getAnswer)) {
                correctAnswers++;
            }
        }
        return correctAnswers;
    }

    private void revealAnswer() {
        final String getAnswer = questionsLists.get(curQuestPos).getAnswer();

        if (word.getText().toString().toLowerCase().equals(getAnswer)) {
            word.setTextColor(Color.parseColor("#62B865"));
            word.setEnabled(false);
        } else {
            word.setTextColor(Color.parseColor("#ff0000"));
            word.setText(getAnswer);
            word.setEnabled(false);
        }
    }

    private class LoadQuestionsTask extends AsyncTask<Void, Void, List<QuestionsList>> {

        @Override
        protected List<QuestionsList> doInBackground(Void... voids) {
            DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference()
                    .child("words").child("quiz").child(getSelectedTopicName);

            firebaseDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    questionsLists.clear();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        QuestionsList questionsList1 = child.getValue(QuestionsList.class);
                        if (questionsList1 != null) {
                            questionsLists.add(questionsList1);
                        }
                    }
                    // Update the UI with the loaded questions and options
                    updateUI();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle any errors or interruptions
                }
            });

            return questionsLists;
        }

        private void updateUI() {
            question.setVisibility(View.VISIBLE);
            questions.setVisibility(View.VISIBLE);
            word.setVisibility(View.VISIBLE);
            check.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            questions.setText((curQuestPos + 1) + "/" + questionsLists.size());
            question.setText(questionsLists.get(0).getQuestion());

            DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference()
                    .child("words").child("quiz").child(getSelectedTopicName).child("question1").child("image");
            imageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String base64Image = snapshot.getValue(String.class);
                    if (base64Image != null) {
                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle any errors or interruptions
                }
            });
        }
    }

    private void loadQuestions() {
        new LoadQuestionsTask().execute();
    }
}


