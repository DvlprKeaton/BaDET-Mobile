package com.example.badet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmergencyContactsActivity extends AppCompatActivity {

    BottomNavigationView nav;
    String[] NameList = {"sample"};
    String ContactList [] = {"sample"};
    ListView listView;
    List<String> teamNames = new ArrayList<>();
    List<String> contacts = new ArrayList<>();
    static int PERMISSION_CODE = 100;

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        Log.d("ID",user_id);

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
                        BackgroundWorker backgroundWorker = new BackgroundWorker(EmergencyContactsActivity.this);
                        backgroundWorker.execute(url,type, null,null,null,null);
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

        ArrayList<String> arrayList = getIntent().getStringArrayListExtra("arrayList");

        ArrayList<String> key1List = new ArrayList<>();
        ArrayList<String> key2List = new ArrayList<>();
        ArrayList<String> key3List = new ArrayList<>();

        for (String item : arrayList) {

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(item);
                int id = jsonObject.getInt("id");
                String teamName = jsonObject.getString("team_name");
                String contactNumber = jsonObject.getString("contact_number");

                String teamNamess = jsonObject.getString("team_name");
                teamNames.add(teamNamess);

                String contactss = jsonObject.getString("contact_number");
                contacts.add(contactss);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (ContextCompat.checkSelfPermission(EmergencyContactsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EmergencyContactsActivity.this, new String[]{Manifest.permission.CALL_PHONE},PERMISSION_CODE);
        }

        listView = (ListView) findViewById(R.id.list_contacts);
        CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(),teamNames,contacts);
        listView.setAdapter(customBaseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+ contacts.get(i)));
                startActivity(intent);

            }
        });
    }

}