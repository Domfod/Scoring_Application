package com.example.scoringapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "savedGames.db";
    public static final String TABLE_NAME = "savedGamesTable";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "lName";
    public static final String COL_3 = "lScore";
    public static final String COL_4 = "rName";
    public static final String COL_5 = "rScore";
    public static final String COL_6 = "notes";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL("create table "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, lName TEXT," +
            "lScore INTEGER, rName TEXT, rScore INTEGER, notes TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String lName, int lScore, String rName, int rScore, String notes) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, lName);
        contentValues.put(COL_3, lScore);
        contentValues.put(COL_4, rName);
        contentValues.put(COL_5, rScore);
        contentValues.put(COL_6, notes);
       long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if(result == -1) {
            return false;
        }
        else{
            return true;
        }

    }


}
