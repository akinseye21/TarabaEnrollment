package com.emp.singlefpdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "Enrollment.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_table";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_FIRSTNAME = "first_name";
    private static final String COLUMN_SURNAME = "surname";
    private static final String COLUMN_OTHERNAME = "other_name";
    private static final String COLUMN_DOB = "dob";
    private static final String COLUMN_GUARDIAN_NAME = "guardian_name";
    private static final String COLUMN_GUARDIAN_PHONE = "guardian_phone";
    private static final String COLUMN_GUARDIAN_ADDRESS = "guardian_address";
    private static final String COLUMN_GUARDIAN_RELATIONSHIP = "guardian_relationship";
    private static final String COLUMN_QUESTION1 = "question_1";
    private static final String COLUMN_QUESTION2 = "question_2";
    private static final String COLUMN_QUESTION3 = "question_3";
    private static final String COLUMN_QUESTION4 = "question_4";
    private static final String COLUMN_QUESTION5 = "question_5";
    private static final String COLUMN_RELIGION = "religion";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_PICTURE = "picture";
    private static final String COLUMN_FINGERPRINT = "fingerprint";
    private static final String COLUMN_SIGNATURE = "signature";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_CLASS = "class";
    private static final String COLUMN_SHOE = "shoe_size";
    private static final String COLUMN_HEIGHT = "height";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_ORIGIN = "origin";
    private static final String COLUMN_SCHOOL = "school_name";
    private static final String COLUMN_SCHOOL_LGA = "school_lga";
    private static final String COLUMN_CREATED_BY = "created_by";
    private static final String COLUMN_TESIS_NUMBER = "tesis_number";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" +COLUMN_ID + " INTEGER PRIMARY KEY, "+
                COLUMN_FIRSTNAME + " TEXT, "+
                COLUMN_SURNAME + " TEXT, " +
                COLUMN_OTHERNAME + " TEXT, "+
                COLUMN_DOB + " TEXT, "+
                COLUMN_GUARDIAN_NAME + " TEXT, "+
                COLUMN_GUARDIAN_PHONE + " TEXT, "+
                COLUMN_GUARDIAN_ADDRESS + " TEXT, "+
                COLUMN_GUARDIAN_RELATIONSHIP + " TEXT, "+
                COLUMN_QUESTION1 + " TEXT, "+
                COLUMN_QUESTION2 + " TEXT, "+
                COLUMN_QUESTION3 + " TEXT, "+
                COLUMN_QUESTION4 + " TEXT, "+
                COLUMN_QUESTION5 + " TEXT, "+
                COLUMN_RELIGION + " TEXT, "+
                COLUMN_ADDRESS + " TEXT, "+
                COLUMN_PICTURE + " BLOB, "+
                COLUMN_FINGERPRINT + " BLOB, "+
                COLUMN_SIGNATURE + " BLOB, "+
                COLUMN_GENDER + " TEXT, "+
                COLUMN_CLASS + " TEXT, "+
                COLUMN_SHOE + " TEXT, "+
                COLUMN_HEIGHT + " TEXT, "+
                COLUMN_WEIGHT + " TEXT, "+
                COLUMN_ORIGIN + " TEXT, "+
                COLUMN_SCHOOL + " TEXT, "+
                COLUMN_SCHOOL_LGA + " TEXT, "+
                COLUMN_CREATED_BY + " TEXT, "+
                COLUMN_TESIS_NUMBER + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(" DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    void addToTable(String firstname, String surname, String othername, String dob, String gname, String gphone,
                    String gaddress, String grelationship, String question1, String question2, String question3,
                    String question4, String question5, String religion, String address, byte[] picture, byte[] fingerprint,  byte[] signature,
                    String gender, String classs, String shoe, String height, String weight, String origin, String school,
                    String schoollga, String created_by, String tesis_num){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FIRSTNAME, firstname);
        cv.put(COLUMN_SURNAME, surname);
        cv.put(COLUMN_OTHERNAME, othername);
        cv.put(COLUMN_DOB, dob);
        cv.put(COLUMN_GUARDIAN_NAME, gname);
        cv.put(COLUMN_GUARDIAN_PHONE, gphone);
        cv.put(COLUMN_GUARDIAN_ADDRESS, gaddress);
        cv.put(COLUMN_GUARDIAN_RELATIONSHIP, grelationship);
        cv.put(COLUMN_QUESTION1, question1);
        cv.put(COLUMN_QUESTION2, question2);
        cv.put(COLUMN_QUESTION3, question3);
        cv.put(COLUMN_QUESTION4, question4);
        cv.put(COLUMN_QUESTION5, question5);
        cv.put(COLUMN_RELIGION, religion);
        cv.put(COLUMN_ADDRESS, address);
        cv.put(COLUMN_PICTURE, picture);
        cv.put(COLUMN_FINGERPRINT, fingerprint);
        cv.put(COLUMN_SIGNATURE, signature);
        cv.put(COLUMN_GENDER, gender);
        cv.put(COLUMN_CLASS, classs);
        cv.put(COLUMN_SHOE, shoe);
        cv.put(COLUMN_HEIGHT, height);
        cv.put(COLUMN_WEIGHT, weight);
        cv.put(COLUMN_ORIGIN, origin);
        cv.put(COLUMN_SCHOOL, school);
        cv.put(COLUMN_SCHOOL_LGA, schoollga);
        cv.put(COLUMN_CREATED_BY, created_by);
        cv.put(COLUMN_TESIS_NUMBER, tesis_num);

        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            System.out.println("Failed to save information");
            Toast.makeText(context, "Failed to save information", Toast.LENGTH_SHORT).show();
        }else{
            System.out.println("Successfully saved information");
            Toast.makeText(context, "Information saved successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }


//        if (cursor != null && cursor.moveToFirst()) {
//            // Cursor is not empty, and there is at least one record
//            // You can iterate through the records or retrieve data as needed
//        } else {
//            // Cursor is empty, no records found
//            // Handle the case when no records are found in the database
//
//        }
        return cursor;
    }

    public Bitmap getImage(int id){
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE _id = "+id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        byte[] bitmap = cursor.getBlob(16);
        Bitmap image = null;
        if (bitmap != null && bitmap.length > 0) {
            image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            // Now 'image' should contain the decoded bitmap
        } else {
            // Handle case where byte[] is empty or null
            System.out.println("Error picture image");
        }
//        Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
        return image;
    }

    public Bitmap getFingerprint(int id){
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE _id = "+id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        byte[] bitmap = cursor.getBlob(17);
        Bitmap image = null;
        if (bitmap != null && bitmap.length > 0) {
            image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            // Now 'image' should contain the decoded bitmap
        } else {
            // Handle case where byte[] is empty or null
            System.out.println("Error fingerprint image");
        }
//        Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
        return image;
    }

    public Bitmap getSignature(int id){
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE _id = "+id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        byte[] bitmap = cursor.getBlob(18);
        Bitmap image = null;
        if (bitmap != null && bitmap.length > 0) {
            image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            // Now 'image' should contain the decoded bitmap
        } else {
            // Handle case where byte[] is empty or null
            System.out.println("Error signature image");
        }
//        Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
        return image;
    }

    public void deleteRecord(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE _id = "+id);
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public int getRecordCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;

        try {
            // Execute a query to get the count
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0); // The count is at index 0
            }

            cursor.close();
        } finally {
            db.close();
        }

        return count;
    }
}
