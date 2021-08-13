package won.young.challengecamera

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.TimePicker
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
        initStartDate()
    }

    private fun initStartDate() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        val nowYear = calendar.get(Calendar.YEAR)
        val nowMonth = calendar.get(Calendar.MONTH)
        val nowDay = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.set(Calendar.YEAR, nowYear)
        calendar.set(Calendar.MONTH, nowMonth)
        calendar.set(Calendar.DAY_OF_MONTH, nowDay)

        binding.addChallengeStartDateButton.text = "$nowYear ${nowMonth + 1} $nowDay"
        startDate = calendar.timeInMillis
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

            if (isAlarmOn) {
                if (alarmTime == -1 || alarmMinute == -1) {
                    Toast.makeText(this, "알람 시간을 설정하세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

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
                        binding.addChallengeEndDateButton.isEnabled = false
                    }

                    1->{
                        kind = 1
                        binding.addChallengeEndDateButton.isEnabled = true
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

                    binding.addChallengeEndDateButton.text = "$year ${month + 1} $dayOfMonth"
                    endDate = calendar.timeInMillis
                }
            },
                nowYear, nowMonth, nowDay).show()
        }

        binding.addChallengeTimeButton.setOnClickListener {
            TimePickerDialog(this, object: TimePickerDialog.OnTimeSetListener{
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    alarmAmPm = hourOfDay >= 11
                    alarmTime = if (hourOfDay > 12) hourOfDay-12 else hourOfDay
                    alarmMinute = minute

                    val amPmText = if (alarmAmPm) "오후" else "오전"
                    val timeText = if (alarmTime < 10 && alarmTime > 0) "0$alarmTime" else if (alarmTime == 0) "12" else alarmTime.toString()
                    val minuteText = if (alarmMinute < 10) "0$alarmMinute" else "${alarmMinute}"

                    binding.addChallengeTimeButton.text = "$amPmText $timeText : $minuteText"
                }

            }, 0, 0, false).show()
        }

        binding.addChallengeTimeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            isAlarmOn = isChecked
            binding.addChallengeTimeButton.isEnabled = isAlarmOn
        }

        binding.addChallengeHideCheckBox.setOnCheckedChangeListener { _, isChecked ->
            isHideOn = isChecked
        }
    }

    private fun makeDirectory(isHideOn: Boolean, dirName: String) {
        val mkName = if (isHideOn) ".$dirName" else dirName

        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + APP_NAME + File.separator + mkName
        val file = File(path)
        if (!file.mkdirs()) {
            Toast.makeText(this, "폴더생성 에러", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "폴더생성 완료", Toast.LENGTH_SHORT).show()
        }
    }
}
