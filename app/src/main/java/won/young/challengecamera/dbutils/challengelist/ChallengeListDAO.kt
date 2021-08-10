package won.young.challengecamera.dbutils.challengelist

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

const val CHALLENGE_NAME = "challengeName"
const val CHALLENGE_LIST = "challengeList"
const val DATE = "date"
const val KIND = "kind"
class ChallengeListDAO (private val database: SQLiteDatabase) {

    fun getChallengeList(): ArrayList<ChallengeListDTO>{
        val sql = "select * from challengeList"
        val result = database.rawQuery(sql, null)
        var challengeList: ArrayList<ChallengeListDTO> = arrayListOf()
        while (result.moveToNext()) {
            var nowDTO = ChallengeListDTO()
            nowDTO.challengeName = result.getString(result.getColumnIndex(CHALLENGE_NAME))
            nowDTO.date = result.getLong(result.getColumnIndex(DATE))
            nowDTO.kind = result.getInt(result.getColumnIndex(KIND))
            challengeList.add(nowDTO)
        }
        return challengeList
    }

    fun insertChallengeList(challengeName: String, date: Long, kind: Int): Boolean{
        val data = ContentValues()
        data.put(CHALLENGE_NAME, challengeName)
        data.put(DATE, date.toString())
        data.put(KIND, kind)

        var result = database.insert(CHALLENGE_LIST, null, data)
        return result != -1L
    }

    fun deleteChallengeList(challengeName: String): Boolean{
        var whereArgs = arrayOf(challengeName)
        var result = database.delete(CHALLENGE_LIST, "${CHALLENGE_NAME}=?", whereArgs)
        return result != 1
    }
}