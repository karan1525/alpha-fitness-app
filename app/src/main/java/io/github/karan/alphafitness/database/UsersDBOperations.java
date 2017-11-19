package io.github.karan.alphafitness.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import io.github.karan.alphafitness.model.User;

/**
 * A class to do CRUD operations
 * on the database that was created before
 */

public class UsersDBOperations {

    public static final String LOGTAG = "USER_MNGMNT_SYS";

    private SQLiteOpenHelper dbhandler;
    private SQLiteDatabase database;

    private static final String[] allUserColumns = {
            UserDBHandler.COLUMN_ID,
            UserDBHandler.COLUMN_NAME,
            UserDBHandler.COLUMN_GENDER,
            UserDBHandler.COLUMN_WEIGHT
    };

    private static final String[] allUserDataColumns = {
            UserDBHandler.COLUMN_TIME,
            UserDBHandler.COLUMN_DISTANCE_WEEKLY,
            UserDBHandler.COLUMN_TIME_WEEKLY,
            UserDBHandler.COLUMN_WORKOUTS_WEEKLY,
            UserDBHandler.COLUMN_CALORIES_WEEKLY
    };

    public UsersDBOperations(Context context) {
        dbhandler = new UserDBHandler(context);
    }

    public void open() {
        Log.i(LOGTAG, "Database Opened");
        database = dbhandler.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();
    }

    public User addUser(User userToAdd) {
        ContentValues values = new ContentValues();
        values.put(UserDBHandler.COLUMN_NAME, userToAdd.getmName());
        values.put(UserDBHandler.COLUMN_GENDER, userToAdd.getmGender());
        values.put(UserDBHandler.COLUMN_WEIGHT, userToAdd.getmWeight());
        long insertId = database.insert(UserDBHandler.TABLE_USERS, null, values);
        userToAdd.setmId(insertId);
        return userToAdd;
    }

    //Get a single user
    public User getUser(long id) {
        Cursor cursor = database.query(UserDBHandler.TABLE_USERS, allUserColumns,
                UserDBHandler.COLUMN_ID + "=?",new String[]{String.valueOf(id)},
                null,null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Log.d(LOGTAG,cursor.getString(0));
        Log.d(LOGTAG,cursor.getString(1));
        Log.d(LOGTAG,cursor.getString(2));
        Log.d(LOGTAG,cursor.getString(3));

        return new User(Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Float.parseFloat(cursor.getString(3)));
    }

    public int updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(UserDBHandler.COLUMN_NAME, user.getmName());
        values.put(UserDBHandler.COLUMN_GENDER, user.getmGender());
        values.put(UserDBHandler.COLUMN_WEIGHT, user.getmWeight());

        return database.update(UserDBHandler.TABLE_USERS, values,
                UserDBHandler.COLUMN_ID + "=?",
                new String[] { String.valueOf(user.getmId())});

    }

    public void removeUser(User user) {
        database.delete(UserDBHandler.TABLE_USERS,
                UserDBHandler.COLUMN_ID + "=" + user.getmId(), null);
    }


}
