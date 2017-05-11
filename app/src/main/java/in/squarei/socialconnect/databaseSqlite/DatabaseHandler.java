package in.squarei.socialconnect.databaseSqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Iterator;
import java.util.Map;

import in.squarei.socialconnect.utils.Logger;

/**
 * Created by mohit kumar on 5/11/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Feeds.db";
    private static final String TAG = "DatabaseHandler";
    private static final String TABLE_NAME = "Feeds";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_POST_ID = "post_id";
    private static final String COLUMN_POST_SENDER_NAME = "sender_name";
    private static final String COLUMN_TITLE = "post_title";
    private static final String COLUMN_POST_DESCRIPTION = "post_description";
    private static final String COLUMN_POST_COMMENTS = "post_comments";
    private static final String COLUMN_POST_LIKES = "post_likes";
    private static final String COLUMN_POST_IMAGE = "post_image";
    private static final String COLUMN_POST_DATE = "post_date";

    private static final String CREATE_TABLE = "CREATE TABLE" + TABLE_NAME + "(" + COLUMN_ID + " INT PRIMARY KEY NOT NULL, " + COLUMN_POST_SENDER_NAME + " TEXT NOT NULL " + COLUMN_POST_DATE + " CHAR(50)"
            + COLUMN_POST_ID + " TEXT NOT NULL " + COLUMN_TITLE + " TEXT   " + COLUMN_POST_LIKES + " TEXT " + COLUMN_POST_COMMENTS + " TEXT "
            + COLUMN_POST_IMAGE + " TEXT " + COLUMN_POST_DESCRIPTION + " TEXT" + ")";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //    db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //  db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void inserData(Map<String, String> mapData, DatabaseHandler databaseHandler) {

        Logger.info(TAG, "============values to be inserted=========" + mapData);
        SQLiteDatabase db = databaseHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        Iterator<Map.Entry<String, String>> entries = mapData.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            values.put(entry.getKey(), entry.getValue());
        }
        long newRowId = db.insert("TABLE_NAME", null, values);
    }

    public Cursor getFeedsData(String[] columns, DatabaseHandler databaseHandler) {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();

        // Filter results WHERE "title" = 'My Title'
        String selection = "COLUMN_NAME" + " = ?";
        String[] selectionArgs = {"My Title"};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = "COLUMN_NAME" + " DESC";

        Cursor cursor = db.query("TABLE_NAME",            // The table to query
                columns,                                  // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        return cursor;
    }
}
