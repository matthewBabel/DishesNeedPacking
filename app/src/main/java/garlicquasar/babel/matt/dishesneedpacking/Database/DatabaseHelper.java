package garlicquasar.babel.matt.dishesneedpacking.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Defines database schema
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "local_highscores";

    // Table columns
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String SCORE = "score";
    public static final String LEVEL = "level";
    public static final String DATE = "date";

    // Database Information
    static final String DB_NAME = "DISHWASHER.DB";

    // database version
    static final int DB_VERSION = 2;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + DATE + " TEXT, " + LEVEL + " INT, " + SCORE + " INT);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //when there is no database and the app needs one
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    //when the schema version we need does not match the schema version of the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
