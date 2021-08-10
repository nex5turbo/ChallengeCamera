package won.young.challengecamera

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import won.young.challengecamera.databinding.ActivityMainBinding
import won.young.challengecamera.dbutils.DBHelper
import won.young.challengecamera.dbutils.challengelist.ChallengeListDAO
import won.young.challengecamera.dbutils.challengelist.ChallengeListDTO
import won.young.challengecamera.dbutils.recyclerview.challengelist.ChallengeListRecyclerViewAdapter

const val DATABASE_NAME = "challenge.db"

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dbHelper = DBHelper(this, DATABASE_NAME, null, 1)
        val database = ChallengeListDAO(dbHelper.writableDatabase)
        var challengeListDTOs = database.getChallengeList()


        var adapter = ChallengeListRecyclerViewAdapter(this, challengeListDTOs)
        binding.challengeListRecyclerView.adapter = adapter
        binding.challengeListRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.addChallengeListButton.setOnClickListener {
            val name = System.currentTimeMillis().toString()
            val date = name.toLong()
            val kind = 1
            val result = database.insertChallengeList(name, date, kind)
            challengeListDTOs = database.getChallengeList()
            adapter = ChallengeListRecyclerViewAdapter(this, challengeListDTOs)
            binding.challengeListRecyclerView.adapter
        }

    }
}