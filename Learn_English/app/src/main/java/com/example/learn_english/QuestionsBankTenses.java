package com.example.learn_english;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionsBankTenses {
    private static List<QuestionsList> aQuestions() {
        final List<QuestionsList> questionsList = new ArrayList<>();

            /*final QuestionsList question1 = new QuestionsList("Please be quiet. The children ...(sleep)",
                    "sleeping", "are sleeping",
                    "have slept", "sleep",
                    "are sleeping");
            final QuestionsList question2 = new QuestionsList("Michael is at university. He ...(study) history.",
                    "is studying", "studied", "have studied", "are studying", "is studying");

            questionsList.add(question1);
            questionsList.add(question2);*/

        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference()
                .child("Tenses").child("A").child("Quiz").child("1");

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    QuestionsList questionsList1 = child.getValue(QuestionsList.class);
                    if (questionsList1 != null) {
                        questionsList.add(questionsList1);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                questionsList.add(new QuestionsList("q", "1", "2", "3", "4", "1"));
            }
        });


            return questionsList;
    }



        private static List<QuestionsList> bQuestions() {
            final List<QuestionsList> questionsList = new ArrayList<>();

            final QuestionsList question1 = new QuestionsList("I … (to arrive) in London at last.",
                    "arrive", "arrived",
                    "have arrived\n", "am arriving",
                    "arrived");
            final QuestionsList question2 = new QuestionsList("In England, each man ... different language.",
                    "speaks", "is speaking", "is going to speak", "speak", "speaks");
            final QuestionsList question3 = new QuestionsList("a",
                    "b", "c",
                    "have arrived\n", "am arriving",
                    "b");

            questionsList.add(question1);
            questionsList.add(question2);
            Log.i("TAG","B");

            questionsList.add(question3);
            return questionsList;
        }


        public static List<QuestionsList> getQuestions(String selectedTopicName) {
            switch (selectedTopicName) {
                case "Present Continuous":
                    return aQuestions();
                default:
                    return bQuestions();
            }
        }

    }


