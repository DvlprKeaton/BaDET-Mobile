package com.example.badet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int Private_mode = 0;
    private static final String PREF_NAME = "AndroidHivPref";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_CONTACT = "contact";

    public static final String KEY_PASSWORD = "passowrd";

    public SessionManager (Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Private_mode);
        editor = pref.edit();
    }

    public void createLoginSession(String contact, String password){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_CONTACT, contact);
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_CONTACT, pref.getString(KEY_CONTACT, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);
    }
}
