package com.example.chattingonlineapplication.Database.SQLite;

public class ContactSQLiteInit {
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ContactReader.TABLE_NAME + " (" +
                    ContactReader.COLUMN_NO + " INTEGER PRIMARY KEY," +
                    ContactReader.COLUMN_NAME + " TEXT," +
                    ContactReader.COLUMN_PHONENUMBER + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ContactReader.TABLE_NAME;
}
