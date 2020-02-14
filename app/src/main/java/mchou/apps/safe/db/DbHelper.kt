package mchou.apps.safe.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

 class DbHelper(context: Context?): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLES)
    }

    override fun onUpgrade(db:SQLiteDatabase, old_version:Int, new_version:Int) {
        db.execSQL(SQL_DELETE_TABLES)
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "mc3.db"

        const val TABLE_NAME = "links"

        const val COLUMN_ID_NAME = "id"
        const val COLUMN_TEXT_0_NAME = "name"
        const val COLUMN_TEXT_1_NAME = "login"
        const val COLUMN_TEXT_2_NAME = "paswword"
        const val COLUMN_TEXT_3_NAME = "url"

        public val ALL_COLUMNS = arrayOf(COLUMN_ID_NAME, COLUMN_TEXT_0_NAME, COLUMN_TEXT_1_NAME, COLUMN_TEXT_2_NAME, COLUMN_TEXT_3_NAME)

        private const val SQL_CREATE_TABLES = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID_NAME + " INTEGER PRIMARY KEY," +
            COLUMN_TEXT_0_NAME + " TEXT," +
            COLUMN_TEXT_1_NAME + " TEXT," +
            COLUMN_TEXT_2_NAME + " TEXT," +
            COLUMN_TEXT_3_NAME + " TEXT)"

        private const val SQL_DELETE_TABLES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
