package com.example.learn_english;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    private ImageView imageView;

    private int curQuestPos = 0;
    private String selectedOptionByUser = "";

    private Handler autoNextHandler = new Handler();
    private Runnable autoNextRunnable;

    private Animations animations;
    private ImageView waveHeaderA, waveHeaderB;
    private ImageView waveLayer2A, waveLayer2B;
    private ImageView waveLayer3A, waveLayer3B;
    private ImageView bubble1, bubble2, bubble3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_second);

        TextView selectedTopicName = findViewById(R.id.topicName);
        getSelectedTopicName = getIntent().getStringExtra("selectedTopic");

        questions = findViewById(R.id.questions);
        question = findViewById(R.id.question);
        word = findViewById(R.id.word);
        nextBtn = findViewById(R.id.nextBtn);
        progressBar = findViewById(R.id.progressBar2);
        check = findViewById(R.id.check);
        imageView = findViewById(R.id.imageView);

        selectedTopicName.setText(getSelectedTopicName);

        waveHeaderA = findViewById(R.id.waveHeaderA);
        waveHeaderB = findViewById(R.id.waveHeaderB);
        waveLayer2A = findViewById(R.id.waveLayer2A);
        waveLayer2B = findViewById(R.id.waveLayer2B);
        bubble1 = findViewById(R.id.bubble1);
        bubble2 = findViewById(R.id.bubble2);
        bubble3 = findViewById(R.id.bubble3);

        animations = new Animations();
        setupAnimations();

        loadQuestions();

        check.setOnClickListener(v -> {
            selectedOptionByUser = word.getText().toString().toLowerCase();
            revealAnswer();
            questionsLists.get(curQuestPos).setUserSelected(selectedOptionByUser);
        });

        nextBtn.setOnClickListener(v -> {
            if (selectedOptionByUser.isEmpty()) {
                Toast.makeText(QuizActivityWords.this, "Please type your answer", Toast.LENGTH_SHORT).show();
            } else {
                autoNextHandler.removeCallbacks(autoNextRunnable); // cancel pending auto-next
                animateQuestionChange();
            }
        });
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

    private void animateQuestionChange() {
        Animation fadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        fadeOut.setDuration(300);
        question.startAnimation(fadeOut);
        imageView.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) { }
            @Override public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                changeNextQuestion();

                Animation fadeIn = AnimationUtils.loadAnimation(QuizActivityWords.this, android.R.anim.fade_in);
                fadeIn.setDuration(300);
                question.startAnimation(fadeIn);
                imageView.startAnimation(fadeIn);
            }
        });
    }

    private void changeNextQuestion() {
        curQuestPos++;

        if (curQuestPos == (questionsLists.size() - 1)) {
            nextBtn.setText("Finish");
        } else if (curQuestPos == questionsLists.size()) {
            ForRes();
            return;
        }

        selectedOptionByUser = "";
        word.setText(" ");
        word.setEnabled(true);
        word.setTextColor(Color.parseColor("#ae8df2"));
        check.setVisibility(View.VISIBLE);

        question.setText(questionsLists.get(curQuestPos).getQuestion());
        questions.setText((curQuestPos + 1) + "/" + questionsLists.size());

        DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference()
                .child("words").child("quiz").child(getSelectedTopicName)
                .child("question" + (curQuestPos + 1)).child("image");

        imageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String base64Image = snapshot.getValue(String.class);
                if (base64Image != null) {
                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imageView.setImageBitmap(bitmap);

                    // slide-in animation for image
                    imageView.setTranslationX(-1000f);
                    imageView.animate().translationX(0f).setDuration(400).start();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void revealAnswer() {
        final String getAnswer = questionsLists.get(curQuestPos).getAnswer();
        check.setVisibility(View.GONE);
        selectedOptionByUser = word.getText().toString().trim().toLowerCase();

        int colorFrom = Color.parseColor("#ae8df2");
        int colorTo = selectedOptionByUser.equals(getAnswer.trim().toLowerCase()) ?
                Color.parseColor("#62B865") : Color.parseColor("#ff0000");

        if (!selectedOptionByUser.equals(getAnswer.trim().toLowerCase())) {
            word.setText(getAnswer);
        }

        ValueAnimator colorAnim = ObjectAnimator.ofInt(word, "textColor", colorFrom, colorTo);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setDuration(400);
        colorAnim.start();

        word.setEnabled(false);

        // --- AUTO NEXT AFTER 5 SECONDS ---
        autoNextRunnable = new Runnable() {
            @Override
            public void run() {
                if (curQuestPos < questionsLists.size()) {
                    animateQuestionChange();
                }
            }
        };
        autoNextHandler.postDelayed(autoNextRunnable, 5000); // 5 seconds
    }

    private void ForRes() {
        autoNextHandler.removeCallbacks(autoNextRunnable);
        Intent intent = new Intent(QuizActivityWords.this, QuizResults.class);
        intent.putExtra("correct", getCorrectAnswers());
        intent.putExtra("incorrect", getInCorrectAnswers());
        intent.putExtra("selectedTopicName", getSelectedTopicName);
        intent.putExtra("size", questionsLists.size());
        startActivity(intent);
        finish();
    }

    private int getCorrectAnswers() {
        int correctAnswers = 0;
        for (QuestionsList q : questionsLists) {
            if (q.getUserSelected() != null && q.getUserSelected().equals(q.getAnswer()))
                correctAnswers++;
        }
        return correctAnswers;
    }

    private int getInCorrectAnswers() {
        int incorrect = 0;
        for (QuestionsList q : questionsLists) {
            if (q.getUserSelected() != null && !q.getUserSelected().equals(q.getAnswer()))
                incorrect++;
        }
        return incorrect;
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
                        if (questionsList1 != null) questionsLists.add(questionsList1);
                    }
                    updateUI();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
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

                        imageView.setTranslationX(-1000f);
                        imageView.animate().translationX(0f).setDuration(400).start();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        }
    }

    private void loadQuestions() {
        new LoadQuestionsTask().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        autoNextHandler.removeCallbacks(autoNextRunnable);
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
        autoNextHandler.removeCallbacks(autoNextRunnable);
        if (animations != null) animations.cleanupAnimations();
    }
}
