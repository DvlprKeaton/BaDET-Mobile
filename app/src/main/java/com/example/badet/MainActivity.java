package com.example.badet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    EditText cnum, pword;
    TextView register;
    Button login;

    DBHelper DB;

    String userId;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    //    sessionManager = new SessionManager(getApplicationContext());

        String fileName = "userid.txt";
        File file = new File(getFilesDir(), fileName);

        if (file.exists()) {
            try {
                FileInputStream inputStream = openFileInput(fileName);
                int content;
                StringBuilder stringBuilder = new StringBuilder();

                while ((content = inputStream.read()) != -1) {
                    stringBuilder.append((char) content);
                }

                String userId = stringBuilder.toString();
                inputStream.close();
                Log.d("FILE DATA", userId);

                if (userId.isEmpty()) {
                    // The file contains no data, delete it
                    file.delete();
                } else {
                    // The file contains data, start the home activity
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("user_id", userId);
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        //EditText
        cnum = findViewById(R.id.cnum);
        pword = findViewById(R.id.pword);

        //TextView
        register = findViewById(R.id.register);

        String fullText = "Don't have an account? Click to sign up";
        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf("Click to sign up");
        int endIndex = startIndex + "Click to sign up".length();
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        register.setText(spannableString);

        //Button
        login = findViewById(R.id.btn_login);

        //Database
        DB = new DBHelper(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String contact = cnum.getText().toString();
                String password = pword.getText().toString();

                if (contact.equals("") || password.equals(""))
                    Toast.makeText(MainActivity.this, "Please enter all missing fields", Toast.LENGTH_SHORT).show();
                else{

                    String url = "https://badet-portal.online/validateData.php";
                    String type = "login";
                    BackgroundWorker backgroundWorker = new BackgroundWorker(MainActivity.this);
                    backgroundWorker.execute(url,type,null,contact,password,null);
                }
            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistrationForm.class);
                startActivity(intent);
            }
        });
    }
}