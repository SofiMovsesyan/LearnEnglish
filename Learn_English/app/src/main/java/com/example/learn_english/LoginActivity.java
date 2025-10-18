package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    public static String PREFS_NAME = "MyPrefsFile";
    TextView crAcc, err;
    EditText LogEmail, LogPassword;
    Button Loginbtn;
    String emailPattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    private Animations animations;
    private ImageView waveHeaderA, waveHeaderB;
    private ImageView waveLayer2A, waveLayer2B;
    private ImageView waveLayer3A, waveLayer3B;
    private ImageView bubble1, bubble2, bubble3, bubble4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        animations = new Animations();
        initializeViews();
        setupAnimations();

        crAcc = findViewById(R.id.crAcc);
        err = findViewById(R.id.err);
        LogEmail = findViewById(R.id.LogEmail);
        LogPassword = findViewById(R.id.LogPassword);
        Loginbtn = findViewById(R.id.Loginbtn);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn", false);
                if (hasLoggedIn) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    setupClickListenersWithAnimations();
                }
            }
        }, 10);
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

        crAcc = findViewById(R.id.crAcc);
        err = findViewById(R.id.err);
        LogEmail = findViewById(R.id.LogEmail);
        LogPassword = findViewById(R.id.LogPassword);
        Loginbtn = findViewById(R.id.Loginbtn);
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

            animateFormEntrance();
        });
    }

    private void animateFormEntrance() {
        View mainCard = findViewById(R.id.mainContent);
        if (mainCard != null) {
            animations.slideUpView(mainCard, 800);
        }

        new Handler().postDelayed(() -> {
            if (LogEmail != null) animations.fadeInView(LogEmail, 400);
        }, 200);

        new Handler().postDelayed(() -> {
            if (LogPassword != null) animations.fadeInView(LogPassword, 400);
        }, 400);

        new Handler().postDelayed(() -> {
            if (Loginbtn != null) animations.fadeInView(Loginbtn, 400);
        }, 600);

        new Handler().postDelayed(() -> {
            if (crAcc != null) animations.fadeInView(crAcc, 400);
        }, 800);
    }

    private void setupClickListenersWithAnimations() {
        crAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                            startActivity(intent);
                        })
                        .start();
            }
        });

        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                            LoginUser();
                        })
                        .start();
            }
        });
    }

    private void LoginUser() {
        String email = LogEmail.getText().toString();
        String password = LogPassword.getText().toString();

        if (!email.matches(emailPattern)) {
            animations.shakeView(LogEmail);
            showBeautifulError("Please enter a valid email address");
            LogEmail.requestFocus();
        } else if (password.isEmpty() || password.length() < 8) {
            animations.shakeView(LogPassword);
            showBeautifulError("Password must be at least 8 characters");
            LogPassword.requestFocus();
        } else {
            progressDialog.setMessage("Please Wait While Login...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    mUser = mAuth.getCurrentUser();
                    if (mUser != null) {
                        if (mUser.isEmailVerified()) {
                            SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("hasLoggedIn", true);
                            editor.apply();
                            progressDialog.dismiss();

                            showBeautifulSuccess("Login successful! Welcome back!");

                            new Handler().postDelayed(() -> {
                                sendUserToNextActivity();
                            }, 1500);

                        } else {
                            progressDialog.dismiss();
                            showBeautifulError("Please verify your email before logging in");
                            mAuth.signOut();
                        }
                    }
                } else {
                    progressDialog.dismiss();
                    showBeautifulError("Invalid email or password. Please try again.");
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

    private void sendUserToNextActivity() {
        View root = findViewById(android.R.id.content);
        if (root != null) {
            root.animate()
                    .alpha(0f)
                    .setDuration(500)
                    .withEndAction(() -> {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .start();
        } else {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
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