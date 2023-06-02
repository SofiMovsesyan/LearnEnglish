package com.example.learn_english;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    TextView alrhaveacc;
    EditText RegName, RegEmail, RegPassword, ConfPass;
    Button Regbtn;
    String emailPattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        alrhaveacc = findViewById(R.id.alrhaveacc);
        RegName = findViewById(R.id.RegName);
        RegEmail = findViewById(R.id.RegEmail);
        RegPassword = findViewById(R.id.RegPassword);
        ConfPass = findViewById(R.id.ConfPass);
        Regbtn = findViewById(R.id.Regbtn);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        alrhaveacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        Regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAuth();
            }
        });
    }

    private void CreateAuth() {
        String name = RegName.getText().toString();
        String email = RegEmail.getText().toString();
        String password = RegPassword.getText().toString();
        String confirmPass = ConfPass.getText().toString();

        if (name.isEmpty()) {
            RegName.setError("Enter Your Name");
        } else if (!email.matches(emailPattern)) {
            RegEmail.setError("Enter Correct Email");
            RegEmail.requestFocus();
        } else if (password.isEmpty() || password.length() < 8) {
            RegPassword.setError("Password must be at least 8 characters");
            RegPassword.requestFocus();
        } else if (!password.equals(confirmPass)) {
            ConfPass.setError("Password not matched");
            ConfPass.requestFocus();
        } else {
            progressDialog.setMessage("Please Wait While Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            sendVerificationEmail();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Registration Failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
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

                        Tenses tenses = new Tenses(0, 0); // Assuming default progress keys are 0

// Add the tenses object to the user
                        user.setTenses(tenses);
                        // Store the user in Firebase Realtime Database
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                        usersRef.child(userId).setValue(user);

                        sendUserToNextActivity();
                    } else {
                        // Failed to send verification email
                        Toast.makeText(RegisterActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                    }
                    sendUserToNextActivity();

                }
            });
        }
    }




    private void sendUserToNextActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
