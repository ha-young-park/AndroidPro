package com.example.androidproject2;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    final static String TAG="SQLiteDBTest";

    public DBHelper(Context context) {
        super(context, UserContract.DB_NAME, null, UserContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,getClass().getName()+".onCreate()");
        System.out.println("CREATE_TABLE => " + UserContract.Users.CREATE_TABLE);
        db.execSQL(UserContract.Users.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG,getClass().getName() +".onUpgrade()");
        db.execSQL(UserContract.Users.DELETE_TABLE);
        onCreate(db);
    }

    // insert 쿼리
    public void insertUserBySQL(String title, String shour, String sminute, String ehour, String eminute, String place, String memo) {
        try {
            String sql = String.format (
                    "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (NULL, '%s', '%s', '%s', '%s')",
                    UserContract.Users.TABLE_NAME,
                    UserContract.Users._ID,
                    UserContract.Users.KEY_TITLE,
                    UserContract.Users.KEY_S_HOUR,
                    UserContract.Users.KEY_S_MINUTE,
                    UserContract.Users.KEY_E_HOUR,
                    UserContract.Users.KEY_E_MINUTE,
                    UserContract.Users.KEY_PLACE,
                    UserContract.Users.KEY_MEMO,
                    title,
                    shour,
                    sminute,
                    ehour,
                    eminute,
                    place,
                    memo);

            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in inserting recodes");
        }
    }

    // 전체 조회 쿼리
    public Cursor getAllUsersBySQL() {
        String sql = "Select * FROM " + UserContract.Users.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql,null);
    }

    // Max ID 조회 쿼리
    public Cursor getMaxIdBySQL() {
        String sql = "Select MAX(_ID) FROM " + UserContract.Users.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql,null);
    }

    // 삭제 쿼리
    public void deleteUserBySQL(String _id) {
        try {
            String sql = String.format (
                    "DELETE FROM %s WHERE %s = %s",
                    UserContract.Users.TABLE_NAME,
                    UserContract.Users._ID,
                    _id);
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in deleting recodes");
        }
    }

    // 업데이트 쿼리
    public void updateUserBySQL(String _id, String title, String shour, String sminute, String ehour, String eminute, String place, String memo) {
        try {
            String sql = String.format (
                    "UPDATE  %s SET %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s' WHERE %s = %s",
                    UserContract.Users.TABLE_NAME,
                    UserContract.Users.KEY_TITLE, title,
                    UserContract.Users.KEY_S_HOUR, shour,
                    UserContract.Users.KEY_S_MINUTE,sminute,
                    UserContract.Users.KEY_E_HOUR, ehour,
                    UserContract.Users.KEY_E_MINUTE,eminute,
                    UserContract.Users.KEY_PLACE, place,
                    UserContract.Users.KEY_MEMO, memo,
                    UserContract.Users._ID, _id) ;
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in updating recodes");
        }
    }

    //insert 함수
    public long insertUserByMethod(String title, String shour, String sminute, String ehour, String eminute, String place, String memo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserContract.Users.KEY_TITLE, title);
        values.put(UserContract.Users.KEY_S_HOUR, shour);
        values.put(UserContract.Users.KEY_S_MINUTE,sminute);
        values.put(UserContract.Users.KEY_E_HOUR, ehour);
        values.put(UserContract.Users.KEY_E_MINUTE,eminute);
        values.put(UserContract.Users.KEY_PLACE,place);
        values.put(UserContract.Users.KEY_MEMO,memo);

        return db.insert(UserContract.Users.TABLE_NAME,null,values);
    }

    //전체 조회 함수
    public Cursor getAllUsersByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(UserContract.Users.TABLE_NAME,null,null,null,null,null,null);
    }

    //삭제 함수
    public long deleteUserByMethod(String _id) {
        SQLiteDatabase db = getWritableDatabase();

        System.out.println("_id => " + _id);

        String whereClause = UserContract.Users._ID +" = ?";
        String[] whereArgs ={_id};
        return db.delete(UserContract.Users.TABLE_NAME, whereClause, whereArgs);
    }

    //수정 함수
    public long updateUserByMethod(String _id, String title, String shour, String sminute, String ehour, String eminute, String place, String memo) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserContract.Users.KEY_TITLE, title);
        values.put(UserContract.Users.KEY_S_HOUR, shour);
        values.put(UserContract.Users.KEY_S_MINUTE,sminute);
        values.put(UserContract.Users.KEY_E_HOUR, ehour);
        values.put(UserContract.Users.KEY_E_MINUTE,eminute);
        values.put(UserContract.Users.KEY_PLACE,place);
        values.put(UserContract.Users.KEY_MEMO,memo);

        String whereClause = UserContract.Users._ID +" = ?";
        String[] whereArgs ={_id};

        return db.update(UserContract.Users.TABLE_NAME, values, whereClause, whereArgs);
    }
}
