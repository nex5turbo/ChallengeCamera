package won.young.challengecamera.dbutils.challengelist

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

const val CHALLENGE_NAME = "challengeName"
const val CHALLENGE_LIST = "challengeList"
const val START_DATE = "startDate"
const val END_DATE = "endDate"
const val KIND = "kind" //0 -> count up, 1 -> count down
const val IS_ALARM_ON = "isAlarmOn"
const val IS_HIDE_ON = "isHideOn" // 0 -> false, 1 -> true
const val ALARM_AM_PM = "alarmAmPm" // 0 -> false(am), 1 -> true(pm)
const val ALARM_TIME = "alarmTime"
const val ALARM_MINUTE = "alarmMinute"

class ChallengeListDAO (private val database: SQLiteDatabase) {

    fun getChallengeList(): ArrayList<ChallengeListDTO>{
        val sql = "select * from $CHALLENGE_LIST"
        val result = database.rawQuery(sql, null)
        var challengeList: ArrayList<ChallengeListDTO> = arrayListOf()
        while (result.moveToNext()) {
            var nowDTO = ChallengeListDTO()
            nowDTO.challengeName = result.getString(result.getColumnIndex(CHALLENGE_NAME))
            nowDTO.startDate = result.getLong(result.getColumnIndex(START_DATE))
            nowDTO.endDate = result.getLong(result.getColumnIndex(END_DATE))
            nowDTO.kind = result.getInt(result.getColumnIndex(KIND))
            nowDTO.isAlarmOn = setBoolean(result.getInt(result.getColumnIndex(IS_ALARM_ON)))
            nowDTO.isHideOn = setBoolean(result.getInt(result.getColumnIndex(IS_HIDE_ON)))
            nowDTO.alarmAmPm = setBoolean(result.getInt(result.getColumnIndex(ALARM_AM_PM)))
            nowDTO.alarmTime = result.getInt(result.getColumnIndex(ALARM_TIME))
            nowDTO.alarmMinute = result.getInt(result.getColumnIndex(ALARM_MINUTE))
            challengeList.add(nowDTO)
        }
        return challengeList
    }

    fun insertChallengeList(
                challengeName: String,
                startDate: Long,
                endDate: Long,
                kind: Int,
                isAlarmOn: Boolean,
                isHideOn: Boolean,
                alarmAmPm: Boolean,
                alarmTime: Int,
                alarmMinute: Int): Boolean{

        val data = ContentValues()
        data.put(CHALLENGE_NAME, challengeName)
        data.put(START_DATE, startDate.toString())
        data.put(END_DATE, endDate.toString())
        data.put(KIND, kind)
        data.put(IS_ALARM_ON, setBinary(isAlarmOn))
        data.put(IS_HIDE_ON, setBinary(isHideOn))
        data.put(ALARM_AM_PM, setBinary(alarmAmPm))
        data.put(ALARM_TIME, alarmTime)
        data.put(ALARM_MINUTE, alarmMinute)

        var result = database.insert(CHALLENGE_LIST, null, data)
        return result != -1L
    }

    fun isDuplicated(name: String): Boolean {
        val sql = "select * from ${CHALLENGE_LIST} where ${CHALLENGE_NAME}=\"${name}\""
        val result = database.rawQuery(sql, null)
        return result.count != 0
    }

    fun deleteChallengeList(challengeName: String): Boolean{
        var whereArgs = arrayOf(challengeName)
        var result = database.delete(CHALLENGE_LIST, "${CHALLENGE_NAME}=?", whereArgs)
        return result != 1
    }

    private fun setBoolean(binary: Int): Boolean = binary == 1
    private fun setBinary(boolean: Boolean): Int = if (boolean) 1 else 0
}