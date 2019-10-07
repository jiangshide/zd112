package com.android.cache.sourcestorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.cache.SourceInfo;

import static com.android.cache.Preconditions.checkAllNotNull;
import static com.android.cache.Preconditions.checkNotNull;

/**
 * created by jiangshide on 2019-06-14.
 * email:18311271399@163.com
 */
public class DatabaseSourceInfoStorage extends SQLiteOpenHelper implements SourceInfoStorage {

    private static final String TABLE = "SourceInfo";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_LENGTH = "length";
    private static final String COLUMN_MIME = "mime";
    private static final String[] ALL_COLUMNS = new String[]{COLUMN_ID,COLUMN_URL,COLUMN_LENGTH,COLUMN_MIME};
    private static final String CREATE_SQL = "CREATE TABLE "+TABLE+
            " ("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
            COLUMN_URL+" TEXT NOT NULL,"+
            COLUMN_MIME+" TEXT,"+
            COLUMN_LENGTH+" INTEGER"+");";

    DatabaseSourceInfoStorage(Context context){
        super(context,"AndroidVideoCache.db",null,1);
        checkNotNull(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        checkNotNull(db);
        db.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new IllegalStateException("Should not be called,There is no any migration");
    }

    @Override
    public SourceInfo get(String url) {
        checkNotNull(url);
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(TABLE,ALL_COLUMNS,COLUMN_URL+"=?",new String[]{url},null,null,null);
                    return cursor == null || !cursor.moveToFirst() ? null:convert(cursor);
        }finally {
            if(null != cursor){
                cursor.close();
            }
        }
    }

    private SourceInfo convert(Cursor cursor){
        return new SourceInfo(
          cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL)),
          cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_LENGTH)),
          cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MIME))
        );
    }

    @Override
    public void put(String url, SourceInfo sourceInfo) {
        checkAllNotNull(url,sourceInfo);
        SourceInfo sourceInfoFromDb = get(url);
        boolean exist = sourceInfoFromDb != null;
        ContentValues contentValues = convert(sourceInfo);
        if(exist){
            getWritableDatabase().update(TABLE,contentValues,COLUMN_URL+"=?",new String[]{url});
        }else{
            getWritableDatabase().insert(TABLE,null,contentValues);
        }
    }

    private ContentValues convert(SourceInfo sourceInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_URL,sourceInfo.url);
        contentValues.put(COLUMN_LENGTH,sourceInfo.length);
        contentValues.put(COLUMN_MIME,sourceInfo.mime);
        return contentValues;
    }

    @Override
    public void release() {
        close();
    }
}
