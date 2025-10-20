package com.example.learn_english;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PrepositionsActivity extends AppCompatActivity {
    ArrayList<PrepositionsModel> prepositionsModels = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private int progress;
    private PrepositionsRecyclerViewAdapter adapter;

    private Animations animations;
    private ImageView waveHeaderA, waveHeaderB;
    private ImageView waveLayer2A, waveLayer2B;
    private ImageView waveLayer3A, waveLayer3B;
    private ImageView bubble1, bubble2, bubble3, bubble4, bubble5, bubble6, bubble7, bubble8, bubble9, bubble10;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepositions);

        animations = new Animations();
        initializeViews();
        setupAnimations();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        setUpPrepositions();

        adapter = new PrepositionsRecyclerViewAdapter(this, prepositionsModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        progress = sharedPreferences.getInt("progress", 0);

        // Load user progress from Firebase
        loadInitialProgressData();
    }

    private void initializeViews() {
        waveHeaderA = findViewById(R.id.waveHeaderA);
        waveHeaderB = findViewById(R.id.waveHeaderB);
        waveLayer2A = findViewById(R.id.waveLayer2A);
        waveLayer2B = findViewById(R.id.waveLayer2B);
        waveLayer3A = findViewById(R.id.waveLayer3A);
        waveLayer3B = findViewById(R.id.waveLayer3B);

        bubble1 = findViewById(R.id.bubble1);
        bubble2 = findViewById(R.id.bubble2);
        bubble3 = findViewById(R.id.bubble3);
        bubble4 = findViewById(R.id.bubble4);
        bubble5 = findViewById(R.id.bubble5);
        bubble6 = findViewById(R.id.bubble6);
        bubble7 = findViewById(R.id.bubble7);
        bubble8 = findViewById(R.id.bubble8);
        bubble9 = findViewById(R.id.bubble9);
        bubble10 = findViewById(R.id.bubble10);
    }

    private void setupAnimations() {
        View root = findViewById(android.R.id.content);

        animations.waitForLayout(root, () -> {
            if (waveHeaderA != null && waveHeaderB != null) {
                animations.startWaveAnimations(waveHeaderA, waveHeaderB,
                        waveLayer2A, waveLayer2B, waveLayer3A, waveLayer3B);
            }

            if (bubble1 != null && bubble2 != null && bubble3 != null && bubble4 != null && bubble5 != null &&
                    bubble6 != null && bubble7 != null && bubble8 != null && bubble9 != null && bubble10 != null) {
                animations.startBubbleAnimations(root, bubble1, bubble2, bubble3, bubble4, bubble5,
                        bubble6, bubble7, bubble8, bubble9, bubble10);
            }
        });
    }

    private void loadInitialProgressData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("users").child(userId).child("prepositions");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i < prepositionsModels.size(); i++) {
                    PrepositionsModel model = prepositionsModels.get(i);
                    String key = model.getPrepositionName().replace(" ", "");

                    if (snapshot.hasChild(key)) {
                        Integer progress = snapshot.child(key).getValue(Integer.class);
                        if (progress != null) {
                            model.setProgress(progress);
                        }
                    } else {
                        model.setProgress(0);
                    }
                }
                adapter.notifyDataSetChanged();
                animateProgressBars();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PrepositionsActivity.this, "Failed to load progress", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void animateProgressBars() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        if (recyclerView != null) {
            recyclerView.post(() -> {
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    View child = recyclerView.getChildAt(i);
                    if (child != null) {
                        View progressBar = child.findViewById(R.id.progressBar3);
                        if (progressBar != null) {
                            progressBar.setAlpha(0f);
                            new Handler().postDelayed(() -> {
                                progressBar.animate()
                                        .alpha(1f)
                                        .setDuration(600)
                                        .start();
                            }, i * 200);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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
        if (adapter != null) adapter.cleanup();
        if (animations != null) animations.cleanupAnimations();
    }

    private void setUpPrepositions() {
        String[] prepositionList = getResources().getStringArray(R.array.prepositions);
        prepositionsModels.clear();
        for (String preposition : prepositionList) {
            prepositionsModels.add(new PrepositionsModel(preposition, 0));
        }
    }
}
