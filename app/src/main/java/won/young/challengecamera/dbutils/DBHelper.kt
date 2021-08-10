package won.young.challengecamera.dbutils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
): SQLiteOpenHelper(context, name, factory, version){
    override fun onCreate(db: SQLiteDatabase) {
        val challengeList = "create table if not exists challengeList("+
                "_id integer primary key autoincrement, " +
                "challengeName text, date text, kind integer);"

        db.execSQL(challengeList)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        var sql = "drop table if exists challengeList;"
        db.execSQL(sql)
    }
}