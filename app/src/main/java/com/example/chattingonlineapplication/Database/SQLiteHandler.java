package com.example.chattingonlineapplication.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "COUNTRY_CODE_PHONE";
    private static final String DATABASE_NAME = "COUNTRY_LIST";
    private static final int DATABASE_VERSION = 1;
    private static final String countryId = "COUNTRY_ID";
    private static final String countryName = "COUNTRY_NAME";
    private static final String countryCode = "COUNTRY_CODE";

    public SQLiteHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create database
        String sql = "CREATE TABLE " + TABLE_NAME
                + " ("
                + countryId + "Integer primary key,"
                + countryName + "Text"
                + countryCode + "Integer"
                + ") ";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
