

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.journaldev.sqlite.DatabaseHelper
import java.sql.Date
import java.text.SimpleDateFormat

class DBManager(private val context: Context) {
    private var dbHelper: DatabaseHelper? = null
    private var database: SQLiteDatabase? = null
    @Throws(SQLException::class)
    fun open(): DBManager {
        dbHelper = DatabaseHelper(context)

        database = dbHelper!!.writableDatabase
        //dbHelper!!.onUpgrade(database!!,1,2)
        return this
    }

    fun close() {
        dbHelper!!.close()
    }

    fun insert(name: String?, desc: String?,picture:String?,date:java.util.Date?,tag:String?) {
        val contentValue = ContentValues()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        contentValue.put(DatabaseHelper.SUBJECT, name)
        contentValue.put(DatabaseHelper.DESC, desc)
        contentValue.put(DatabaseHelper.PICTURE, picture)
        contentValue.put(DatabaseHelper.DATE, dateFormat.format(date))
        contentValue.put(DatabaseHelper.TAG, tag)

     val res=   database!!.insert(DatabaseHelper.TABLE_NAME, null, contentValue)
    }

    fun fetch(): Cursor? {
        val columns = arrayOf(DatabaseHelper._ID, DatabaseHelper.SUBJECT, DatabaseHelper.DESC,DatabaseHelper.PICTURE, DatabaseHelper.TAG,DatabaseHelper.DATE)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
       val date= java.util.Date(System.currentTimeMillis())
        val cursor =
            database!!.query(DatabaseHelper.TABLE_NAME, columns, "date<'"+dateFormat.format(date)+"'", null, null, null, DatabaseHelper.DATE +" desc")
        cursor?.moveToFirst()
        return cursor
    }
    fun setClicked(_id: Int): Int {
        val contentValues = ContentValues()
        contentValues.put(DatabaseHelper.TAG, "")
        return database!!.update(
            DatabaseHelper.TABLE_NAME,
            contentValues,
            DatabaseHelper._ID + " = " + _id,
            null
        )
    }
    fun update(_id: Long, name: String?, desc: String?): Int {
        val contentValues = ContentValues()
        contentValues.put(DatabaseHelper.SUBJECT, name)
        contentValues.put(DatabaseHelper.DESC, desc)
        return database!!.update(
            DatabaseHelper.TABLE_NAME,
            contentValues,
            DatabaseHelper._ID + " = " + _id,
            null
        )
    }

    fun delete(_id: Long) {
        database!!.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null)
    }
}