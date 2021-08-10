package won.young.challengecamera

import android.content.Intent
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
    lateinit var dbHelper: DBHelper
    lateinit var database: ChallengeListDAO
    lateinit var challengeListDTOs: ArrayList<ChallengeListDTO>
    lateinit var adapter: ChallengeListRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("###", "onCreate()")
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        dbHelper = DBHelper(this, DATABASE_NAME, null, 1)
        database = ChallengeListDAO(dbHelper.writableDatabase)


        binding.addChallengeListButton.setOnClickListener {
            val addIntent = Intent(this, AddChallengeActivity::class.java)
            startActivity(addIntent)
        }
    }

    override fun onResume() {
        Log.d("###", "onResume()")
        challengeListDTOs = database.getChallengeList()
        adapter = ChallengeListRecyclerViewAdapter(this, challengeListDTOs)
        binding.challengeListRecyclerView.adapter = adapter
        binding.challengeListRecyclerView.layoutManager = LinearLayoutManager(this)
        Log.d("###size", challengeListDTOs.size.toString())
        super.onResume()
    }

    override fun onPause() {
        Log.d("###", "onPause()")
        super.onPause()
    }

    override fun onStart() {
        Log.d("###", "onStart()")
        super.onStart()
    }

    override fun onStop() {
        Log.d("###", "onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("###", "onDestroy()")
        super.onDestroy()
    }

    override fun onRestart() {
        super.onRestart()
    }
}