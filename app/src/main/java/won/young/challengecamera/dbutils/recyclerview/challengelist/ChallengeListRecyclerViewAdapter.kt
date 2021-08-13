package won.young.challengecamera.dbutils.recyclerview.challengelist

import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import won.young.challengecamera.APP_NAME
import won.young.challengecamera.R
import won.young.challengecamera.dbutils.challengelist.ChallengeListDAO
import won.young.challengecamera.dbutils.challengelist.ChallengeListDTO
import won.young.challengecamera.utils.DateUtil
import java.io.File

class ChallengeListRecyclerViewAdapter(val database: ChallengeListDAO, val context: Context, val challengeListDtos: ArrayList<ChallengeListDTO>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + APP_NAME + File.separator

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.challenge_list_recycler_item, parent, false)
        return Holder(view)
    }

    inner class Holder(view: View): RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = holder.itemView
        val challengeName = view.findViewById<TextView>(R.id.challengeNameTextView)
        val date = view.findViewById<TextView>(R.id.dateTextView)
        val alarm = view.findViewById<TextView>(R.id.alarmTextView)
        val hide = view.findViewById<TextView>(R.id.hideTextView)

        val isAlarmOn = challengeListDtos[position].isAlarmOn
        val isHideOn = challengeListDtos[position].isHideOn

        challengeName.text = challengeListDtos[position].challengeName
        val startDate = challengeListDtos[position].startDate
        val endDate = challengeListDtos[position].endDate
        val kind = challengeListDtos[position].kind
        val dDays = DateUtil.dateDifference(startDate!!, endDate!!, kind!!)

        if (kind == 1 && dDays == 0) {
            date.text = "D-DAY"
        } else if (kind == 1 && dDays < 0) {
            date.text = "챌린지 종료"
        } else {
            date.text = "D-${dDays + 1}"
        }
        alarm.text = if (isAlarmOn!!) context.getString(R.string.alarmOnMessage) else context.getString(R.string.alarmOffMessage)
        hide.text = if (isHideOn!!) context.getString(R.string.hideOnMessage) else context.getString(R.string.hideOffMessage)

        view.setOnLongClickListener {
            //TODO for delete challenge
            Toast.makeText(context, "$position ${challengeListDtos[position].challengeName} ${challengeListDtos.size}", Toast.LENGTH_SHORT).show()
            setDirectoryEmpty(challengeListDtos[position].challengeName!!, challengeListDtos[position].isHideOn!!)
            database.deleteChallengeList(challengeListDtos[position].challengeName!!)
            challengeListDtos.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, challengeListDtos.size)
            return@setOnLongClickListener true
        }
        view.setOnClickListener{
            Toast.makeText(context, "$position ${challengeListDtos[position].challengeName}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return challengeListDtos.size
    }

    private fun setDirectoryEmpty(name: String, isHideOn: Boolean) {
        val hideName = if (isHideOn) ".$name" else name
        val filePath = path + File.separator + hideName
        val folder = File(filePath)
        val childFileList = folder.listFiles()

        if (folder.exists()) {
            childFileList.forEach { childFile ->
                childFile.delete()
            }
            folder.delete()
        }
    }


}