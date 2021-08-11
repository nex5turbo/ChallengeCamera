package won.young.challengecamera

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import won.young.challengecamera.databinding.ActivityMainBinding
import won.young.challengecamera.dbutils.DBHelper
import won.young.challengecamera.dbutils.challengelist.ChallengeListDAO
import won.young.challengecamera.dbutils.challengelist.ChallengeListDTO
import won.young.challengecamera.dbutils.recyclerview.challengelist.ChallengeListRecyclerViewAdapter
import java.io.File


const val DATABASE_NAME = "challenge.db"
const val APP_NAME = "ChallengeCamera"

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

        checkDirectory()

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
        adapter = ChallengeListRecyclerViewAdapter(database,this, challengeListDTOs)
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

    private fun checkDirectory(){
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + "$APP_NAME" + File.separator
        val file = File(path)
        if (file.exists()) return

        if (!file.mkdirs()) {
            Toast.makeText(this, "앱 폴더 생성에 에러가 발생했습니다. 앱을 재시작해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}