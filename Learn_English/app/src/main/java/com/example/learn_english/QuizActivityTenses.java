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
                        option1.setBackgroundColor(Color.RED);
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
                        option2.setBackgroundColor(Color.RED);
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
                        option3.setBackgroundColor(Color.RED);
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
                        option4.setBackgroundColor(Color.RED);
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

        private void changeNextQuestion() {
            curQuestPos++;
            if ((curQuestPos+1) == questionsLists.size()) {
                nextBtn.setText("Finish");
//                ForRes();
            }
            if (curQuestPos < questionsLists.size()) {
                selectedOptionByUser = "";
                option1.setBackgroundColor(Color.GRAY);
                option1.setTextColor(Color.parseColor("#1F6BB8"));

                option2.setBackgroundColor(Color.GRAY);
                option2.setTextColor(Color.parseColor("#1F6BB8"));

                option3.setBackgroundColor(Color.GRAY);
                option3.setTextColor(Color.parseColor("#1F6BB8"));

                option4.setBackgroundColor(Color.GRAY);
                option4.setTextColor(Color.parseColor("#1F6BB8"));

                questions.setText((curQuestPos+1)+"/"+questionsLists.size());
                question.setText(questionsLists.get(curQuestPos).getQuestion());
                option1.setText(questionsLists.get(curQuestPos).getOption1());
                option2.setText(questionsLists.get(curQuestPos).getOption2());
                option3.setText(questionsLists.get(curQuestPos).getOption3());
                option4.setText(questionsLists.get(curQuestPos).getOption4());
            }
            else {
//                Intent intent = new Intent(QuizActivityTenses.this, QuizResults.class);
//                intent.putExtra("correct", getCorrectAnswers());
//                intent.putExtra("incorrect", getInCorrectAnswers());
//                startActivity(intent);
//                finish();
            }
        }
//        private void ForRes() {
//            Intent intent = new Intent(QuizActivityTenses.this, QuizResults.class);
//            intent.putExtra("correct", getCorrectAnswers());
//            intent.putExtra("incorrect", getInCorrectAnswers());
//            startActivity(intent);
//            finish();
//
//
//        }

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
                option1.setBackgroundColor(Color.GREEN);
                option1.setTextColor(Color.WHITE);
            }
            else if (option2.getText().toString().equals(getAnswer)) {
                option2.setBackgroundColor(Color.GREEN);
                option2.setTextColor(Color.WHITE);
            }
            else if (option3.getText().toString().equals(getAnswer)) {
                option3.setBackgroundColor(Color.GREEN);
                option3.setTextColor(Color.WHITE);
            }
            else if (option4.getText().toString().equals(getAnswer)) {
                option4.setBackgroundColor(Color.GREEN);
                option4.setTextColor(Color.WHITE);
            }
        }


    }
}