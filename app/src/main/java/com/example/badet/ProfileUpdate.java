package com.example.badet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileUpdate extends AppCompatActivity {

    String user_id;

    EditText name,cnum,pword,cpword;

    Button submit;
    BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);


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
                        BackgroundWorker backgroundWorker = new BackgroundWorker(ProfileUpdate.this);
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

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        name = findViewById(R.id.profile_name);
        cnum = findViewById(R.id.profile_phone);
        pword = findViewById(R.id.password);
        cpword = findViewById(R.id.con_password);

        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = cnum.getText().toString();
                String fullname = name.getText().toString();
                String password = pword.getText().toString();
                String conpassword = cpword.getText().toString();

                if(contact.equals("") || fullname.equals("") || password.equals("") || conpassword.equals(""))
                    Toast.makeText(ProfileUpdate.this, "Please enter all missing fields", Toast.LENGTH_SHORT).show();
                else{
                    if (password.equals(conpassword)) {
                        String url = "https://badet-portal.online/profileUpdate.php";
                        String type = "update";
                        BackgroundWorker backgroundWorker = new BackgroundWorker(ProfileUpdate.this);
                        backgroundWorker.execute(url,type,fullname,contact,password,user_id);

                    }else{
                        Toast.makeText(ProfileUpdate.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
}