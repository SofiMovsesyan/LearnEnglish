package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class QuizActivityTenses extends AppCompatActivity {
    private TextView questions, question;
    private AppCompatButton option1, option2, option3, option4, nextBtn;
    private List<QuestionsList> questionsLists;

    private int curQuestPos = 0;
    private String selectedOptionByUser = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_tenses);
            final TextView selectedTopicName = findViewById(R.id.topicName);
            final String getSelectedTopicName = getIntent().getStringExtra("selectedTopic");
            questions = findViewById(R.id.questions);
            question = findViewById(R.id.question);
            option1 = findViewById(R.id.option1);
            option2 = findViewById(R.id.option2);
            option3 = findViewById(R.id.option3);
            option4 = findViewById(R.id.option4);
            nextBtn = findViewById(R.id.nextBtn);
            selectedTopicName.setText(getSelectedTopicName);
            questionsLists = QuestionsBankTenses.getQuestions(getSelectedTopicName);
            questions.setText((curQuestPos + 1) + "/" + questionsLists.size());
            question.setText(questionsLists.get(0).getQuestion());
            option1.setText(questionsLists.get(0).getOption1());
            option2.setText(questionsLists.get(0).getOption2());
            option3.setText(questionsLists.get(0).getOption3());
            option4.setText(questionsLists.get(0).getOption4());

            option1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedOptionByUser.isEmpty()) {
                        selectedOptionByUser = option1.getText().toString();
                        option1.setBackgroundResource(R.drawable.rounded_borderes_wrong);
                        option1.setTextColor(Color.WHITE);
                        revealAnswer();
                        questionsLists.get(curQuestPos).setUserSelectedAnswer(selectedOptionByUser);
                    }
                }
            });

            option2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedOptionByUser.isEmpty()) {
                        selectedOptionByUser = option2.getText().toString();
                        option2.setBackgroundResource(R.drawable.rounded_borderes_wrong);
                        option2.setTextColor(Color.WHITE);
                        revealAnswer();
                        questionsLists.get(curQuestPos).setUserSelectedAnswer(selectedOptionByUser);
                    }
                }
            });
            option3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedOptionByUser.isEmpty()) {
                        selectedOptionByUser = option3.getText().toString();
                        option3.setBackgroundResource(R.drawable.rounded_borderes_wrong);
                        option3.setTextColor(Color.WHITE);
                        revealAnswer();
                        questionsLists.get(curQuestPos).setUserSelectedAnswer(selectedOptionByUser);
                    }
                }
            });

            option4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedOptionByUser.isEmpty()) {
                        selectedOptionByUser = option4.getText().toString();
                        option4.setBackgroundResource(R.drawable.rounded_borderes_wrong);
                        option4.setTextColor(Color.WHITE);
                        revealAnswer();
                        questionsLists.get(curQuestPos).setUserSelectedAnswer(selectedOptionByUser);
                    }
                }
            });

            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedOptionByUser.isEmpty()) {
                        Toast.makeText(QuizActivityTenses.this, "Pls select an option", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        changeNextQuestion();
                    }
                }
            });

        }

        private boolean flag = false;
        private void changeNextQuestion() {
            curQuestPos++;

            if (curQuestPos == (questionsLists.size()-1)) {
                nextBtn.setText("Finish");
            }
            else if (curQuestPos == questionsLists.size()) {
                flag = true;
//                ForRes();
            }
            if (flag==true) {

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

                questions.setText((curQuestPos+1)+"/"+questionsLists.size());
                question.setText(questionsLists.get(curQuestPos).getQuestion());
                option1.setText(questionsLists.get(curQuestPos).getOption1());
                option2.setText(questionsLists.get(curQuestPos).getOption2());
                option3.setText(questionsLists.get(curQuestPos).getOption3());
                option4.setText(questionsLists.get(curQuestPos).getOption4());
//                if (flag) {
//                    ForRes();
//                }
//                curQuestPos++;
            }
            else {
                ForRes();
            }
        }
        private void ForRes() {
            Intent intent = new Intent(QuizActivityTenses.this, QuizResults.class);
            intent.putExtra("correct", getCorrectAnswers());
            intent.putExtra("incorrect", getInCorrectAnswers());
            startActivity(intent);
            finish();


        }

        private int getCorrectAnswers() {
            int correctAnswers = 0;
            for (int i = 0; i < questionsLists.size(); i++) {
                final String getUserSelectedAnswer = questionsLists.get(i).getUserSelectedAnswer();
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
                final String getUserSelectedAnswer = questionsLists.get(i).getUserSelectedAnswer();
                final String getAnswer = questionsLists.get(i).getAnswer();
                if (!getUserSelectedAnswer.equals(getAnswer)) {
                    correctAnswers++;
                }
            }
            return correctAnswers;
        }



        private void  revealAnswer() {
            final String getAnswer = questionsLists.get(curQuestPos).getAnswer();
            if (option1.getText().toString().equals(getAnswer)) {
                option1.setBackgroundResource(R.drawable.rounded_borderes_correct);
                option1.setTextColor(Color.WHITE);
            }
            else if (option2.getText().toString().equals(getAnswer)) {
                option2.setBackgroundResource(R.drawable.rounded_borderes_correct);
                option2.setTextColor(Color.WHITE);
            }
            else if (option3.getText().toString().equals(getAnswer)) {
                option3.setBackgroundResource(R.drawable.rounded_borderes_correct);
                option3.setTextColor(Color.WHITE);
            }
            else if (option4.getText().toString().equals(getAnswer)) {
                option4.setBackgroundResource(R.drawable.rounded_borderes_correct);
                option4.setTextColor(Color.WHITE);
            }
        }


    }
//}