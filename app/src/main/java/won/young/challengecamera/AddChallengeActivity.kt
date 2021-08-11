package won.young.challengecamera

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import won.young.challengecamera.databinding.ActivityAddChallengeBinding
import won.young.challengecamera.dbutils.DBHelper
import won.young.challengecamera.dbutils.challengelist.ChallengeListDAO
import java.io.File
import java.util.*

class AddChallengeActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddChallengeBinding
    lateinit var dbHelper: DBHelper
    lateinit var challengeListDAO: ChallengeListDAO

    var name = ""
    var startDate = 0L
    var endDate = 0L
    var kind = 0
    var isAlarmOn = false
    var isHideOn = false
    var alarmAmPm = false
    var alarmTime = -1
    var alarmMinute = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_challenge)
        dbHelper = DBHelper(this, DATABASE_NAME, null, 1)
        challengeListDAO = ChallengeListDAO(dbHelper.writableDatabase)
        initListener()
    }

    private fun initListener() {
        binding.addChallengeAddButton.setOnClickListener {
            name = binding.addChallengeNameEditText.text.toString()
            if (name.length < 2) {
                Toast.makeText(this, "2글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (challengeListDAO.isDuplicated(name)) {
                Toast.makeText(this, "챌린지명이 중복됩니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (startDate == 0L) {
                Toast.makeText(this, "시작할 날짜를 설정하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (endDate == 0L && kind == 1) {
                Toast.makeText(this, "종료할 날짜를 설정하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//            if (isAlarmOn) {
//                if (alarmTime == -1 || alarmMinute == -1) {
//                    Toast.makeText(this, "알람 시간을 설정하세요.", Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
//            }

            challengeListDAO.insertChallengeList(name, startDate, endDate, kind, isAlarmOn, isHideOn, alarmAmPm, alarmTime, alarmMinute)
            makeDirectory(isHideOn, name)
            finish()
        }

        binding.addChallengeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0->{
                        kind = 0
                    }

                    1->{
                        kind = 1
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.addChallengeStartDateButton.setOnClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
            val nowYear = calendar.get(Calendar.YEAR)
            val nowMonth = calendar.get(Calendar.MONTH)
            val nowDay = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    if (System.currentTimeMillis() > calendar.timeInMillis) {
                        Toast.makeText(this@AddChallengeActivity, "날짜를 다시 설정하세요.", Toast.LENGTH_SHORT).show()
                        return
                    }
                    binding.addChallengeStartDateButton.text = "$year ${month + 1} $dayOfMonth"
                    startDate = calendar.timeInMillis
                }
            },
            nowYear, nowMonth, nowDay).show()
        }

        binding.addChallengeEndDateButton.setOnClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
            val nowYear = calendar.get(Calendar.YEAR)
            val nowMonth = calendar.get(Calendar.MONTH)
            val nowDay = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    if (System.currentTimeMillis() > calendar.timeInMillis) {
                        Toast.makeText(this@AddChallengeActivity, "날짜를 다시 설정하세요.", Toast.LENGTH_SHORT).show()
                        return
                    }
                    binding.addChallengeEndDateButton.text = "$year ${month + 1} $dayOfMonth"
                    endDate = calendar.timeInMillis
                }
            },
                nowYear, nowMonth, nowDay).show()
        }

        binding.addChallengeTimeButton.setOnClickListener {

        }

        binding.addChallengeTimeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            isAlarmOn = isChecked
        }

        binding.addChallengeHideCheckBox.setOnCheckedChangeListener { _, isChecked ->
            isHideOn = isChecked
        }
    }

    private fun makeDirectory(isHideOn: Boolean, dirName: String) {
        var mkName = if (isHideOn) ".$dirName" else "$dirName"

        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + "$APP_NAME" + File.separator + "$mkName"
        val file = File(path)
        if (!file.mkdirs()) {
            Toast.makeText(this, "폴더생성 에러", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "폴더생성 완료", Toast.LENGTH_SHORT).show()
        }
    }
}
