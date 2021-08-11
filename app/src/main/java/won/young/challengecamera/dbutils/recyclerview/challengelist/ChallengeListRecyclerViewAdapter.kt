package won.young.challengecamera.dbutils.recyclerview.challengelist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import won.young.challengecamera.R
import won.young.challengecamera.dbutils.challengelist.ChallengeListDAO
import won.young.challengecamera.dbutils.challengelist.ChallengeListDTO

class ChallengeListRecyclerViewAdapter(val database: ChallengeListDAO, val context: Context, val challengeListDtos: ArrayList<ChallengeListDTO>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.challenge_list_recycler_item, parent, false)
        return Holder(view)
    }

    inner class Holder(view: View): RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var view = holder.itemView
        val challengeName = view.findViewById<TextView>(R.id.challengeNameTextView)
        val date = view.findViewById<TextView>(R.id.dateTextView)
        val alarm = view.findViewById<TextView>(R.id.alarmTextView)
        val hide = view.findViewById<TextView>(R.id.hideTextView)

        val isAlarmOn = challengeListDtos[position].isAlarmOn
        val isHideOn = challengeListDtos[position].isHideOn

        challengeName.text = challengeListDtos[position].challengeName
        date.text = challengeListDtos[position].startDate.toString()
        alarm.text = if (isAlarmOn!!) context.getString(R.string.alarmOnMessage) else context.getString(R.string.alarmOffMessage)
        hide.text = if (isHideOn!!) context.getString(R.string.hideOnMessage) else context.getString(R.string.hideOffMessage)

        view.setOnLongClickListener {
            //TODO for delete challenge
            Toast.makeText(context, "$position ${challengeListDtos[position].challengeName} ${challengeListDtos.size}", Toast.LENGTH_SHORT).show()
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


}