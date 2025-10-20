package com.example.learn_english;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    TextView alrhaveacc, err;
    EditText RegName, RegEmail, RegPassword, ConfPass;
    Button Regbtn;
    String emailPattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    private Animations animations;
    private ImageView waveHeaderA, waveHeaderB;
    private ImageView waveLayer2A, waveLayer2B;
    private ImageView waveLayer3A, waveLayer3B;
    private ImageView bubble1, bubble2, bubble3, bubble4;
    private View mainCard;
    private ConstraintLayout rootLayout;
    private int originalCardHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        animations = new Animations();

        initializeViews();
        setupAnimations();

        // Setup click listeners with animations
        setupClickListenersWithAnimations();

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
    }

    private void initializeViews() {
        rootLayout = findViewById(R.id.root_constraint);
        mainCard = findViewById(R.id.mainContent);
        err = findViewById(R.id.err);

        // Store original card height after layout
        if (mainCard != null) {
            mainCard.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mainCard.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    originalCardHeight = mainCard.getHeight();
                }
            });
        }

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

        alrhaveacc = findViewById(R.id.alrhaveacc);
        RegName = findViewById(R.id.RegName);
        RegEmail = findViewById(R.id.RegEmail);
        RegPassword = findViewById(R.id.RegPassword);
        ConfPass = findViewById(R.id.ConfPass);
        Regbtn = findViewById(R.id.Regbtn);
    }

    private void setupAnimations() {
        View root = findViewById(android.R.id.content);

        animations.waitForLayout(root, () -> {
            if (waveHeaderA != null && waveHeaderB != null) {
                animations.startWaveAnimations(waveHeaderA, waveHeaderB,
                        waveLayer2A, waveLayer2B, waveLayer3A, waveLayer3B);
            }

            if (bubble1 != null && bubble2 != null && bubble3 != null && bubble4 != null) {
                animations.startBubbleAnimations(root, bubble1, bubble2, bubble3, bubble4);
            }

        });
    }

    private void setupClickListenersWithAnimations() {
        alrhaveacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add scale animation on click
                v.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .withEndAction(() -> {
                            v.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(100)
                                    .start();

                            // Navigate after animation
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        })
                        .start();
            }
        });

        Regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add button press animation
                v.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .withEndAction(() -> {
                            v.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(100)
                                    .start();

                            // Call registration method after animation
                            CreateAuth();
                        })
                        .start();
            }
        });
    }

    private void CreateAuth() {
        String name = RegName.getText().toString();
        String email = RegEmail.getText().toString();
        String password = RegPassword.getText().toString();
        String confirmPass = ConfPass.getText().toString();

        if (name.isEmpty()) {
            animations.shakeView(RegName);
            showBeautifulError("Please enter your full name");
            RegName.requestFocus();
        } else if (!email.matches(emailPattern)) {
            animations.shakeView(RegEmail);
            showBeautifulError("Please enter a valid email address");
            RegEmail.requestFocus();
        } else if (password.isEmpty() || password.length() < 8) {
            animations.shakeView(RegPassword);
            showBeautifulError("Password must be at least 8 characters");
            RegPassword.requestFocus();
        } else if (!password.equals(confirmPass)) {
            animations.shakeView(ConfPass);
            showBeautifulError("Passwords do not match");
            ConfPass.requestFocus();
        } else {
            progressDialog.setMessage("Please Wait While Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            showBeautifulSuccess("Registration successful! Sending verification email...");
                            sendVerificationEmail();
                        } else {
                            progressDialog.dismiss();
                            showBeautifulError("Registration failed: " + task.getException().getMessage());
                        }
                    });
        }
    }

    private void showBeautifulError(String message) {
        if (err != null) {
            animations.showErrorWithAnimation(err, message);

            new Handler().postDelayed(() -> {
                animations.hideMessageWithAnimation(err);
            }, 5000);
        }
    }

    private void showBeautifulSuccess(String message) {
        if (err != null) {
            animations.showSuccessWithAnimation(err, message);

            new Handler().postDelayed(() -> {
                animations.hideMessageWithAnimation(err);
            }, 3000);
        }
    }

    private void sendVerificationEmail() {
        mUser = mAuth.getCurrentUser();

        if (mUser != null) {
            mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        String userId = mUser.getUid();
                        String email = mUser.getEmail();

                        // Create a User object
                        User user = new User(userId, email);

                        Tenses tenses = new Tenses(0, 0, 0,0,0,0,0,0,0,0,0,0);
                        Prepositions prepositions = new Prepositions(0,0,0,0,0,0,0,0,0,0);
                        Words words = new Words(0,0,0,0,0,0,0,0);
                        user.setTenses(tenses);
                        user.setPrepositions(prepositions);
                        user.setWords(words);

                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                        usersRef.child(userId).setValue(user);

                        progressDialog.dismiss();
                        showBeautifulSuccess("Verification email sent! Please check your inbox.");

                        new Handler().postDelayed(() -> {
                            sendUserToNextActivity();
                        }, 2000);
                    } else {
                        progressDialog.dismiss();
                        showBeautifulError("Failed to send verification email. Please try again.");
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        // Fade out animation before navigation
        View root = findViewById(android.R.id.content);
        if (root != null) {
            root.animate()
                    .alpha(0f)
                    .setDuration(500)
                    .withEndAction(() -> {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .start();
        } else {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animations != null) {
            animations.pauseAnimations();
        }
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
        if (animations != null) {
            animations.cleanupAnimations();
        }
    }
}