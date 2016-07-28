package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import model.Task;

/**
 * Created by nguyen.hoai.duc on 7/27/2016.
 */
public class DBAdapter {
    private static final String DB_NAME = "worktask";
    private static final String DB_TABLE = "task";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_ESTIMATE = "estimate";
    private static final String KEY_STATUS = "status";
    private static final String KEY_STARTTIME = "starttime";
    private static final String KEY_STARTDATE = "startdate";
    private static final String KEY_DUETIME = "duetime";
    private static final String KEY_DUEDATE = "duedate";
    private static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " ("
            + KEY_ID + " integer primary key autoincrement,"
            + KEY_TITLE + " TEXT,"
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_PRIORITY + " TEXT,"
            + KEY_ESTIMATE + " TEXT,"
            + KEY_STATUS + " TEXT,"
            + KEY_STARTTIME + " TEXT,"
            + KEY_STARTDATE + " TEXT,"
            + KEY_DUETIME + " TEXT,"
            + KEY_DUEDATE + " TEXT)";

    private SQLiteDatabase mDb;
    private DBOpenHelper mDbOpenHelper;
    final Context mContext;

    public DBAdapter(Context ctx) {
        this.mContext = ctx;
        mDbOpenHelper = new DBOpenHelper(mContext);
    }

    private static class DBOpenHelper extends SQLiteOpenHelper {

        DBOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        mDb = mDbOpenHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbOpenHelper.close();
    }

    public long addTask(Task mTask) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, mTask.getTitle());
        initialValues.put(KEY_DESCRIPTION, mTask.getDescription());
        initialValues.put(KEY_PRIORITY, mTask.getPriority());
        initialValues.put(KEY_ESTIMATE, mTask.getEstimate());
        initialValues.put(KEY_STATUS, mTask.getStatus());
        initialValues.put(KEY_STARTTIME, mTask.getStarttime());
        initialValues.put(KEY_STARTDATE, mTask.getStartdate());
        initialValues.put(KEY_DUETIME, mTask.getDuetime());
        initialValues.put(KEY_DUEDATE, mTask.getDuedate());
        return mDb.insert(DB_TABLE, null, initialValues);
    }

    public boolean deleteContact(long rowID) {
        return mDb.delete(DB_TABLE, KEY_ID + "=" + rowID, null) > 0;
    }

    public ArrayList<Task> getAllTask() {
        ArrayList<Task> mListTask = new ArrayList<>();
        Cursor mCursor = mDb.query(DB_TABLE, new String[]{KEY_ID,
                KEY_TITLE,
                KEY_DESCRIPTION,
                KEY_PRIORITY,
                KEY_ESTIMATE,
                KEY_STATUS,
                KEY_STARTTIME,
                KEY_STARTDATE,
                KEY_DUETIME,
                KEY_DUEDATE}, null, null, null, null, null);

        if (mCursor.moveToFirst()) {
            do {
                Task mTask = cursorToObject(mCursor);
                mListTask.add(mTask);

            } while (mCursor.moveToNext());
        }
        mDb.close();
        return mListTask;
    }

    public Task getTask(int rowId) {
        Cursor mCursor = mDb.query(true, DB_TABLE, new String[]{KEY_ID,
                        KEY_TITLE,
                        KEY_DESCRIPTION,
                        KEY_PRIORITY,
                        KEY_ESTIMATE,
                        KEY_STATUS,
                        KEY_STARTTIME,
                        KEY_STARTDATE,
                        KEY_DUETIME,
                        KEY_DUEDATE}, KEY_ID + "=" + rowId, null, null,
                null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        Task mTask = cursorToObject(mCursor);
        return mTask;
    }

    public ArrayList<Task> filterTask(List<String> status) {
        ArrayList<Task> mListTask = new ArrayList<>();
        for (int i = 0; i < status.size(); i++) {
            Cursor mCursor = mDb.query(true, DB_TABLE, new String[]{KEY_ID,
                            KEY_TITLE,
                            KEY_DESCRIPTION,
                            KEY_PRIORITY,
                            KEY_ESTIMATE,
                            KEY_STATUS,
                            KEY_STARTTIME,
                            KEY_STARTDATE,
                            KEY_DUETIME,
                            KEY_DUEDATE}, KEY_STATUS + "= '" + status.get(i) + "'", null, null,
                    null, null, null);
            if (mCursor.moveToFirst()) {
                do {
                    Task mTask = cursorToObject(mCursor);
                    mListTask.add(mTask);

                } while (mCursor.moveToNext());
            }
        }
        mDb.close();
        return mListTask;
    }

    public Task cursorToObject(Cursor mCursor) {
        Task mTask = new Task();
        mTask.setId(mCursor.getInt(mCursor.getColumnIndex("id")));
        mTask.setTitle(mCursor.getString(mCursor.getColumnIndex("title")));
        mTask.setDescription(mCursor.getString(mCursor.getColumnIndex("description")));
        mTask.setPriority(mCursor.getString(mCursor.getColumnIndex("priority")));
        mTask.setEstimate(mCursor.getString(mCursor.getColumnIndex("estimate")));
        mTask.setStatus(mCursor.getString(mCursor.getColumnIndex("status")));
        mTask.setStarttime(mCursor.getString(mCursor.getColumnIndex("starttime")));
        mTask.setStartdate(mCursor.getString(mCursor.getColumnIndex("startdate")));
        mTask.setDuetime(mCursor.getString(mCursor.getColumnIndex("duetime")));
        mTask.setDuedate(mCursor.getString(mCursor.getColumnIndex("duedate")));
        return mTask;
    }

    public boolean editTask(int rowId, Task editTask) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, editTask.getTitle());
        args.put(KEY_DESCRIPTION, editTask.getDescription());
        args.put(KEY_PRIORITY, editTask.getPriority());
        args.put(KEY_ESTIMATE, editTask.getEstimate());
        args.put(KEY_STATUS, editTask.getStatus());
        args.put(KEY_STARTTIME, editTask.getStarttime());
        args.put(KEY_STARTDATE, editTask.getStartdate());
        args.put(KEY_DUETIME, editTask.getDuetime());
        args.put(KEY_DUEDATE, editTask.getDuedate());

        return mDb.update(DB_TABLE, args, KEY_ID + "=" + rowId, null) > 0;
    }

    public boolean updateStatus(int rowId, String status) {
        ContentValues args = new ContentValues();
        args.put(KEY_STATUS, status);
        return mDb.update(DB_TABLE, args, KEY_ID + "=" + rowId, null) > 0;
    }
}
