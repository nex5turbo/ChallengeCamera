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
        val challengeList = "create table if not exists challengeList(" +
                "challengeName text primary key, startDate text, endDate text, kind integer," +
                "isAlarmOn integer, isHideOn integer, alarmAmPm integer, alarmTime integer, alarmMinute integer);"
        val challengeItem = "create table if not exists challengeItem(" +
                "_id integer auto increment primary key, challengeName text, " +
                "imageName text, day integer, diary text);"
        db.execSQL(challengeList)
        db.execSQL(challengeItem)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        var sql = "drop table if exists challengeList;"
        db.execSQL(sql)
        sql = "drop table if exists challengeName;"
        db.execSQL(sql)
    }
}