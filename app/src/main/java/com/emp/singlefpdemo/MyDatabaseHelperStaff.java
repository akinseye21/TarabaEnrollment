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

public class MyDatabaseHelperStaff extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "Staff_Enrollment.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "staff_table";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_FIRSTNAME = "first_name";
    private static final String COLUMN_SURNAME = "surname";
    private static final String COLUMN_OTHERNAME = "other_name";
    private static final String COLUMN_DOB = "dob";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_NIN = "nin";
    private static final String COLUMN_QUALIFICATION = "qualification";
    private static final String COLUMN_QUESTION1 = "question_1";
    private static final String COLUMN_QUESTION2 = "question_2";
    private static final String COLUMN_QUESTION3 = "question_3";
    private static final String COLUMN_QUESTION4 = "question_4";
    private static final String COLUMN_RELIGION = "religion";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_NOK = "next_of_kin";
    private static final String COLUMN_REFEREE = "referee";
    private static final String COLUMN_SUBJECTS = "subjects";
    private static final String COLUMN_EXPERIENCE = "experience";
    private static final String COLUMN_SCHOOL = "school_name";
    private static final String COLUMN_SCHOOL_LGA = "school_lga";
    private static final String COLUMN_CREATED_BY = "created_by";
    private static final String COLUMN_PSN = "psn";
    private static final String COLUMN_PICTURE = "picture";
    private static final String COLUMN_FINGERPRINT = "fingerprint";
    private static final String COLUMN_SIGNATURE = "signature";
    private static final String COLUMN_IDCARD = "idcard";
    private static final String COLUMN_EMPLOYMENT_LETTER = "employment";
    private static final String COLUMN_PROMOTION_LETTER = "promotion";
    private static final String COLUMN_STAFF_CATEGORY = "staff_category";
    private static final String COLUMN_BANK_NAME = "bank_name";
    private static final String COLUMN_ACCOUNT_NUMBER = "acc_number";
    private static final String COLUMN_ACCOUNT_NAME = "acc_name";

    public MyDatabaseHelperStaff(@Nullable Context context) {
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
                COLUMN_EMAIL + " TEXT, "+
                COLUMN_PHONE + " TEXT, "+
                COLUMN_NIN + " TEXT, "+
                COLUMN_QUALIFICATION + " TEXT, "+
                COLUMN_QUESTION1 + " TEXT, "+
                COLUMN_QUESTION2 + " TEXT, "+
                COLUMN_QUESTION3 + " TEXT, "+
                COLUMN_QUESTION4 + " TEXT, "+
                COLUMN_RELIGION + " TEXT, "+
                COLUMN_ADDRESS + " TEXT, "+
                COLUMN_GENDER + " TEXT, "+
                COLUMN_NOK + " TEXT, "+
                COLUMN_REFEREE + " TEXT, "+
                COLUMN_SUBJECTS + " TEXT, "+
                COLUMN_EXPERIENCE + " TEXT, "+
                COLUMN_SCHOOL + " TEXT, "+
                COLUMN_SCHOOL_LGA + " TEXT, "+
                COLUMN_CREATED_BY + " TEXT, "+
                COLUMN_PSN + " TEXT, "+
                COLUMN_PICTURE + " BLOB, "+
                COLUMN_FINGERPRINT + " BLOB, "+
                COLUMN_SIGNATURE + " BLOB, "+
                COLUMN_IDCARD + " BLOB, "+
                COLUMN_EMPLOYMENT_LETTER + " BLOB, "+
                COLUMN_PROMOTION_LETTER + " BLOB, "+
                COLUMN_STAFF_CATEGORY + " TEXT, "+
                COLUMN_BANK_NAME + " TEXT, "+
                COLUMN_ACCOUNT_NUMBER + " TEXT, "+
                COLUMN_ACCOUNT_NAME + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(" DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    void addToTable(String firstname, String surname, String othername, String dob, String email, String phone,
                    String nin, String qualification, String question1, String question2, String question3,
                    String question4, String religion, String address, String gender, String nok, String referee,
                    String subjects, String experience, String school, String schoollga, String created_by, String psn,
                    byte[] picture, byte[] fingerprint,  byte[] signature, byte[] idcard, byte[] employment, byte[] promotion,
                    String staff_category, String bankname, String accnumber, String accname){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FIRSTNAME, firstname);
        cv.put(COLUMN_SURNAME, surname);
        cv.put(COLUMN_OTHERNAME, othername);
        cv.put(COLUMN_DOB, dob);
        cv.put(COLUMN_EMAIL, email);
        cv.put(COLUMN_PHONE, phone);
        cv.put(COLUMN_NIN, nin);
        cv.put(COLUMN_QUALIFICATION, qualification);
        cv.put(COLUMN_QUESTION1, question1);
        cv.put(COLUMN_QUESTION2, question2);
        cv.put(COLUMN_QUESTION3, question3);
        cv.put(COLUMN_QUESTION4, question4);
        cv.put(COLUMN_RELIGION, religion);
        cv.put(COLUMN_ADDRESS, address);
        cv.put(COLUMN_GENDER, gender);
        cv.put(COLUMN_NOK, nok);
        cv.put(COLUMN_REFEREE, referee);
        cv.put(COLUMN_SUBJECTS, subjects);
        cv.put(COLUMN_EXPERIENCE, experience);
        cv.put(COLUMN_SCHOOL, school);
        cv.put(COLUMN_SCHOOL_LGA, schoollga);
        cv.put(COLUMN_CREATED_BY, created_by);
        cv.put(COLUMN_PSN, psn);
        cv.put(COLUMN_PICTURE, picture);
        cv.put(COLUMN_FINGERPRINT, fingerprint);
        cv.put(COLUMN_SIGNATURE, signature);
        cv.put(COLUMN_IDCARD, idcard);
        cv.put(COLUMN_EMPLOYMENT_LETTER, employment);
        cv.put(COLUMN_PROMOTION_LETTER, promotion);
        cv.put(COLUMN_STAFF_CATEGORY, staff_category);
        cv.put(COLUMN_BANK_NAME, bankname);
        cv.put(COLUMN_ACCOUNT_NUMBER, accnumber);
        cv.put(COLUMN_ACCOUNT_NAME, accname);

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
        return cursor;
    }

    public Bitmap getPicture(int id){
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE _id = "+id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        byte[] bitmap = cursor.getBlob(24);
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
        byte[] bitmap = cursor.getBlob(25);
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
        byte[] bitmap = cursor.getBlob(26);
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

    public Bitmap getIdCard(int id){
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE _id = "+id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        byte[] bitmap = cursor.getBlob(27);
        Bitmap image = null;
        if (bitmap != null && bitmap.length > 0) {
            image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            // Now 'image' should contain the decoded bitmap
        } else {
            // Handle case where byte[] is empty or null
            System.out.println("Error idcard image");
        }
//        Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
        return image;
    }

    public Bitmap getEmploymentLetter(int id){
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE _id = "+id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        byte[] bitmap = cursor.getBlob(28);
        Bitmap image = null;
        if (bitmap != null && bitmap.length > 0) {
            image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            // Now 'image' should contain the decoded bitmap
        } else {
            // Handle case where byte[] is empty or null
            System.out.println("Error idcard image");
        }
//        Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
        return image;
    }

    public Bitmap getPromotionLetter(int id){
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE _id = "+id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        byte[] bitmap = cursor.getBlob(29);
        Bitmap image = null;
        if (bitmap != null && bitmap.length > 0) {
            image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            // Now 'image' should contain the decoded bitmap
        } else {
            // Handle case where byte[] is empty or null
            System.out.println("Error promotion image");
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
