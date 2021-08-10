package won.young.challengecamera.dbutils.recyclerview.challengelist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import won.young.challengecamera.R
import won.young.challengecamera.dbutils.challengelist.ChallengeListDTO

class ChallengeListRecyclerViewAdapter(val context: Context, val challengeListDtos: ArrayList<ChallengeListDTO>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.challenge_list_recycler_item, parent, false)
        return Holder(view)
    }

    inner class Holder(view: View): RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var view = holder.itemView
        val challengeName = view.findViewById<TextView>(R.id.challengeNameTextView)
        val date = view.findViewById<TextView>(R.id.dateTextView)
        val kind = view.findViewById<TextView>(R.id.kindTextView)

        challengeName.text = challengeListDtos[position].challengeName
        date.text = challengeListDtos[position].date.toString()
        kind.text = challengeListDtos[position].kind.toString()
    }

    override fun getItemCount(): Int {
        return challengeListDtos.size
    }
}