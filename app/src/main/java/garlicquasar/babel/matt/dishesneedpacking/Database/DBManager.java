package garlicquasar.babel.matt.dishesneedpacking.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Database controller
 */
public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String date, int level, int score) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, name);
        contentValue.put(DatabaseHelper.DATE, date);
        contentValue.put(DatabaseHelper.LEVEL, level);
        contentValue.put(DatabaseHelper.SCORE, score);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public void clear() {
        database.delete(DatabaseHelper.TABLE_NAME, null, null);
    }

    public Cursor fetchAll() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.NAME, DatabaseHelper.DATE, DatabaseHelper.LEVEL, DatabaseHelper.SCORE };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, "score DESC");
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchByLevel(int level) {
        String[] columns = new String[] { DatabaseHelper.NAME, DatabaseHelper.DATE, DatabaseHelper.LEVEL, DatabaseHelper.SCORE };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, "level = ?", new String[] {""+level}, null, null, "score DESC", "100");

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int fetchNumberOfLevels() {
        String[] columns = new String[] {  DatabaseHelper.NAME, DatabaseHelper.DATE, DatabaseHelper.LEVEL, DatabaseHelper.SCORE};
        Cursor cursor = database.query(true, DatabaseHelper.TABLE_NAME, columns, null, null, DatabaseHelper.LEVEL, null, null, null);

        return cursor.getCount();
    }

    public int update(long _id, String name, String date, String level, String score) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NAME, name);
        contentValues.put(DatabaseHelper.DATE, date);
        contentValues.put(DatabaseHelper.LEVEL, level);
        contentValues.put(DatabaseHelper.SCORE, score);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}
