
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.journaldev.sqlite.CitiesDatabaseHelper

class Cities(private val context: Context) {
    private var dbHelper: CitiesDatabaseHelper? = null
    private var database: SQLiteDatabase? = null
    @Throws(SQLException::class)
    fun open(): Cities {
        dbHelper = CitiesDatabaseHelper(context)
        dbHelper!!.createDataBase()

        database = dbHelper!!.readableDatabase

        return this
    }

    fun close() {
        dbHelper!!.close()
    }

    fun fetchProvinces(): Cursor? {
        val columns = arrayOf(CitiesDatabaseHelper._ID, CitiesDatabaseHelper.TITLE)
        val cursor =
            database!!.query(CitiesDatabaseHelper.Province_TABLE_NAME, columns, null, null, null, null,CitiesDatabaseHelper.SORT)
        cursor?.moveToFirst()
        return cursor
    }
    fun fetchCities(provinceId:Int): Cursor? {
        val columns = arrayOf(CitiesDatabaseHelper._ID, CitiesDatabaseHelper.TITLE)
        val cursor =
            database!!.query(CitiesDatabaseHelper.City_TABLE_NAME, columns, "ProvinceId="+provinceId, null, null, null,"Title" )
        cursor?.moveToFirst()
        return cursor
    }

}