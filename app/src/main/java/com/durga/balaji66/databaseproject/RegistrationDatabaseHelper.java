package com.durga.balaji66.databaseproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class RegistrationDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_NAME = "UserManager.db";

    private static final String TABLE_USER = "balaji";

    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_FIRST_NAME = "user_firstname";
    private static final String COLUMN_USER_LAST_NAME = "user_lastname";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    private String CREATE_USER_TABLE = "CREATE TABLE "
            + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_FIRST_NAME + " TEXT,"
            + COLUMN_USER_LAST_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT"
            + ")";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;


    public RegistrationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }
//  ALTER TABLE user RENAME TO balaji;
//  ALTER TABLE balaji ADD column status VARCHAR ;
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("ALTER TABLE balaji ADD column status VARCHAR ;");
        //onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_FIRST_NAME, user.getFirstname());
        values.put(COLUMN_USER_LAST_NAME, user.getLastname());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public List<User> getAllUser() {
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_FIRST_NAME,
                COLUMN_USER_LAST_NAME,
                COLUMN_USER_EMAIL,
                COLUMN_USER_PASSWORD
        };
        String sortOrder =
                COLUMN_USER_FIRST_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setFirstname(cursor.getString(cursor.getColumnIndex(COLUMN_USER_FIRST_NAME)));
                user.setLastname(cursor.getString(cursor.getColumnIndex(COLUMN_USER_LAST_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return userList;
    }

    public boolean checkUser(String email) {

        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_USER_EMAIL + " = ?";

        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean checkUser(String email, String password) {

        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    boolean updateUserData(String fname, String lname, String email, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_FIRST_NAME, fname);
        contentValues.put(COLUMN_USER_LAST_NAME, lname);
        contentValues.put(COLUMN_USER_PASSWORD, password);
        return db.update(TABLE_USER, contentValues, COLUMN_USER_EMAIL + "=?", new String[]{email}) == 1;
    }


    public boolean delete(String email) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_USER, COLUMN_USER_EMAIL +  "=" + "'"+email+"'", null) > 0;
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+ TABLE_USER);
    }


}
