package com.example.optisportapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.*


class CalendarActivity : AppCompatActivity() {
    private lateinit var profile: Profile
    private var changeDate = false
    private var changeTrainingIndex = 0
    private var nextTraining = 0
    private val dateFormat = SimpleDateFormat("yyyyMMdd")

    override fun onBackPressed() {
        intent = Intent(this, TrainingActivity::class.java)
        intent.putExtra("extra_profile", profile)
        startActivity(intent)
    }

    private fun popUpDate(message: Int, confirm: Int){
        val builder = AlertDialog.Builder(this, R.style.Theme_custom_dialog_alert)
        builder.setCancelable(true)
        builder.setMessage(message)
        builder.setPositiveButton(confirm) { _, _ -> }
        builder.create().show()
    }

    private fun popUpTraining(message: Int, date: Date){
        var lastTrainingType = 0
        if (message == R.string.change_training_type){
            lastTrainingType = profile.trainingsList[getTrainingIndex(profile, date)].type
        }
        val builder = AlertDialog.Builder(this, R.style.Theme_custom_dialog_alert)
        builder.setCancelable(true)
        builder.setTitle(message)
        builder.setItems(
            arrayOf(resources.getString(TRAINING_NORMAL),
                    resources.getString(TRAINING_LONG),
                    resources.getString(TRAINING_FRACTIONAL),
                    resources.getString(TRAINING_MUSCULAR))
        ) { _, which ->
            when (which) {
                0 ->  {
                    if(message == R.string.change_training_type) {
                        profile.trainingsList[getTrainingIndex(profile, date)].type = TRAINING_NORMAL
                        profile.trainingsList[getTrainingIndex(profile, date)].details =
                            trainingListNormal[(0 until trainingListNormal.size).random()]
                        profile.nbTrainingsNormalTotal++
                }
                    else{
                        profile.trainingsList.add(Training(date,
                            TRAINING_NORMAL,
                            trainingListNormal[(0 until trainingListNormal.size).random()]))
                        profile.nbTrainingsNormalTotal++
                    }
                }
                1 -> {
                    if(message == R.string.change_training_type) {
                        profile.trainingsList[getTrainingIndex(profile, date)].type = TRAINING_LONG
                        profile.trainingsList[getTrainingIndex(profile, date)].details =
                            trainingListLong[(0 until trainingListLong.size).random()]
                        profile.nbTrainingsLongTotal++
                    }
                    else{
                        profile.trainingsList.add(Training(date,
                            TRAINING_LONG,
                            trainingListLong[(0 until trainingListLong.size).random()]))
                        profile.nbTrainingsLongTotal++
                    }
                }
                2 -> {
                    if(message == R.string.change_training_type) {
                        profile.trainingsList[getTrainingIndex(profile, date)].type = TRAINING_FRACTIONAL
                        profile.trainingsList[getTrainingIndex(profile, date)].details =
                            trainingListFractional[(0 until trainingListFractional.size).random()]
                        profile.nbTrainingsFractionalTotal++
                    }
                    else{
                        profile.trainingsList.add(Training(date,
                            TRAINING_FRACTIONAL,
                            trainingListFractional[(0 until trainingListFractional.size).random()]))
                        profile.nbTrainingsFractionalTotal++
                    }
                }
                3 -> {
                    if(message == R.string.change_training_type) {
                        profile.trainingsList[getTrainingIndex(profile, date)].type = TRAINING_MUSCULAR
                        profile.trainingsList[getTrainingIndex(profile, date)].details =
                            trainingListMuscular[(0 until trainingListMuscular.size).random()]
                        profile.nbTrainingsMuscularTotal++
                    }
                    else{
                        profile.trainingsList.add(Training(date,
                            TRAINING_MUSCULAR,
                            trainingListMuscular[(0 until trainingListMuscular.size).random()]))
                        profile.nbTrainingsMuscularTotal++
                    }
                }
            }
            when (message) {
                R.string.add_training -> {
                    profile.trainingsList.sortBy { it.date }
                    profile.nbTrainingsTotal++
                }
                R.string.change_training_type -> {
                    when (lastTrainingType) {
                        TRAINING_NORMAL -> profile.nbTrainingsNormalTotal--
                        TRAINING_FRACTIONAL -> profile.nbTrainingsFractionalTotal--
                        TRAINING_LONG -> profile.nbTrainingsLongTotal--
                        TRAINING_MUSCULAR -> profile.nbTrainingsMuscularTotal--
                    }
                }
            }
            saveProfile(profile)
            intent = Intent(this, CalendarActivity::class.java)
            intent.putExtra("extra_profile", profile)
            startActivity(intent)
        }
        builder.create().show()
    }

    private fun addDateTextView(linearLayout: LinearLayout, date:Date){
        val textView = TextView(this)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50)
        textView.text = dateToString(date, resources)
        textView.setTextColor(Color.WHITE)
        textView.setOnClickListener(View.OnClickListener { view ->
            val trainingIndex = getTrainingIndex(profile, date)
            if (profile.editTraining && !changeDate && (trainingIndex >= 0)) {
                changeTrainingIndex = trainingIndex
                changeDate = true
                popUpDate(R.string.change_date_message, R.string.change_date_confirm)
            }
            else if(profile.editTraining && changeDate && (trainingIndex == -1) &&
                !(dateFormat.format(date).equals(dateFormat.format(profile.dateMarathon)))) {
                changeDate = false
                profile.trainingsList[changeTrainingIndex].date = date
                profile.trainingsList.sortBy {  it.date }
                saveProfile(profile)
                intent = Intent(view.context, CalendarActivity::class.java)
                intent.putExtra("extra_profile", profile)
                startActivity(intent)
            }
            else if (profile.editTraining && !changeDate &&
                    !(dateFormat.format(date).equals(dateFormat.format(profile.dateMarathon)))){
                popUpTraining(R.string.add_training, date)
            }
        })
        textView.layoutParams = params
        textView.textSize = 20F
        linearLayout.addView(textView)
    }

    private fun addDetailsTextView(linearLayout: LinearLayout, text: Int, date: Date){
        val textView = TextView(this)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100)
        textView.text = resources.getText(text)
        if (text == R.string.nothing ) {
            textView.setTextColor(Color.GRAY)
        }
        else{
            textView.setTextColor(Color.RED)
        }
        params.setMargins(10, 0, 20, 20)
        textView.setOnClickListener(View.OnClickListener {
            val trainingIndex = getTrainingIndex(profile, date)
            if (profile.editTraining && trainingIndex >= 0) {
                popUpTraining(R.string.change_training_type, date)
            }
            else if (profile.editTraining &&
                !dateFormat.format(date).equals(dateFormat.format(profile.dateMarathon))){
                popUpTraining(R.string.add_training, date)
            }
        })
        textView.layoutParams = params
        textView.textSize = 18F
        linearLayout.addView(textView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        profile = intent.getSerializableExtra("extra_profile") as Profile
        val buttonEdit = findViewById<Button>(R.id.buttonEdit)
        if (profile.editTraining){
            buttonEdit.text = resources.getString(R.string.cancel)
        }
        else {
            buttonEdit.text = resources.getString(R.string.edit)
        }
        buttonEdit.setOnClickListener {
            if (profile.editTraining) {
                profile.editTraining = false
                buttonEdit.text = resources.getString(R.string.edit)
            }
            else {
                profile.editTraining = true
                buttonEdit.text = resources.getString(R.string.cancel)
            }
        }
        val linearLayoutTrainings = findViewById<LinearLayout>(R.id.LinearLayoutTrainings)
        nextTraining = 0
        var dayMilliseconds = Calendar.getInstance().timeInMillis
        var day: Date
        val oneDayInMilliseconds = 1000 * 60 * 60 * 24
        for(days in 0..getDaysLeft(profile.dateMarathon)){
            day = Date(dayMilliseconds)
            addDateTextView(linearLayoutTrainings, day)
            when (dateFormat.format(day).equals(
                dateFormat.format(profile.trainingsList[nextTraining].date))) {
                true -> {
                    addDetailsTextView(linearLayoutTrainings,
                        profile.trainingsList[nextTraining].details,
                        profile.trainingsList[nextTraining].date)
                    if (nextTraining < profile.trainingsList.size - 1){
                        nextTraining++
                    }
                }
                else -> {
                    addDetailsTextView(linearLayoutTrainings, R.string.nothing, day)
                }
            }
            dayMilliseconds += oneDayInMilliseconds
        }
        day = Date(dayMilliseconds)
        addDateTextView(linearLayoutTrainings, day)
        addDetailsTextView(linearLayoutTrainings, R.string.marathon, day)
    }
}
