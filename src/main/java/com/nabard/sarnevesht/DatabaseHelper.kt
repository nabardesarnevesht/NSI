package com.journaldev.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    companion object {
        // Table Name
        const val TABLE_NAME = "NotifData"

        // Table columns
        const val _ID = "_id"
        const val SUBJECT = "subject"
        const val PICTURE = "picture"
        const val DATE = "date"
        const val TAG = "tag"
        const val DESC = "description"

        // Database Information
        const val DB_NAME = "Data.DB"

        // database version
        const val DB_VERSION = 1

        // Creating table query
        private const val CREATE_TABLE = ("create table " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUBJECT + " TEXT NOT NULL, " + DESC + " TEXT, " + PICTURE + " TEXT, " + DATE + " DATETIME,"+TAG+" TEXT);")
    }
}