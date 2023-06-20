package com.example.badet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "badet.db";

    public DBHelper(Context context) {
        super(context, "badet.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase myDB) {
        myDB.execSQL("CREATE TABLE users(cnum TEXT primary key, fname TEXT, pword TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase myDB, int i, int i1) {
        myDB.execSQL("DROP TABLE IF EXISTS users");
    }

    public Boolean insertData (String cnum, String fname, String pword){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cnum",cnum);
        contentValues.put("fname",fname);
        contentValues.put("pword",pword);
        long results = myDB.insert("users", null, contentValues);
        if (results == 1) return false;
        else
            return true;
    }

    public Boolean checkContact(String cnum){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * FROM users WHERE cnum = ?", new String[]{cnum});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Boolean checkcnumpword(String cnum, String pword){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * FROM users WHERE cnum = ? AND pword = ?", new String[]{cnum,pword});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }
}
