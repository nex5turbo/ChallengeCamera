package won.young.challengecamera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import won.young.challengecamera.databinding.ActivityAddChallengeBinding
import won.young.challengecamera.dbutils.DBHelper
import won.young.challengecamera.dbutils.challengelist.ChallengeListDAO

class AddChallengeActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddChallengeBinding
    lateinit var dbHelper: DBHelper
    lateinit var database: ChallengeListDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_challenge)
        dbHelper = DBHelper(this, DATABASE_NAME, null, 1)
        database = ChallengeListDAO(dbHelper.writableDatabase)
        initListener()
    }

    private fun initListener() {
        binding.addChallengeAddButton.setOnClickListener {
            val name = binding.addChallengeNameEditText.text.toString()
            if (database.isDuplicated(name)) {
                Toast.makeText(this, "챌린지명이 중복됩니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val date = System.currentTimeMillis()
            val kind = 1
            database.insertChallengeList(name, date, kind)
            finish()

        }
    }
}