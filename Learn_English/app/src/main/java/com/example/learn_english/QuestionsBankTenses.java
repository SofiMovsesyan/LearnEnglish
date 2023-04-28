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

            final QuestionsList question1 = new QuestionsList("I … (to arrive) in London at last.",
                    "arrive", "arrived",
                    "have arrived\n", "am arriving",
                    "arrived", "");
            final QuestionsList question2 = new QuestionsList("In England, each man ... different language.",
                    "speaks", "is speaking", "is going to speak", "speak", "speaks",
                    "");
            final QuestionsList question3 = new QuestionsList("a",
                    "b", "c",
                    "have arrived\n", "am arriving",
                    "b", "");

            questionsList.add(question1);
            questionsList.add(question2);

            questionsList.add(question3);
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


