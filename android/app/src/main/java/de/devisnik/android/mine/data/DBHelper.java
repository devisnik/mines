package de.devisnik.android.mine.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper {
    public static final String SCORES_DB = "scores";
    public static final String SCORES_TABLE = "scores_table";
    public static final int SCORES_VERSION = 9;

    public static final String DATE_COLUMN = "playdate";
    public static final String TIME_COLUMN = "time";
    public static final String NAME_COLUMN = "name";
    public static final String ID_COLUMN = "_id";

    private static final String BOARD_COLUMN = "board";
    private static final String LEVEL_COLUMN = "level";

    private static final String[] COLUMNS = {ID_COLUMN, NAME_COLUMN, LEVEL_COLUMN, BOARD_COLUMN,
            TIME_COLUMN, DATE_COLUMN};
    private DBOpenHelper itsDbOpenHelper;
    private SQLiteDatabase itsDatabase;

    private static class DBOpenHelper extends SQLiteOpenHelper {

        private static final String CREATE = "CREATE TABLE " + DBHelper.SCORES_TABLE + " ("
                + ID_COLUMN + " INTEGER PRIMARY KEY, " + NAME_COLUMN + " TEXT, " + LEVEL_COLUMN
                + " TEXT, " + BOARD_COLUMN + " TEXT, " + TIME_COLUMN + " INTEGER, " + DATE_COLUMN
                + " INTEGER" + ")";

        public DBOpenHelper(final Context context) {
            super(context, SCORES_DB, null, SCORES_VERSION);
        }

        @Override
        public void onCreate(final SQLiteDatabase db) {
            db.execSQL(CREATE);
        }

        @Override
        public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + SCORES_TABLE);
            onCreate(db);
        }

    }

    public DBHelper(Context context) {
        itsDbOpenHelper = new DBOpenHelper(context);
        itsDatabase = itsDbOpenHelper.getWritableDatabase();
    }

    public void close() {
        if (itsDatabase == null) {
            return;
        }
        itsDatabase.close();
        itsDatabase = null;
    }

    public long insert(Score score) {
        ContentValues values = createContentValues(score);
        return itsDatabase.insert(SCORES_TABLE, null, values);
    }

    @SuppressWarnings("boxing")
    private ContentValues createContentValues(Score score) {
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, score.name);
        values.put(LEVEL_COLUMN, score.level);
        values.put(BOARD_COLUMN, score.board);
        values.put(TIME_COLUMN, score.time);
        values.put(DATE_COLUMN, score.date);
        return values;
    }

    public void delete(Score score) {
        itsDatabase.delete(SCORES_TABLE, "_id=" + score.id, null);
    }

    public Cursor createCursor(String board, String level) {
        return itsDatabase.query(SCORES_TABLE, COLUMNS, BOARD_COLUMN + "='" + board + "' AND "
                + LEVEL_COLUMN + "='" + level + "'", null, null, null, TIME_COLUMN);
    }

    public Score readScore(long id) {
        Cursor cursor = itsDatabase.query(SCORES_TABLE, COLUMNS, "_id=" + id, null, null, null,
                null);
        try {
            if (cursor.moveToFirst())
                return readScore(cursor);
            return null;
        } finally {
            cursor.close();
        }
    }

    public Score readScore(Cursor cursor) {
        Score score = new Score();
        score.id = cursor.getLong(0);
        score.name = cursor.getString(1);
        score.level = cursor.getString(2);
        score.board = cursor.getString(3);
        score.time = cursor.getInt(4);
        score.date = cursor.getLong(5);
        score.rank = cursor.getPosition() + 1;
        return score;
    }

    public long readId(Cursor cursor) {
        return cursor.getLong(0);
    }

    public void delete(Cursor cursor) {
        itsDatabase.delete(SCORES_TABLE, "_id=" + cursor.getLong(0), null);
    }

    public void updateName(Score score) {
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, score.name);
        itsDatabase.update(SCORES_TABLE, values, "_id=" + score.id, null);
    }
}
