package com.example.chattingonlineapplication.Database.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.chattingonlineapplication.Models.Contact;
import com.example.chattingonlineapplication.Models.PhoneContact;

import java.util.ArrayList;
import java.util.List;

public class ContactSQLiteHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ChattingOnlineDb";
    private static final String TAG = "ContactDatabase";

    public ContactSQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ContactSQLiteInit.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldversion , int newversion) {
        sqLiteDatabase.execSQL(ContactSQLiteInit.SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public void add(PhoneContact contact) {
        Log.i(TAG, "MyDatabaseHelper.add ... " + contact);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ContactReader.COLUMN_NAME, contact.getUserName());
        values.put(ContactReader.COLUMN_PHONENUMBER, contact.getPhoneNumber());

        // Inserting Row
        db.insert(ContactReader.TABLE_NAME, null, values);

        // Closing database connection
        db.close();
    }


    public PhoneContact get(int id) {
        Log.i(TAG, "MyDatabaseHelper.get ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ContactReader.TABLE_NAME, new String[]{ContactReader.COLUMN_NO,
                        ContactReader.COLUMN_NAME, ContactReader.COLUMN_PHONENUMBER}, ContactReader.COLUMN_NAME + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        PhoneContact contact = new PhoneContact(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2));

        return contact;
    }


    public List<PhoneContact> getAll() {
        Log.i(TAG, "MyDatabaseHelper.getAll ... ");

        List<PhoneContact> contactList = new ArrayList<PhoneContact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ContactReader.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PhoneContact c = new PhoneContact();
                c.setUserNumber(Integer.parseInt(cursor.getString(0)));
                c.setUserName(cursor.getString(1));
                c.setPhoneNumber(cursor.getString(2));

                contactList.add(c);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

    public int getCounts() {
        Log.i(TAG, "MyDatabaseHelper.getCounts ... ");

        String countQuery = "SELECT  * FROM " + ContactReader.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }


    public int update(PhoneContact phoneContact) {
        Log.i(TAG, "MyDatabaseHelper.update ... " + phoneContact.getUserName());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContactReader.COLUMN_NAME, phoneContact.getUserName());
        values.put(ContactReader.COLUMN_PHONENUMBER, phoneContact.getPhoneNumber());

        // updating row
        return db.update(ContactReader.TABLE_NAME, values, ContactReader.COLUMN_NO + " = ?",
                new String[]{String.valueOf(phoneContact.getUserNumber())});
    }

    public void delete(PhoneContact contact) {
        Log.i(TAG, "MyDatabaseHelper.delete ... " + contact.getUserName());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ContactReader.TABLE_NAME, ContactReader.COLUMN_NO + " = ?",
                new String[]{String.valueOf(contact.getUserNumber())});
        db.close();
    }

    public void deleteAll(){
        Log.i(TAG, "MyDatabaseHelper.delete all ... " + ContactReader.TABLE_NAME);
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(ContactReader.TABLE_NAME, null, null);
        db.close();
    }
}
