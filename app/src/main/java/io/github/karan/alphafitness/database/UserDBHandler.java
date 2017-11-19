package io.github.karan.alphafitness.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A class that manages all the DB
 * for the user class
 */

public class UserDBHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "userID";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_WEIGHT= "weight";

    public static final String TABLE_USER_DATA = "userData";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DISTANCE_WEEKLY= "workoutDistance";
    public static final String COLUMN_TIME_WEEKLY= "workoutTime";
    public static final String COLUMN_WORKOUTS_WEEKLY= "numWorkouts";
    public static final String COLUMN_CALORIES_WEEKLY= "workoutCalories";

    private static final String USER_TABLE =
            " CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_GENDER + " TEXT, " +
                    COLUMN_WEIGHT + " NUMERIC" + ")";

    private static final String USER_DATA_TABLE =
            " CREATE TABLE " + TABLE_USER_DATA + " (" +
                    COLUMN_TIME + " TEXT, " +
                    COLUMN_DISTANCE_WEEKLY + " NUMERIC, " +
                    COLUMN_TIME_WEEKLY + " NUMERIC, " +
                    COLUMN_WORKOUTS_WEEKLY + " NUMERIC, " +
                    COLUMN_CALORIES_WEEKLY + " NUMERIC" + ")";

    public UserDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE);
        db.execSQL(TABLE_USER_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+ TABLE_USERS);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_USER_DATA);
        db.execSQL(USER_TABLE);
        db.execSQL(USER_DATA_TABLE);
    }
}
