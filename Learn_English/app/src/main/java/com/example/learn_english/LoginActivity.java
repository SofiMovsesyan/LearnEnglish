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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                    crAcc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, Register.class));
                            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                            startActivity(intent);
//                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Loginbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoginUser();


                        }
                    });


                }
            }
        }, 10);

    }
    
    private void LoginUser() {
        String email = LogEmail.getText().toString();
        String password = LogPassword.getText().toString();

        if (!email.matches(emailPattern)) {
            LogEmail.setError("Enter Correct Email");
            LogEmail.requestFocus();
        } else if (password.isEmpty() || password.length() < 8) {
            LogPassword.setError("Enter Proper Password");
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
                            sendUserToNextActivity();
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            err.setError("Verify your email");
                            err.setFocusable(true);
                            err.setFocusableInTouchMode(true);
                            err.requestFocus();
                            Toast.makeText(LoginActivity.this, "Email not verified", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                        }
                    }
                } else {
                    progressDialog.dismiss();
                    err.setError("Your password or Email is wrong");
                    err.setFocusable(true);
                    err.setFocusableInTouchMode(true);
                    err.requestFocus();
                    Toast.makeText(LoginActivity.this, "Login Failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
