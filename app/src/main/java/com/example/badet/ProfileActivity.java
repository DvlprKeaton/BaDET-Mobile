package com.example.badet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileActivity extends AppCompatActivity {

    String user_id,name,cnum;

    TextView profile_name,profile_id,profile_cnum;

    Button update;

    BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_name = findViewById(R.id.profile_name);
        profile_id = findViewById(R.id.profile_description);
        profile_cnum = findViewById(R.id.profile_phone);

        update = findViewById(R.id.update);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        name = intent.getStringExtra("name");
        cnum = intent.getStringExtra("cnum");

        profile_name.setText(name);
        profile_id.setText(user_id);
        profile_cnum.setText(cnum);

        nav = findViewById(R.id.nav_bar);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("user_id",user_id);
                        startActivity(intent);
                        break;
                    case R.id.contacts:
                        String url = "https://badet-portal.online/emergencyData.php";
                        String type = "emergency";
                        BackgroundWorker backgroundWorker = new BackgroundWorker(ProfileActivity.this);
                        backgroundWorker.execute(url, type, null, null, null, null);
                        break;
                    case R.id.settings:
                        Intent intent3 = new Intent(getApplicationContext(), SettingsActivity.class);
                        intent3.putExtra("user_id",user_id);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileUpdate.class);
                intent.putExtra("user_id",user_id);
                startActivity(intent);
            }
        });

    }
}