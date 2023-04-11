package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        /* Log out */
//        button = findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFS_NAME, 0);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putBoolean("hasLoggedIn", false);
//                editor.commit();
//                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }
}