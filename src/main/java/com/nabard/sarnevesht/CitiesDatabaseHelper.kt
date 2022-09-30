package com.journaldev.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.*

class CitiesDatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, "Iran", null, 1) {
    private val DB_NAME = "Iran" // Database name
    private val TAG = "DataBaseHelper" // Tag just for the LogCat window
    private var mContext: Context?=context
    private var DB_FILE: File? = context!!.getDatabasePath(DB_NAME)
    private var a=""

    fun open() {
        try {
            openDataBase()
            close()
            val mDb = getReadableDatabase()
        } catch(ex:Exception ){
            Log.e(TAG, "open >>" + ex.toString())

        }
    }
    fun createDataBase() {
        // If the database does not exist, copy it from the assets.
        val mDataBaseExist = checkDataBase()
        if (!mDataBaseExist) {
            this.readableDatabase
            close()
            try {
                // Copy the database from assests
                copyDataBase()
                Log.e(TAG, "createDatabase database created")
            } catch (mIOException: IOException) {
                throw Error("ErrorCopyingDataBase")
            }
        }
    }

    // Check that the database file exists in databases folder
    private fun checkDataBase(): Boolean {
        return DB_FILE!!.exists()
    }

    private fun copyDataBase() {
        val mInput: InputStream = mContext!!.getAssets().open(DB_NAME+".db")
        val mOutput: OutputStream = FileOutputStream(DB_FILE)
        val mBuffer = ByteArray(1024)
        var mLength: Int
        while (mInput.read(mBuffer).also { mLength = it } > 0) {
            mOutput.write(mBuffer, 0, mLength)
        }
        mOutput.flush()
        mOutput.close()
        mInput.close()
    }

    fun openDataBase(): SQLiteDatabase {
        createDataBase()
        // Log.v("DB_PATH", DB_FILE.getAbsolutePath());
        val mDataBase = SQLiteDatabase.openDatabase("Iran",null, SQLiteDatabase.CREATE_IF_NECESSARY)
        // mDataBase = SQLiteDatabase.openDatabase(DB_FILE, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase
    }

    companion object {
        // Table Name
        const val Province_TABLE_NAME = "Province"
        const val City_TABLE_NAME = "City"

        // Table columns
        const val _ID = "Id"
        const val TITLE = "Title"
        const val SORT = "Sort"
        const val PROVINCEID= "ProvinceId"

    }

    override fun onCreate(p0: SQLiteDatabase?) {
        //TODO("Not yet implemented")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        //TODO("Not yet implemented")
    }
}