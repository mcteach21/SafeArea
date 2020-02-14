package mchou.apps.safe.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import java.util.ArrayList


class Dao(context: Context) {
    private var database: SQLiteDatabase? = null
    private val dbHelper: DbHelper = DbHelper(context)

    init {
        open()
    }

    @Throws(SQLException::class)
    fun open() {
        database = dbHelper.writableDatabase
    }

    fun close() {
        dbHelper.close()
    }

    private fun cursorToItem(cursor: Cursor): Item {
        return Item(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4))
    }


    fun all() : List<Item> {
        val items = ArrayList<Item>()
        val cursor = database!!.query(DbHelper.TABLE_NAME, DbHelper.ALL_COLUMNS, null, null, null, null, DbHelper.COLUMN_TEXT_0_NAME)

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val item = cursorToItem(cursor)
            items.add(item)
            cursor.moveToNext()
        }
        cursor.close()
        return items
    }

    fun create(name: String, login: String, password: String, url: String) : Boolean {
        val values = ContentValues()
        values.put(DbHelper.COLUMN_TEXT_0_NAME, name)
        values.put(DbHelper.COLUMN_TEXT_1_NAME, login)
        values.put(DbHelper.COLUMN_TEXT_2_NAME, password)
        values.put(DbHelper.COLUMN_TEXT_3_NAME, url)

        val insertId = database!!.insert(DbHelper.TABLE_NAME, null, values)
        return (insertId>0)
    }

    fun delete(item: Item) {
        val id = item.id
        database!!.delete(DbHelper.TABLE_NAME, " id = $id", null)
    }

    fun update(item: Item) {
        val values = ContentValues()
        values.put(DbHelper.COLUMN_TEXT_0_NAME, item.name)
        values.put(DbHelper.COLUMN_TEXT_1_NAME, item.login)
        values.put(DbHelper.COLUMN_TEXT_2_NAME, item.password)
        values.put(DbHelper.COLUMN_TEXT_3_NAME, item.url)

        database!!.update(DbHelper.TABLE_NAME, values,"id = ?", arrayOf(item.id.toString()))
    }
}
