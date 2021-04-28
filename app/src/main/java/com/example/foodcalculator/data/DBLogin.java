package com.example.foodcalculator.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.os.IResultReceiver;
import android.util.Log;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.example.foodcalculator.data.SHAHash.getSalt;
import static com.example.foodcalculator.data.SHAHash.getHashedPassword;
import static com.example.foodcalculator.data.Hex.toHex;
import static com.example.foodcalculator.data.Hex.fromHex;

public class DBLogin {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_SALT = "salt";
    private static final String TAG = "DBLogin";

    private static final String DATABASE_NAME = "logindb";
    private static final String DATABASE_TABLE = "users";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table users (_id integer primary key autoincrement, "
                    + "salt not null, "
                    + "username text not null, "
                    + "password text not null);";

    private Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBLogin(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, context.getFilesDir().getPath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS users");
            onCreate(db);
        }
    }

    public DBLogin open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long AddUser(String username, String password) {
        byte[] salt = null;
        try {
            salt = getSalt();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USERNAME, username);
        initialValues.put(KEY_PASSWORD, toHex(getHashedPassword(password, salt)));
        initialValues.put(KEY_SALT, toHex(salt));
        return db.insert(DATABASE_TABLE, null, initialValues);

    }

    public LoginReturn Login(String username, String password) throws SQLException {

//        DATABASE_TABLE + " WHERE username=? AND password=?"

        try (Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE username=? ", new String[]{username})) {
            while (mCursor.moveToNext()) {

                byte[] salt = fromHex(mCursor.getString(mCursor.getColumnIndex(KEY_SALT)));
                byte[] loginPassword = getHashedPassword(password, salt);
                byte[] storedPassword = fromHex(mCursor.getString(mCursor.getColumnIndex(KEY_PASSWORD)));

                if (Arrays.equals(loginPassword, storedPassword)) {
                    return new LoginReturn(mCursor.getString(mCursor.getColumnIndex(KEY_ROWID)), mCursor.getString(mCursor.getColumnIndex(KEY_USERNAME)), true);
                }
            }
        }
        return new LoginReturn("", "", false);
    }

}
