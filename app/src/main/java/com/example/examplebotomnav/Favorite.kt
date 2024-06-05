package com.example.examplebotomnav

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class Favorite(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DB_VERSION) {

    companion object {
        fun getReadableDatabase() {
        }

        fun read_all_data(keyId: String?) {
        }

        fun insertIntoTheDatabase(title: Any, imageResourse: Int, keyId: Any, favStatus: String) {

        }

        fun remove_fav(keyId: String) {

        }

        fun insertEmpty() {
            TODO("Not yet implemented")
        }

        private const val DB_VERSION = 1
        private const val DATABASE_NAME = "CoffeeDB"
        private const val TABLE_NAME = "favoriteTable"
        const val KEY_ID = "id"
        const val ITEM_TITLE = "itemTitle"
        const val ITEM_IMAGE = "itemImage"
        const val FAVORITE_STATUS = "fStatus"

        private const val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME ($KEY_ID TEXT, $ITEM_TITLE TEXT, $ITEM_IMAGE TEXT, $FAVORITE_STATUS TEXT)"
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        // No implementation needed
    }

    // Create empty table
    fun insertEmpty() {
        val db = writableDatabase
        val cv = ContentValues()
        for (x in 1..10) {
            cv.put(KEY_ID, x)
            cv.put(FAVORITE_STATUS, "0")
            db.insert(TABLE_NAME, null, cv)
        }
    }

    // Insert data into database
    fun insertIntoTheDatabase(itemTitle: String, itemImage: Int, id: String, favStatus: String) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ITEM_TITLE, itemTitle)
        cv.put(ITEM_IMAGE, itemImage)
        cv.put(KEY_ID, id)
        cv.put(FAVORITE_STATUS, favStatus)
        db.insert(TABLE_NAME, null, cv)
        Log.d("FavDB Status", "$itemTitle, favstatus - $favStatus - . $cv")
    }

    // Read all data
    fun readAllData(id: String): Cursor {
        val db = readableDatabase
        val sql = "SELECT * FROM $TABLE_NAME WHERE $KEY_ID=$id"
        return db.rawQuery(sql, null)
    }

    // Remove line from database
    fun removeFav(id: String) {
        val db = writableDatabase
        val sql = "UPDATE $TABLE_NAME SET $FAVORITE_STATUS='0' WHERE $KEY_ID=$id"
        db.execSQL(sql)
        Log.d("remove", id)
    }

    // Select all favorite list
    fun selectAllFavoriteList(): Cursor {
        val db = readableDatabase
        val sql = "SELECT * FROM $TABLE_NAME WHERE $FAVORITE_STATUS='1'"
        return db.rawQuery(sql, null)
    }
}

