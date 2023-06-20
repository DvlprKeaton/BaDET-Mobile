package com.example.badet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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


public class BackgroundWorker extends AsyncTask<String,Void,String> {

    Context context;
    AlertDialog alertDialog;
    BackgroundWorker(Context ctx){
        context = ctx;
    }
    SessionManager sessionManager;

    public static String LOG_ID = "";
    public static String LOG_NAME = "";
    public static String LOG_CON = "";


    @Override
    protected String doInBackground(String... params) {
        String login_url = params[0];
        String type = params[1];
        String name = params[2];
        String username = params[3];
        String password = params[4];
        String extra = params[5];

        try {

            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String post_data = "";
            if(type.equals("register")) {
                post_data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&"
                        + URLEncoder.encode("contact", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")+ "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            }
            else if(type.equals("login")){
                post_data = URLEncoder.encode("contact", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            }else if(type.equals("profile")){
                post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            }
            else if(type.equals("update")){
                post_data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&"
                        + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")+ "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")+ "&"
                        + URLEncoder.encode("extra", "UTF-8") + "=" + URLEncoder.encode(extra, "UTF-8");
            }

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {

                result += line;
            }
            Log.d("PHP RESPONSE", result);
            String[] parts = result.replace("[", "").replace("]", "").replace("\"", "").split(",");
            String message = parts[0];
            String uid = parts[1];
            String aname = parts[2];
            String cnumm = parts[3];

            Log.d("PHP RESPONSE", message);
            Log.d("PHP RESPONSE", uid);

            if (message.equals("Session is valid")){
                Log.d("PHP RESPONSE", uid);
                LOG_ID = uid;
                LOG_NAME = aname;
                LOG_CON = cnumm;
                Intent intent = new Intent(context.getApplicationContext(), HomeActivity.class);
                intent.putExtra("user_id",LOG_ID);
                context.startActivity(intent);
            }else if (message.equals("Registed!")){
                Log.d("PHP RESPONSE", uid);
                Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
                context.startActivity(intent);
            }else if (message.equals("Profile updated")){
                Log.d("PHP RESPONSE", uid);
                Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
                intent.putExtra("user_id",LOG_ID);
                context.startActivity(intent);
            }
            Log.d("TYPE", type);
            if (type.equals("profile")){
                Log.d("PHP RESPONSE", uid);
                Intent intent = new Intent(context.getApplicationContext(), ProfileActivity.class);
                intent.putExtra("user_id",LOG_ID);
                intent.putExtra("name",LOG_NAME);
                intent.putExtra("cnum",LOG_CON);
                context.startActivity(intent);
            }

            if(type.equals("emergency")){
                Log.d("BackResults",result);
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        ArrayList<String> arrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String item = jsonArray.getString(i);
                            arrayList.add(item);
                        }
                        Intent intent = new Intent(context.getApplicationContext(), EmergencyContactsActivity.class);
                        intent.putStringArrayListExtra("arrayList", arrayList);
                        intent.putExtra("user_id",LOG_ID);
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


            }

            if (type.equals("upload")){
                Toast.makeText(context, ""+name+" "+username+" "+password+","+extra, Toast.LENGTH_SHORT).show();
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(context);

                // Create a JSON object to hold the image data
                JSONObject imageJsonObject = new JSONObject();
                try {
                    imageJsonObject.put("image", name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Create a new JsonObjectRequest to send the image data to the server
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, login_url, imageJsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(context, ""+name+" "+username+" "+password+","+extra, Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors that occur during the request
                    }
                });

// Add the request to the RequestQueue
                queue.add(jsonObjectRequest);

            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
