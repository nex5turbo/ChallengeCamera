package won.young.challengecamera.dbutils.challengeitem

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import won.young.challengecamera.dbutils.challengelist.CHALLENGE_NAME

const val CHALLENGE_ITEM = "challengeItem"
const val DAY = "day"
const val DIARY = "diary"
const val IMAGE_NAME = "imageName"
class ChallengeItemDAO(private val database: SQLiteDatabase) {
    fun getChallengeItems(name: String): ArrayList<ChallengeItemDTO>{
        val returnDTO = ArrayList<ChallengeItemDTO>()
        val sql = "select * from $CHALLENGE_ITEM where ${CHALLENGE_NAME}=name;"
        val result = database.rawQuery(sql, null)
        while (result.moveToNext()) {
            val nowDTO = ChallengeItemDTO()
            nowDTO.challengeName = name
            nowDTO.imageName = result.getString(result.getColumnIndex(IMAGE_NAME))
            nowDTO.day = result.getInt(result.getColumnIndex(DAY))
            nowDTO.diary = result.getString(result.getColumnIndex(DIARY))
            returnDTO.add(nowDTO)
        }
        return returnDTO
    }

    fun insertChallengeItem(insertDTO: ChallengeItemDTO): Boolean{
        val data = ContentValues()
        data.put(CHALLENGE_NAME, insertDTO.challengeName)
        data.put(IMAGE_NAME, insertDTO.imageName)
        data.put(DIARY, insertDTO.diary)
        data.put(DAY, insertDTO.day)

        val result = database.insert(CHALLENGE_ITEM, null, data)
        return result != -1L
    }
}