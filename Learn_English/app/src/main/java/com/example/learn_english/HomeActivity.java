package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class HomeActivity extends AppCompatActivity {
    Button button;
    AppCompatButton tenses, prepositions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tenses = findViewById(R.id.tenses);

        tenses.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, TensesActivity.class);
            startActivity(intent);
            finish();
        });

        prepositions = findViewById(R.id.prepositions);

        prepositions.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PrepositionsActivity.class);
            startActivity(intent);
            finish();
        });
        /* Log out */
        button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("hasLoggedIn", false);
            editor.commit();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}