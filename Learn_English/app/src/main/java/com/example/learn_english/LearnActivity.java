package com.example.learn_english;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class LearnActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private List<TextItem> textList;
    private String getSelectedTopicName;
    private TextView tvLearn, topicName;
    private ProgressBar loadingProgress;
    private MaterialButton speakBtn;
    private View contentCard;
    private Animations animations;

    // TTS variables
    private TextToSpeech textToSpeech;
    private boolean isTTSInitialized = false;
    private boolean isSpeaking = false;
    private String currentText = "";

    // Animation variables
    private ValueAnimator pulseAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        initializeViews();
        setupTextToSpeech();
        setupAnimations();
        loadText();
        setupButtonInteractions();
    }

    private void initializeViews() {
        textList = new ArrayList<>();
        getSelectedTopicName = getIntent().getStringExtra("selectedTopic");

        topicName = findViewById(R.id.topicName);
        tvLearn = findViewById(R.id.learn);
        loadingProgress = findViewById(R.id.loadingProgress);
        speakBtn = findViewById(R.id.speakBtn);
        contentCard = findViewById(R.id.content_card);

        // Set topic name
        if (topicName != null && getSelectedTopicName != null) {
            topicName.setText(getSelectedTopicName);
        }

        // Set initial states for animations
        contentCard.setAlpha(0f);
        contentCard.setScaleX(0.9f);
        contentCard.setScaleY(0.9f);
        contentCard.setTranslationY(50f);
        loadingProgress.setVisibility(View.VISIBLE);

        // Hide speak button initially
        speakBtn.setAlpha(0f);

        // Initially disable speak button until TTS is ready
        speakBtn.setEnabled(false);
    }

    private void setupTextToSpeech() {
        textToSpeech = new TextToSpeech(this, this);

        // Set up utterance progress listener
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                runOnUiThread(() -> {
                    isSpeaking = true;
                    speakBtn.setText("Stop Listening");
                    speakBtn.setBackgroundTintList(getColorStateList(R.color.gradient_purple_light));
                    animateSpeakingPulse();
                });
            }

            @Override
            public void onDone(String utteranceId) {
                runOnUiThread(() -> {
                    isSpeaking = false;
                    speakBtn.setText("Listen to Content");
                    speakBtn.setBackgroundTintList(getColorStateList(R.color.gradient_purple_dark));
                    stopSpeakingPulse();
                });
            }

            @Override
            public void onError(String utteranceId) {
                runOnUiThread(() -> {
                    isSpeaking = false;
                    speakBtn.setText("Listen to Content");
                    speakBtn.setBackgroundTintList(getColorStateList(R.color.gradient_purple_dark));
                    stopSpeakingPulse();
                    Toast.makeText(LearnActivity.this, "Speech error occurred", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Text-to-speech language not supported", Toast.LENGTH_SHORT).show();
                speakBtn.setEnabled(false);
            } else {
                isTTSInitialized = true;
                speakBtn.setEnabled(true);
                Log.d("TTS", "Text-to-speech initialized successfully");
            }
        } else {
            Toast.makeText(this, "Text-to-speech initialization failed", Toast.LENGTH_SHORT).show();
            speakBtn.setEnabled(false);
        }
    }

    private void setupAnimations() {
        animations = new Animations();
        View root = findViewById(android.R.id.content);

        animations.waitForLayout(root, () -> {
            // Start wave and bubble animations
            ImageView waveHeaderA = findViewById(R.id.waveHeaderA);
            ImageView waveHeaderB = findViewById(R.id.waveHeaderB);
            ImageView waveLayer2A = findViewById(R.id.waveLayer2A);
            ImageView waveLayer2B = findViewById(R.id.waveLayer2B);
            ImageView bubble1 = findViewById(R.id.bubble1);
            ImageView bubble2 = findViewById(R.id.bubble2);
            ImageView bubble3 = findViewById(R.id.bubble3);

            if (waveHeaderA != null && waveHeaderB != null) {
                animations.startWaveAnimations(waveHeaderA, waveHeaderB,
                        waveLayer2A, waveLayer2B, null, null);
            }

            if (bubble1 != null && bubble2 != null && bubble3 != null) {
                animations.startBubbleAnimations(root, bubble1, bubble2, bubble3);
            }
        });
    }

    private void setupButtonInteractions() {
        speakBtn.setOnClickListener(v -> {
            if (isSpeaking) {
                stopSpeaking();
            } else {
                startSpeaking();
            }
        });
    }

    private void startSpeaking() {
        if (!isTTSInitialized) {
            Toast.makeText(this, "Text-to-speech not ready yet", Toast.LENGTH_SHORT).show();
            return;
        }

        String textToSpeak = tvLearn.getText().toString();
        if (textToSpeak.isEmpty() || textToSpeak.equals("Loading content...")) {
            Toast.makeText(this, "No content to speak", Toast.LENGTH_SHORT).show();
            return;
        }

        // Clear any previous speech
        textToSpeech.stop();

        // Speak the text
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "LearnEnglishUtterance");
        } else {
            textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void stopSpeaking() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            isSpeaking = false;
            speakBtn.setText("Listen to Content");
            speakBtn.setBackgroundTintList(getColorStateList(R.color.gradient_purple_dark));
            stopSpeakingPulse();
        }
    }

    private void animateSpeakingPulse() {
        pulseAnimator = ValueAnimator.ofFloat(1f, 1.1f);
        pulseAnimator.setDuration(500);
        pulseAnimator.setRepeatCount(ValueAnimator.INFINITE);
        pulseAnimator.setRepeatMode(ValueAnimator.REVERSE);
        pulseAnimator.addUpdateListener(animation -> {
            float scale = (float) animation.getAnimatedValue();
            speakBtn.setScaleX(scale);
            speakBtn.setScaleY(scale);
        });
        pulseAnimator.start();
    }

    private void stopSpeakingPulse() {
        if (pulseAnimator != null) {
            pulseAnimator.cancel();
        }
        speakBtn.setScaleX(1f);
        speakBtn.setScaleY(1f);
    }

    private void animateContentEntrance() {
        contentCard.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .translationY(0f)
                .setDuration(800)
                .setInterpolator(new OvershootInterpolator(1.0f))
                .withStartAction(() -> {
                    // Start staggered animations for interactive elements
                    new Handler().postDelayed(() -> animateStaggeredElements(), 300);
                })
                .start();
    }

    private void animateStaggeredElements() {
        // Animate speak button
        new Handler().postDelayed(() -> {
            speakBtn.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(400)
                    .start();
        }, 100);
    }

    private class LoadLearn extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            if (getSelectedTopicName == null) {
                return "No topic selected";
            }

            String cleanTopicName = getSelectedTopicName.replace(" ", "");
            DatabaseReference learnRef = FirebaseDatabase.getInstance().getReference("learn").child(cleanTopicName);

            final String[] text = {""};
            final CountDownLatch latch = new CountDownLatch(1);

            learnRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (DataSnapshot chunkSnapshot : dataSnapshot.getChildren()) {
                        String learn = chunkSnapshot.getValue(String.class);
                        if (learn != null) {
                            stringBuilder.append(learn);
                        }
                    }
                    text[0] = stringBuilder.toString();
                    latch.countDown();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error retrieving data: " + databaseError.getMessage());
                    text[0] = "Error loading content. Please try again.";
                    latch.countDown();
                }
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "Loading interrupted";
            }

            return text[0];
        }

        @Override
        protected void onPostExecute(String fullText) {
            Log.d("LearnActivity", "Loaded data: " + (fullText != null ? fullText.length() + " characters" : "null"));

            // Hide loading progress
            loadingProgress.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction(() -> {
                        loadingProgress.setVisibility(View.GONE);

                        // Process and display text
                        if (fullText != null && !fullText.isEmpty()) {
                            String processedText = fullText.replace("/n", "\n")
                                    .replace("\\n", "\n")
                                    .trim();
                            tvLearn.setText(processedText);
                            currentText = processedText;

                            // Animate content entrance
                            animateContentEntrance();
                        } else {
                            tvLearn.setText("No content available for this topic.");
                            animateContentEntrance();
                        }
                    })
                    .start();
        }
    }

    private void loadText() {
        new LoadLearn().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSpeaking) {
            stopSpeaking();
        }
        if (animations != null) {
            animations.pauseAnimations();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animations != null) {
            View root = findViewById(android.R.id.content);
            animations.waitForLayout(root, () -> {
                ImageView waveHeaderA = findViewById(R.id.waveHeaderA);
                ImageView waveHeaderB = findViewById(R.id.waveHeaderB);
                ImageView waveLayer2A = findViewById(R.id.waveLayer2A);
                ImageView waveLayer2B = findViewById(R.id.waveLayer2B);

                if (waveHeaderA != null && waveHeaderB != null) {
                    animations.startWaveAnimations(waveHeaderA, waveHeaderB,
                            waveLayer2A, waveLayer2B, null, null);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown TTS
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        // Clean up animations
        if (animations != null) {
            animations.cleanupAnimations();
            animations = null;
        }

        if (pulseAnimator != null) {
            pulseAnimator.cancel();
        }
    }
}