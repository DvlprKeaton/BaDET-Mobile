package com.example.badet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements LocationListener {

    public static final int REQUEST_CODE = 22;
    public static final int REQUEST_LOCATION = 1;
    ImageView btn_open;
    ImageView imageView;

    String lat, lang;
    String finAddress;
    String Summary;

    LocationManager locationManager;

    BottomNavigationView nav;

    private String selectedPicture = "";

    SessionManager sessionManager;

    String user_id;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//       sessionManager = new SessionManager(getApplicationContext());
//        sessionManager.checkLogin();

        btn_open = findViewById(R.id.openCam);
        imageView = findViewById(R.id.imgView);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        Log.d("PHP RESPONSE", user_id);

        if (user_id.isEmpty()) {
            user_id = userId;
            Log.d("PHP RESPONSE", user_id);
        }

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

                userId = stringBuilder.toString();
                inputStream.close();

                // Do something with the user ID

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            try {
                FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                outputStream.write(user_id.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }



        nav = findViewById(R.id.nav_bar);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("user_id", user_id);
                        startActivity(intent);
                        break;
                    case R.id.contacts:
                        String url = "https://badet-portal.online/emergencyData.php";
                        String type = "emergency";
                        BackgroundWorker backgroundWorker = new BackgroundWorker(HomeActivity.this);
                        backgroundWorker.execute(url, type, null, null, null, null);
                        break;
                    case R.id.settings:
                        Intent intent3 = new Intent(getApplicationContext(), SettingsActivity.class);
                        intent3.putExtra("user_id", user_id);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });

        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        // locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, HomeActivity.this);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(HomeActivity.this);
                if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(HomeActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations, this can be null.
                                if (location != null) {
                                    lat = String.valueOf(location.getLatitude());
                                    lang = String.valueOf(location.getLongitude());
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, REQUEST_CODE);
                                }
                            }
                        });
               /* locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    OnGPS();
                }else {
                    getLocation();
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE);*/
            }
        });
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }else{
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (locationGPS != null){
                double latd = locationGPS.getLatitude();
                double langd = locationGPS.getLongitude();

                lat = String.valueOf(latd);
                lang = String.valueOf(langd);
            }else if (LocationNetwork != null){
                double latd = LocationNetwork.getLatitude();
                double langd = LocationNetwork.getLongitude();

                lat = String.valueOf(latd);
                lang = String.valueOf(langd);
            }else if (LocationPassive != null){
                double latd = LocationPassive.getLatitude();
                double langd = LocationPassive.getLongitude();

                lat = String.valueOf(latd);
                lang = String.valueOf(langd);
            }else{
                Toast.makeText(this, "Can't Get your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void OnGPS(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
           // photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            //byte[] imageBytes = baos.toByteArray();
          //  selectedPicture = Base64.encodeToString(imageBytes, Base64.DEFAULT);



            AlertDialog.Builder summaryDialog = new AlertDialog.Builder(HomeActivity.this);
            summaryDialog.setTitle("Please enter additional information");

            final EditText summ = new EditText(HomeActivity.this);
            summ.setInputType(InputType.TYPE_CLASS_TEXT);
            summaryDialog.setView(summ);

            summaryDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Summary = summ.getText().toString();

                    ByteArrayOutputStream byteArrayOutputStream;
                    byteArrayOutputStream = new ByteArrayOutputStream();

                    if (photo != null){
                        photo.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();
                        selectedPicture = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                        String url = "https://badet-portal.online/imageData.php";
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("success")){
                                            Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Image uploading failed", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("name","Sample");
                                params.put("picture", selectedPicture);
                                params.put("description", Summary);
                                params.put("latitude", lat);
                                params.put("longitude", lang);
                                params.put("user_id", user_id);

                                return params;
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("Content-Type", "application/x-www-form-urlencoded");
                                return params;
                            }
                        };
                        queue.add(stringRequest);
                    }else{
                        Toast.makeText(HomeActivity.this, "Capture Image First!", Toast.LENGTH_SHORT).show();
                    }


                }
            });

            summaryDialog.setNegativeButton("Recapture", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            });
            summaryDialog.show();


        }else{
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        //lat = String.valueOf(latitude);
        //lang = String.valueOf(longitude);

       // Toast.makeText(this, ""+lat+","+lang, Toast.LENGTH_SHORT).show();

        // Remove location updates (to conserve battery)
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }
}