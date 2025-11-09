package com.example.learn_english;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
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
    private MaterialCardView option1_card, option2_card, option3_card, option4_card;
    private TextView option1, option2, option3, option4;
    private AppCompatButton nextBtn;
    final List<QuestionsList> questionsLists = new ArrayList<>();
    private String getSelectedTopicName;
    private int rightAnswers = 0;

    private int curQuestPos = 0;
    private String selectedOptionByUser = "";
    private Handler autoNextHandler = new Handler();
    private Runnable autoNextRunnable;
    private Handler countdownHandler = new Handler();

    private Animations animations;
    private ImageView waveHeaderA, waveHeaderB;
    private ImageView waveLayer2A, waveLayer2B;
    private ImageView waveLayer3A, waveLayer3B;
    private ImageView bubble1, bubble2, bubble3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_first);

        // First initialize all views
        initializeViews();

        // Then setup animations
        animations = new Animations();
        setupAnimations();

        getSelectedTopicName = getIntent().getStringExtra("selectedTopic");

        if (findViewById(R.id.topicName) != null) {
            TextView selectedTopicName = findViewById(R.id.topicName);
            selectedTopicName.setText(getSelectedTopicName);
        }

        loadQuestions();

        // Set click listeners only if views are found
        if (option1_card != null) {
            option1_card.setOnClickListener(v -> {
                if (selectedOptionByUser.isEmpty()) {
                    selectedOptionByUser = option1.getText().toString();
                    animateOptionSelection(option1_card);
                    revealAnswer();
                    questionsLists.get(curQuestPos).setUserSelected(selectedOptionByUser);
                    startAutoNextTimer();
                }
            });
        }

        if (option2_card != null) {
            option2_card.setOnClickListener(v -> {
                if (selectedOptionByUser.isEmpty()) {
                    selectedOptionByUser = option2.getText().toString();
                    animateOptionSelection(option2_card);
                    revealAnswer();
                    questionsLists.get(curQuestPos).setUserSelected(selectedOptionByUser);
                    startAutoNextTimer();
                }
            });
        }

        if (option3_card != null) {
            option3_card.setOnClickListener(v -> {
                if (selectedOptionByUser.isEmpty()) {
                    selectedOptionByUser = option3.getText().toString();
                    animateOptionSelection(option3_card);
                    revealAnswer();
                    questionsLists.get(curQuestPos).setUserSelected(selectedOptionByUser);
                    startAutoNextTimer();
                }
            });
        }

        if (option4_card != null) {
            option4_card.setOnClickListener(v -> {
                if (selectedOptionByUser.isEmpty()) {
                    selectedOptionByUser = option4.getText().toString();
                    animateOptionSelection(option4_card);
                    revealAnswer();
                    questionsLists.get(curQuestPos).setUserSelected(selectedOptionByUser);
                    startAutoNextTimer();
                }
            });
        }

        if (nextBtn != null) {
            nextBtn.setOnClickListener(v -> {
                if (selectedOptionByUser.isEmpty()) {
                    Toast.makeText(QuizActivityTenses.this, "Please select an option", Toast.LENGTH_SHORT).show();
                } else {
                    cancelAutoNextTimer();
                    animateNextButton();
                }
            });
        }
    }

    private void startAutoNextTimer() {
        // Cancel any existing timer
        cancelAutoNextTimer();

        // Create new timer
        autoNextRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && !isDestroyed()) {
                    changeNextQuestion();
                }
            }
        };

        // Start 5-second timer
        autoNextHandler.postDelayed(autoNextRunnable, 5000);

        // Show countdown animation on next button
        if (nextBtn != null) {
            nextBtn.setText("Հաջորդը (5)");
            startCountdownAnimation();
        }
    }

    private void startCountdownAnimation() {
        final int[] countdown = {5};
        Runnable countdownRunnable = new Runnable() {
            @Override
            public void run() {
                if (countdown[0] > 1 && nextBtn != null && !isFinishing()) {
                    countdown[0]--;
                    nextBtn.setText("Հաջորդը (" + countdown[0] + ")");
                    countdownHandler.postDelayed(this, 1000);
                }
            }
        };
        countdownHandler.postDelayed(countdownRunnable, 1000);
    }

    private void cancelAutoNextTimer() {
        if (autoNextHandler != null && autoNextRunnable != null) {
            autoNextHandler.removeCallbacks(autoNextRunnable);
        }
        if (countdownHandler != null) {
            countdownHandler.removeCallbacksAndMessages(null);
        }
        if (nextBtn != null && !isFinishing()) {
            nextBtn.setText(curQuestPos == (questionsLists.size() - 1) ? "Ավարտ" : "Հաջորդը");
        }
    }

    private void initializeViews() {
        // Initialize main quiz views
        questions = findViewById(R.id.questions);
        question = findViewById(R.id.question);

        // Initialize option cards
        option1_card = findViewById(R.id.option1_card);
        option2_card = findViewById(R.id.option2_card);
        option3_card = findViewById(R.id.option3_card);
        option4_card = findViewById(R.id.option4_card);

        // Initialize option text views
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        // Initialize other views
        nextBtn = findViewById(R.id.nextBtn);
        progressBar = findViewById(R.id.progressBar2);

        // Initialize animation views (check if they exist in layout)
        waveHeaderA = findViewById(R.id.waveHeaderA);
        waveHeaderB = findViewById(R.id.waveHeaderB);
        waveLayer2A = findViewById(R.id.waveLayer2A);
        waveLayer2B = findViewById(R.id.waveLayer2B);
        waveLayer3A = findViewById(R.id.waveLayer3A);
        waveLayer3B = findViewById(R.id.waveLayer3B);

        bubble1 = findViewById(R.id.bubble1);
        bubble2 = findViewById(R.id.bubble2);
        bubble3 = findViewById(R.id.bubble3);

        // Log missing views for debugging
        if (questions == null) Log.e("QuizActivity", "questions view not found");
        if (question == null) Log.e("QuizActivity", "question view not found");
        if (option1_card == null) Log.e("QuizActivity", "option1_card view not found");
        if (option2_card == null) Log.e("QuizActivity", "option2_card view not found");
        if (option3_card == null) Log.e("QuizActivity", "option3_card view not found");
        if (option4_card == null) Log.e("QuizActivity", "option4_card view not found");
        if (option1 == null) Log.e("QuizActivity", "option1 view not found");
        if (option2 == null) Log.e("QuizActivity", "option2 view not found");
        if (option3 == null) Log.e("QuizActivity", "option3 view not found");
        if (option4 == null) Log.e("QuizActivity", "option4 view not found");
        if (nextBtn == null) Log.e("QuizActivity", "nextBtn view not found");
        if (progressBar == null) Log.e("QuizActivity", "progressBar view not found");
    }

    private void setupAnimations() {
        View root = findViewById(android.R.id.content);

        animations.waitForLayout(root, () -> {
            if (waveHeaderA != null && waveHeaderB != null) {
                animations.startWaveAnimations(waveHeaderA, waveHeaderB,
                        waveLayer2A, waveLayer2B, waveLayer3A, waveLayer3B);
            }

            if (bubble1 != null && bubble2 != null && bubble3 != null) {
                animations.startBubbleAnimations(root, bubble1, bubble2, bubble3);
            }
        });
    }

    private void animateOptionSelection(MaterialCardView optionCard) {
        if (optionCard != null) {
            optionCard.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .setInterpolator(new DecelerateInterpolator())
                    .withEndAction(() -> optionCard.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .setInterpolator(new OvershootInterpolator())
                            .start())
                    .start();
        }
    }

    private void animateNextButton() {
        if (nextBtn != null) {
            nextBtn.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .setInterpolator(new DecelerateInterpolator())
                    .withEndAction(() -> {
                        nextBtn.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .setInterpolator(new OvershootInterpolator())
                                .start();
                        changeNextQuestion();
                    })
                    .start();
        }
    }

    private void animateQuestionChange() {
        if (question == null) return;

        // Cancel auto-next timer when changing questions
        cancelAutoNextTimer();

        // Fade out current question
        question.animate()
                .alpha(0f)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .withEndAction(() -> {
                    // Update question content
                    if (questions != null) {
                        questions.setText((curQuestPos + 1) + "/" + questionsLists.size());
                    }
                    if (question != null) {
                        question.setText(questionsLists.get(curQuestPos).getQuestion());
                    }
                    if (option1 != null) option1.setText(questionsLists.get(curQuestPos).getOption1());
                    if (option2 != null) option2.setText(questionsLists.get(curQuestPos).getOption2());
                    if (option3 != null) option3.setText(questionsLists.get(curQuestPos).getOption3());
                    if (option4 != null) option4.setText(questionsLists.get(curQuestPos).getOption4());

                    // Fade in new question
                    question.animate()
                            .alpha(1f)
                            .setDuration(300)
                            .setInterpolator(new DecelerateInterpolator())
                            .start();

                    // Animate options sliding in
                    animateOptionsEntry();
                })
                .start();
    }

    private void animateOptionsEntry() {
        if (option1_card == null || option2_card == null || option3_card == null || option4_card == null) return;

        option1_card.setTranslationX(-50f);
        option2_card.setTranslationX(50f);
        option3_card.setTranslationX(-50f);
        option4_card.setTranslationX(50f);

        option1_card.setAlpha(0f);
        option2_card.setAlpha(0f);
        option3_card.setAlpha(0f);
        option4_card.setAlpha(0f);

        new Handler().postDelayed(() -> {
            if (option1_card != null) option1_card.animate().translationX(0f).alpha(1f).setDuration(300).setInterpolator(new OvershootInterpolator()).start();
        }, 100);

        new Handler().postDelayed(() -> {
            if (option2_card != null) option2_card.animate().translationX(0f).alpha(1f).setDuration(300).setInterpolator(new OvershootInterpolator()).start();
        }, 200);

        new Handler().postDelayed(() -> {
            if (option3_card != null) option3_card.animate().translationX(0f).alpha(1f).setDuration(300).setInterpolator(new OvershootInterpolator()).start();
        }, 300);

        new Handler().postDelayed(() -> {
            if (option4_card != null) option4_card.animate().translationX(0f).alpha(1f).setDuration(300).setInterpolator(new OvershootInterpolator()).start();
        }, 400);
    }

    private boolean flag = false;

    private void changeNextQuestion() {
        curQuestPos++;

        if (curQuestPos == (questionsLists.size() - 1)) {
            if (nextBtn != null) nextBtn.setText("Finish");
        } else if (curQuestPos == questionsLists.size()) {
            flag = true;
        }

        if (flag) {
            ForRes();
            return;
        }

        if (curQuestPos < questionsLists.size()) {
            selectedOptionByUser = "";

            // Reset options with animation
            resetOptionsWithAnimation();

            // Animate question change
            animateQuestionChange();
        } else {
            ForRes();
        }
    }

    private void resetOptionsWithAnimation() {
        if (option1_card != null) {
            option1_card.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(200)
                    .start();
        }
        if (option2_card != null) {
            option2_card.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(200)
                    .start();
        }
        if (option3_card != null) {
            option3_card.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(200)
                    .start();
        }
        if (option4_card != null) {
            option4_card.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(200)
                    .start();
        }

        // Reset background and text color
        resetOptionAppearance();
    }

    private void resetOptionAppearance() {
        // Stop all continuous animations
        if (option1_card != null && option1 != null) {
            option1_card.animate().cancel();
            option1_card.setScaleX(1f);
            option1_card.setScaleY(1f);
            option1_card.setRotation(0f);
            option1_card.setTranslationX(0f);
            option1_card.setCardBackgroundColor(Color.WHITE);
            option1_card.setStrokeColor(Color.parseColor("#ecc9ee"));
            option1_card.setCardElevation(2f);
            option1.setTextColor(Color.parseColor("#764ba2"));
        }
        if (option2_card != null && option2 != null) {
            option2_card.animate().cancel();
            option2_card.setScaleX(1f);
            option2_card.setScaleY(1f);
            option2_card.setRotation(0f);
            option2_card.setTranslationX(0f);
            option2_card.setCardBackgroundColor(Color.WHITE);
            option2_card.setStrokeColor(Color.parseColor("#ecc9ee"));
            option2_card.setCardElevation(2f);
            option2.setTextColor(Color.parseColor("#764ba2"));
        }
        if (option3_card != null && option3 != null) {
            option3_card.animate().cancel();
            option3_card.setScaleX(1f);
            option3_card.setScaleY(1f);
            option3_card.setRotation(0f);
            option3_card.setTranslationX(0f);
            option3_card.setCardBackgroundColor(Color.WHITE);
            option3_card.setStrokeColor(Color.parseColor("#ecc9ee"));
            option3_card.setCardElevation(2f);
            option3.setTextColor(Color.parseColor("#764ba2"));
        }
        if (option4_card != null && option4 != null) {
            option4_card.animate().cancel();
            option4_card.setScaleX(1f);
            option4_card.setScaleY(1f);
            option4_card.setRotation(0f);
            option4_card.setTranslationX(0f);
            option4_card.setCardBackgroundColor(Color.WHITE);
            option4_card.setStrokeColor(Color.parseColor("#ecc9ee"));
            option4_card.setCardElevation(2f);
            option4.setTextColor(Color.parseColor("#764ba2"));
        }

        // Clear the selected option to stop continuous animations
        selectedOptionByUser = "";
    }
    private void ForRes() {
        // Cancel any running timers
        cancelAutoNextTimer();

        // Animate exit
        View root = findViewById(android.R.id.content);
        if (root != null) {
            root.animate()
                    .alpha(0f)
                    .setDuration(500)
                    .withEndAction(() -> {
                        Intent intent = new Intent(QuizActivityTenses.this, QuizResults.class);
                        intent.putExtra("correct", getCorrectAnswers());
                        intent.putExtra("incorrect", getInCorrectAnswers());
                        intent.putExtra("selectedTopicName", getSelectedTopicName);
                        intent.putExtra("size", questionsLists.size());
                        startActivity(intent);
                        finish();
                    })
                    .start();
        } else {
            // Fallback if root is null
            Intent intent = new Intent(QuizActivityTenses.this, QuizResults.class);
            intent.putExtra("correct", getCorrectAnswers());
            intent.putExtra("incorrect", getInCorrectAnswers());
            intent.putExtra("selectedTopicName", getSelectedTopicName);
            intent.putExtra("size", questionsLists.size());
            startActivity(intent);
            finish();
        }
    }

    private int getCorrectAnswers() {
        int correctAnswers = 0;
        for (int i = 0; i < questionsLists.size(); i++) {
            final String getUserSelectedAnswer = questionsLists.get(i).getUserSelected();
            final String getAnswer = questionsLists.get(i).getAnswer();
            if (getUserSelectedAnswer != null && getUserSelectedAnswer.equals(getAnswer)) {
                correctAnswers++;
            }
        }
        return correctAnswers;
    }

    private int getInCorrectAnswers() {
        int incorrectAnswers = 0;
        for (int i = 0; i < questionsLists.size(); i++) {
            final String getUserSelectedAnswer = questionsLists.get(i).getUserSelected();
            final String getAnswer = questionsLists.get(i).getAnswer();
            if (getUserSelectedAnswer == null || !getUserSelectedAnswer.equals(getAnswer)) {
                incorrectAnswers++;
            }
        }
        return incorrectAnswers;
    }

    private void revealAnswer() {
        if (curQuestPos >= questionsLists.size()) return;

        final String getAnswer = questionsLists.get(curQuestPos).getAnswer();

        // First, mark the correct answer in green
        if (option1 != null && option1.getText().toString().equals(getAnswer)) {
            animateCorrectAnswer(option1_card);
        } else if (option2 != null && option2.getText().toString().equals(getAnswer)) {
            animateCorrectAnswer(option2_card);
        } else if (option3 != null && option3.getText().toString().equals(getAnswer)) {
            animateCorrectAnswer(option3_card);
        } else if (option4 != null && option4.getText().toString().equals(getAnswer)) {
            animateCorrectAnswer(option4_card);
        }

        // Then, mark the user's selected answer as wrong if it's incorrect
        if (selectedOptionByUser != null && !selectedOptionByUser.equals(getAnswer)) {
            if (option1 != null && option1.getText().toString().equals(selectedOptionByUser)) {
                animateWrongAnswerBeautifully(option1_card);
            } else if (option2 != null && option2.getText().toString().equals(selectedOptionByUser)) {
                animateWrongAnswerBeautifully(option2_card);
            } else if (option3 != null && option3.getText().toString().equals(selectedOptionByUser)) {
                animateWrongAnswerBeautifully(option3_card);
            } else if (option4 != null && option4.getText().toString().equals(selectedOptionByUser)) {
                animateWrongAnswerBeautifully(option4_card);
            }
        }

        // Count correct answers
        if (selectedOptionByUser != null && selectedOptionByUser.equals(getAnswer)) {
            rightAnswers++;
        }
    }

    private void animateCorrectAnswer(MaterialCardView optionCard) {
        if (optionCard == null) return;

        // Reset any ongoing animations
        optionCard.animate().cancel();

        // Beautiful glow and scale animation for correct answer
        optionCard.animate()
                .scaleX(1.08f)
                .scaleY(1.08f)
                .setDuration(400)
                .setInterpolator(new OvershootInterpolator(1.2f))
                .withStartAction(() -> {
                    // Start with a bright green glow
                    optionCard.setCardBackgroundColor(Color.parseColor("#66BB6A")); // Light green
                    updateOptionTextColor(optionCard, Color.WHITE);
                })
                .withEndAction(() -> {
                    // First bounce back
                    optionCard.animate()
                            .scaleX(1.05f)
                            .scaleY(1.05f)
                            .setDuration(200)
                            .setInterpolator(new DecelerateInterpolator())
                            .withEndAction(() -> {
                                // Continuous gentle pulse
                                startContinuousPulse(optionCard, true);
                            })
                            .start();

                    // Final color transition to solid green
                    optionCard.setCardBackgroundColor(Color.parseColor("#4CAF50"));
                })
                .start();

        // Sparkle effect with elevation
        optionCard.setCardElevation(20f);
        new Handler().postDelayed(() -> {
            optionCard.setCardElevation(12f);
            // Add a subtle rotation for dynamic effect
            optionCard.animate()
                    .rotation(1f)
                    .setDuration(100)
                    .withEndAction(() -> optionCard.animate()
                            .rotation(-1f)
                            .setDuration(100)
                            .withEndAction(() -> optionCard.animate()
                                    .rotation(0f)
                                    .setDuration(50)
                                    .start())
                            .start())
                    .start();
        }, 300);
    }

    private void animateWrongAnswerBeautifully(MaterialCardView optionCard) {
        if (optionCard == null) return;

        // Reset any ongoing animations
        optionCard.animate().cancel();

        // Dramatic shake and color flash for wrong answer
        optionCard.animate()
                .scaleX(0.92f)
                .scaleY(0.92f)
                .setDuration(300)
                .setInterpolator(new DecelerateInterpolator())
                .withStartAction(() -> {
                    // Flash bright red
                    optionCard.setCardBackgroundColor(Color.parseColor("#FF7043")); // Bright orange-red
                    updateOptionTextColor(optionCard, Color.WHITE);
                })
                .withEndAction(() -> {
                    // Bounce back slightly
                    optionCard.animate()
                            .scaleX(0.96f)
                            .scaleY(0.96f)
                            .setDuration(200)
                            .setInterpolator(new OvershootInterpolator(0.8f))
                            .start();

                    // Final color to dark red
                    optionCard.setCardBackgroundColor(Color.parseColor("#F44336"));
                })
                .start();

        // Enhanced shake animation with more dynamics
        new Handler().postDelayed(() -> {
            optionCard.animate()
                    .translationX(-25f)
                    .rotation(-3f)
                    .setDuration(80)
                    .withEndAction(() -> {
                        optionCard.animate()
                                .translationX(25f)
                                .rotation(3f)
                                .setDuration(80)
                                .withEndAction(() -> {
                                    optionCard.animate()
                                            .translationX(-15f)
                                            .rotation(-2f)
                                            .setDuration(60)
                                            .withEndAction(() -> {
                                                optionCard.animate()
                                                        .translationX(15f)
                                                        .rotation(2f)
                                                        .setDuration(60)
                                                        .withEndAction(() -> {
                                                            optionCard.animate()
                                                                    .translationX(0f)
                                                                    .rotation(0f)
                                                                    .setDuration(40)
                                                                    .start();
                                                        })
                                                        .start();
                                            })
                                            .start();
                                })
                                .start();
                    })
                    .start();
        }, 150);

        // Vibration effect with elevation
        optionCard.setCardElevation(8f);
        new Handler().postDelayed(() -> {
            startContinuousVibration(optionCard);
        }, 400);
    }

    private void startContinuousPulse(MaterialCardView cardView, boolean isCorrect) {
        if (cardView == null) return;

        cardView.animate()
                .scaleX(1.03f)
                .scaleY(1.03f)
                .setDuration(800)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(() -> {
                    cardView.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(800)
                            .setInterpolator(new AccelerateInterpolator())
                            .withEndAction(() -> {
                                // Continue pulsing until next question
                                if (selectedOptionByUser != null && !selectedOptionByUser.isEmpty()) {
                                    startContinuousPulse(cardView, isCorrect);
                                }
                            })
                            .start();
                })
                .start();
    }

    private void startContinuousVibration(MaterialCardView cardView) {
        if (cardView == null) return;

        final int[] vibrationCount = {0};
        final int maxVibrations = 3;

        Runnable vibrationRunnable = new Runnable() {
            @Override
            public void run() {
                if (vibrationCount[0] < maxVibrations && cardView != null && !isFinishing()) {
                    cardView.animate()
                            .translationX(3f)
                            .setDuration(40)
                            .withEndAction(() -> cardView.animate()
                                    .translationX(-3f)
                                    .setDuration(40)
                                    .withEndAction(() -> {
                                        cardView.animate()
                                                .translationX(0f)
                                                .setDuration(20)
                                                .start();
                                        vibrationCount[0]++;
                                        if (vibrationCount[0] < maxVibrations) {
                                            new Handler().postDelayed(this, 200);
                                        }
                                    })
                                    .start())
                            .start();
                }
            }
        };

        new Handler().postDelayed(vibrationRunnable, 100);
    }

    private void updateOptionTextColor(MaterialCardView optionCard, int color) {
        if (optionCard == option1_card && option1 != null) {
            option1.setTextColor(color);
        } else if (optionCard == option2_card && option2 != null) {
            option2.setTextColor(color);
        } else if (optionCard == option3_card && option3 != null) {
            option3.setTextColor(color);
        } else if (optionCard == option4_card && option4 != null) {
            option4.setTextColor(color);
        }
    }

    private class LoadQuestionsTask extends AsyncTask<Void, Void, List<QuestionsList>> {

        @Override
        protected List<QuestionsList> doInBackground(Void... voids) {
            setQuizContentVisibility(View.GONE);

            DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("tenses").child("quiz").child(getSelectedTopicName);

            final CountDownLatch latch = new CountDownLatch(1);
            firebaseDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    questionsLists.clear();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        QuestionsList questionsList1 = child.getValue(QuestionsList.class);
                        if (questionsList1 != null) {
                            if (questionsLists.size() != 10) {
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
            if (questionsLists.isEmpty()) {
                Toast.makeText(QuizActivityTenses.this, "No questions found", Toast.LENGTH_SHORT).show();
                return;
            }

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

            // Animate the content in
            animateContentEntry();
        }
    }

    private void setQuizContentVisibility(int visibility) {
        if (question != null) question.setVisibility(visibility);
        if (questions != null) questions.setVisibility(visibility);
        if (option1_card != null) option1_card.setVisibility(visibility);
        if (option2_card != null) option2_card.setVisibility(visibility);
        if (option3_card != null) option3_card.setVisibility(visibility);
        if (option4_card != null) option4_card.setVisibility(visibility);
        if (nextBtn != null) nextBtn.setVisibility(visibility);
        if (progressBar != null) progressBar.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    private void animateContentEntry() {
        // Set initial alpha to 0
        setQuizContentAlpha(0f);

        // Make content visible
        setQuizContentVisibility(View.VISIBLE);

        if (progressBar != null) progressBar.setVisibility(View.GONE);

        // Set initial content
        if (questions != null) questions.setText((curQuestPos + 1) + "/" + questionsLists.size());
        if (question != null) question.setText(questionsLists.get(0).getQuestion());
        if (option1 != null) option1.setText(questionsLists.get(0).getOption1());
        if (option2 != null) option2.setText(questionsLists.get(0).getOption2());
        if (option3 != null) option3.setText(questionsLists.get(0).getOption3());
        if (option4 != null) option4.setText(questionsLists.get(0).getOption4());

        // Staggered fade-in animation
        new Handler().postDelayed(() -> { if (question != null) question.animate().alpha(1f).setDuration(400).start(); }, 100);
        new Handler().postDelayed(() -> { if (questions != null) questions.animate().alpha(1f).setDuration(400).start(); }, 200);
        new Handler().postDelayed(() -> { if (option1_card != null) option1_card.animate().alpha(1f).setDuration(400).start(); }, 300);
        new Handler().postDelayed(() -> { if (option2_card != null) option2_card.animate().alpha(1f).setDuration(400).start(); }, 400);
        new Handler().postDelayed(() -> { if (option3_card != null) option3_card.animate().alpha(1f).setDuration(400).start(); }, 500);
        new Handler().postDelayed(() -> { if (option4_card != null) option4_card.animate().alpha(1f).setDuration(400).start(); }, 600);
        new Handler().postDelayed(() -> { if (nextBtn != null) nextBtn.animate().alpha(1f).setDuration(400).start(); }, 700);
    }

    private void setQuizContentAlpha(float alpha) {
        if (question != null) question.setAlpha(alpha);
        if (questions != null) questions.setAlpha(alpha);
        if (option1_card != null) option1_card.setAlpha(alpha);
        if (option2_card != null) option2_card.setAlpha(alpha);
        if (option3_card != null) option3_card.setAlpha(alpha);
        if (option4_card != null) option4_card.setAlpha(alpha);
        if (nextBtn != null) nextBtn.setAlpha(alpha);
    }

    private void loadQuestions() {
        new LoadQuestionsTask().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelAutoNextTimer();
        if (animations != null) animations.pauseAnimations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animations != null && waveHeaderA != null && waveHeaderB != null) {
            animations.startWaveAnimations(waveHeaderA, waveHeaderB,
                    waveLayer2A, waveLayer2B, waveLayer3A, waveLayer3B);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up handlers to prevent memory leaks
        cancelAutoNextTimer();
        if (autoNextHandler != null) {
            autoNextHandler.removeCallbacksAndMessages(null);
        }
        if (countdownHandler != null) {
            countdownHandler.removeCallbacksAndMessages(null);
        }
        if (animations != null) animations.cleanupAnimations();
    }
}