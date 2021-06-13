package com.example.androidproject2;

import android.provider.BaseColumns;


public final class UserContract {
    public static final String DB_NAME="calendar.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private UserContract() {}

    /* Inner class that defines the table contents */
    public static class Users implements BaseColumns {
        public static final String TABLE_NAME="cal";
        public static final String KEY_TITLE = "Title";
        public static final String KEY_S_HOUR = "start_hour";
        public static final String KEY_S_MINUTE = "start_minute";
        public static final String KEY_E_HOUR = "end_hour";
        public static final String KEY_E_MINUTE = "end_minute";
        public static final String KEY_PLACE = "Place";
        public static final String KEY_MEMO = "Memo";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                KEY_TITLE + TEXT_TYPE + COMMA_SEP +
                KEY_S_HOUR + TEXT_TYPE + COMMA_SEP +
                KEY_S_MINUTE + TEXT_TYPE + COMMA_SEP +
                KEY_E_HOUR + TEXT_TYPE + COMMA_SEP +
                KEY_E_MINUTE + TEXT_TYPE + COMMA_SEP +
                KEY_PLACE + TEXT_TYPE + COMMA_SEP +
                KEY_MEMO + TEXT_TYPE +  " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}

