package com.example.badet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;

public class SettingsActivity extends AppCompatActivity {

    BottomNavigationView nav;
    Button profile,logout;

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        Log.d("USER_ID", user_id);

        profile = findViewById(R.id.profile);
        logout = findViewById(R.id.out);
        nav = findViewById(R.id.nav_bar);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("user_id",user_id);
                        startActivity(intent);
                        break;
                    case R.id.contacts:
                        String url = "https://badet-portal.online/emergencyData.php";
                        String type = "emergency";
                        BackgroundWorker backgroundWorker = new BackgroundWorker(SettingsActivity.this);
                        backgroundWorker.execute(url, type, null, null, null, null);
                        break;
                    case R.id.settings:
                        Intent intent3 = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://badet-portal.online/profileData.php";
                String type = "profile";
                BackgroundWorker backgroundWorker = new BackgroundWorker(SettingsActivity.this);
                backgroundWorker.execute(url, type, null, user_id, null, null);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = "userid.txt";
                File file = new File(getFilesDir(), fileName);
                file.delete();
                Intent intent3 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent3);
            }
        });

    }
}