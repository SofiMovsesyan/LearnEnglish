package com.example.learn_english;

import java.util.ArrayList;
import java.util.List;

public class QuestionsBankTenses {
        private static List<QuestionsList> aQuestions() {
            final List<QuestionsList> questionsList = new ArrayList<>();

            final QuestionsList question1 = new QuestionsList("ABC",
                    "D", "E",
                    "F", "G",
                    "D", "");
            final QuestionsList question2 = new QuestionsList("123",
                    "4", "5", "6", "7", "4",
                    "");

            questionsList.add(question1);
            questionsList.add(question2);

            return questionsList;
        }


        private static List<QuestionsList> bQuestions() {
            final List<QuestionsList> questionsList = new ArrayList<>();

            final QuestionsList question1 = new QuestionsList("ABC",
                    "D", "E",
                    "F", "G",
                    "D", "");
            final QuestionsList question2 = new QuestionsList("123",
                    "4", "5", "6", "7", "4",
                    "");

            questionsList.add(question1);
            questionsList.add(question2);

            return questionsList;
        }


        public static List<QuestionsList> getQuestions(String selectedTopicName) {
            switch (selectedTopicName) {
                case "a":
                    return aQuestions();
                default:
                    return bQuestions();



            }
        }

    }


