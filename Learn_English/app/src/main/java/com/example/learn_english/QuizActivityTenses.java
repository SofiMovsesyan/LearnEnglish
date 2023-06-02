package com.example.learn_english;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class QuizActivityTenses extends AppCompatActivity {
    private TextView questions, question;
    private ProgressBar progressBar;
    private AppCompatButton option1, option2, option3, option4, nextBtn;
    final List<QuestionsList> questionsLists = new ArrayList<>();
    private String getSelectedTopicName;
    private int rightAnswers = 0;

    private int curQuestPos = 0;
    private String selectedOptionByUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_first);

        final TextView selectedTopicName = findViewById(R.id.topicName);
        getSelectedTopicName = getIntent().getStringExtra("selectedTopic");
        questions = findViewById(R.id.questions);
        question = findViewById(R.id.question);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextBtn = findViewById(R.id.nextBtn);
        progressBar = findViewById(R.id.progressBar2);
        selectedTopicName.setText(getSelectedTopicName);

        loadQuestions();

        option1.setOnClickListener(v -> {
            if (selectedOptionByUser.isEmpty()) {
                selectedOptionByUser = option1.getText().toString();
                option1.setBackgroundResource(R.drawable.rounded_borderes_wrong);
                option1.setTextColor(Color.WHITE);
                revealAnswer();
                questionsLists.get(curQuestPos).setUserSelected(selectedOptionByUser);

            }
        });

        option2.setOnClickListener(v -> {
            if (selectedOptionByUser.isEmpty()) {
                selectedOptionByUser = option2.getText().toString();
                option2.setBackgroundResource(R.drawable.rounded_borderes_wrong);
                option2.setTextColor(Color.WHITE);
                revealAnswer();
                questionsLists.get(curQuestPos).setUserSelected(selectedOptionByUser);

            }
        });
        option3.setOnClickListener(v -> {
            if (selectedOptionByUser.isEmpty()) {
                selectedOptionByUser = option3.getText().toString();
                option3.setBackgroundResource(R.drawable.rounded_borderes_wrong);
                option3.setTextColor(Color.WHITE);
                revealAnswer();
                questionsLists.get(curQuestPos).setUserSelected(selectedOptionByUser);

            }
        });

        option4.setOnClickListener(v -> {
            if (selectedOptionByUser.isEmpty()) {
                selectedOptionByUser = option4.getText().toString();
                option4.setBackgroundResource(R.drawable.rounded_borderes_wrong);
                option4.setTextColor(Color.WHITE);
                revealAnswer();
                questionsLists.get(curQuestPos).setUserSelected(selectedOptionByUser);

            }
        });

        nextBtn.setOnClickListener(v -> {
            if (selectedOptionByUser.isEmpty()) {
                Toast.makeText(QuizActivityTenses.this, "Pls select an option", Toast.LENGTH_SHORT).show();
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
            option1.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_borderes_options));
            option1.setTextColor(Color.parseColor("#1F6BB8"));

            option2.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_borderes_options));
            option2.setTextColor(Color.parseColor("#1F6BB8"));

            option3.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_borderes_options));
            option3.setTextColor(Color.parseColor("#1F6BB8"));

            option4.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_borderes_options));
            option4.setTextColor(Color.parseColor("#1F6BB8"));

            questions.setText((curQuestPos + 1) + "/" + questionsLists.size());
            question.setText(questionsLists.get(curQuestPos).getQuestion());
            option1.setText(questionsLists.get(curQuestPos).getOption1());
            option2.setText(questionsLists.get(curQuestPos).getOption2());
            option3.setText(questionsLists.get(curQuestPos).getOption3());
            option4.setText(questionsLists.get(curQuestPos).getOption4());
//                if (flag) {
//                    ForRes();
//                }
//                curQuestPos++;
        } else {
            ForRes();
        }
    }

    private void ForRes() {
        Intent intent = new Intent(QuizActivityTenses.this, QuizResults.class);
        intent.putExtra("correct", getCorrectAnswers());
        intent.putExtra("incorrect", getInCorrectAnswers());
        intent.putExtra("selectedTopicName", getSelectedTopicName);
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

    private void revealAnswer() {
        final String getAnswer = questionsLists.get(curQuestPos).getAnswer();
        if (option1.getText().toString().equals(getAnswer)) {
            option1.setBackgroundResource(R.drawable.rounded_borderes_correct);
            option1.setTextColor(Color.WHITE);
            rightAnswers++;
        } else if (option2.getText().toString().equals(getAnswer)) {
            option2.setBackgroundResource(R.drawable.rounded_borderes_correct);
            option2.setTextColor(Color.WHITE);
            rightAnswers++;
        } else if (option3.getText().toString().equals(getAnswer)) {
            option3.setBackgroundResource(R.drawable.rounded_borderes_correct);
            option3.setTextColor(Color.WHITE);
            rightAnswers++;
        } else if (option4.getText().toString().equals(getAnswer)) {
            option4.setBackgroundResource(R.drawable.rounded_borderes_correct);
            option4.setTextColor(Color.WHITE);
            rightAnswers++;
        }
    }

    private class LoadQuestionsTask extends AsyncTask<Void, Void, List<QuestionsList>> {

        @Override
        protected List<QuestionsList> doInBackground(Void... voids) {

            question.setVisibility(View.GONE);
            questions.setVisibility(View.GONE);
            option1.setVisibility(View.GONE);
            option2.setVisibility(View.GONE);
            option3.setVisibility(View.GONE);
            option4.setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);


            DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("tenses").child("quiz").child(getSelectedTopicName);

            final CountDownLatch latch = new CountDownLatch(1);
            firebaseDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    questionsLists.clear();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        QuestionsList questionsList1 = child.getValue(QuestionsList.class);
                        if (questionsList1 != null) {
                            if (questionsLists.size()!=10) {
                                questionsLists.add(questionsList1);
                            }
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
                    // Shuffle the questions and options
                    Collections.shuffle(questionsLists);

                    for (QuestionsList question : questionsLists) {
                        List<String> options = new ArrayList<>();
                        options.add(question.getOption1());
                        options.add(question.getOption2());
                        options.add(question.getOption3());
                        options.add(question.getOption4());

                        // Shuffle the options
                        Collections.shuffle(options);

                        question.setOption1(options.get(0));
                        question.setOption2(options.get(1));
                        question.setOption3(options.get(2));
                        question.setOption4(options.get(3));
                    }

                    question.setVisibility(View.VISIBLE);
                    questions.setVisibility(View.VISIBLE);
                    option1.setVisibility(View.VISIBLE);
                    option2.setVisibility(View.VISIBLE);
                    option3.setVisibility(View.VISIBLE);
                    option4.setVisibility(View.VISIBLE);
                    nextBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    questions.setText((curQuestPos + 1) + "/" + questionsLists.size());
                    question.setText(questionsLists.get(0).getQuestion());
                    option1.setText(questionsLists.get(0).getOption1());
                    option2.setText(questionsLists.get(0).getOption2());
                    option3.setText(questionsLists.get(0).getOption3());
                    option4.setText(questionsLists.get(0).getOption4());
                }
            }


    private void loadQuestions() {
        new LoadQuestionsTask().execute();
    }
}