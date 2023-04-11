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
        mUser = mAuth.getCurrentUser();

        alrhaveacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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

        if (!email.matches(emailPattern)) {
            RegEmail.setError("Enter Correct Email");
            RegEmail.requestFocus();
        }else if(password.isEmpty() || password.length()<8) {
            RegPassword.setError("Enter Proper Password");
            RegPassword.requestFocus();

        }else if(!password.equals(confirmPass)) {
            ConfPass.setError("Password not matched");
            ConfPass.requestFocus();
        }else {
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFS_NAME, 0);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("hasLoggedIn", true);
            editor.commit();
            progressDialog.setMessage("Please Wait While Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                sendUserToNextActivity();
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
