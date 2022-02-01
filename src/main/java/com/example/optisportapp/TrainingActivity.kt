package com.example.optisportapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class TrainingActivity : AppCompatActivity() {

    private lateinit var profile: Profile

    override fun onBackPressed() {
        intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("extra_profile", profile)
        startActivity(intent)
    }

    private fun popUpCheckTraining(){
        val builder = AlertDialog.Builder(this, R.style.Theme_custom_dialog_alert)
        builder.setCancelable(true)
        builder.setTitle(R.string.check_training)
        val dateString = dateToString(profile.trainingsList[0].date, resources)
        val trainingDetails = resources.getString(profile.trainingsList[0].details)
        val trainingType = profile.trainingsList[0].type
        builder.setMessage("$dateString : $trainingDetails")
        builder.setPositiveButton(R.string.yes, DialogInterface.OnClickListener { _ , _ ->
            profile.trainingsList.removeAt(0)
            profile.nbTrainingsDone++
            when (trainingType) {
                TRAINING_NORMAL -> profile.nbTrainingsNormalDone++
                TRAINING_FRACTIONAL -> profile.nbTrainingsFractionalDone++
                TRAINING_LONG -> profile.nbTrainingsLongDone++
                TRAINING_MUSCULAR -> profile.nbTrainingsMuscularDone++
            }
            saveProfile(profile)
            intent = Intent(this, TrainingActivity::class.java)
            intent.putExtra("extra_profile", profile)
            startActivity(intent)
        })
        builder.setNeutralButton(R.string.remove_training, DialogInterface.OnClickListener { _, _ ->
            profile.trainingsList.removeAt(0)
            profile.nbTrainingsTotal--
            when (trainingType) {
                TRAINING_NORMAL -> profile.nbTrainingsNormalTotal--
                TRAINING_FRACTIONAL -> profile.nbTrainingsFractionalTotal--
                TRAINING_LONG -> profile.nbTrainingsLongTotal--
                TRAINING_MUSCULAR -> profile.nbTrainingsMuscularTotal--
            }
            saveProfile(profile)
            intent = Intent(this, TrainingActivity::class.java)
            intent.putExtra("extra_profile", profile)
            startActivity(intent)
        })
        builder.setNegativeButton(R.string.reschedule, DialogInterface.OnClickListener { _, _ ->
            val oneDayInMilliseconds = 1000 * 60 * 60 * 24
            var day = Calendar.getInstance().time
            while (getTrainingIndex(profile, day) != -1){
                day = Date(day.time + oneDayInMilliseconds)
            }
            profile.trainingsList[0].date = day
            profile.trainingsList.sortBy { it.date }
            saveProfile(profile)
            intent = Intent(this, TrainingActivity::class.java)
            intent.putExtra("extra_profile", profile)
            startActivity(intent)
        })
        builder.create().show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        profile = intent.getSerializableExtra("extra_profile") as Profile
        if (getDaysLeft(profile.trainingsList[0].date) < 0){
            popUpCheckTraining()
        }
        val layoutCalendar = findViewById<LinearLayout>(R.id.linearLayoutCalendar)
        layoutCalendar.setOnClickListener { view ->
            profile.editTraining = false
            intent = Intent(view.context, CalendarActivity::class.java)
            intent.putExtra("extra_profile", profile)
            startActivity(intent)
        }
        val profileGoal = findViewById<TextView>(R.id.textViewProfileGoal)
        profileGoal.text = profile.goalTime
        val profileTrainingsCounter = findViewById<TextView>(R.id.textViewProfileTrainingsCounter)
        val nbTrainingsDone = (profile.nbTrainingsDone).toString()
        val nbTrainingsTotal = (profile.nbTrainingsTotal).toString()
        val trainingsCounter = "$nbTrainingsDone / $nbTrainingsTotal"
        profileTrainingsCounter.text = trainingsCounter

        val profileTrainingsNormalCounter = findViewById<TextView>(R.id.textViewProfileTrainingsNormalCounter)
        val nbTrainingsNormalDone = (profile.nbTrainingsNormalDone).toString()
        val nbTrainingsNormalTotal = (profile.nbTrainingsNormalTotal).toString()
        val trainingsNormalCounter = "$nbTrainingsNormalDone / $nbTrainingsNormalTotal"
        profileTrainingsNormalCounter.text = trainingsNormalCounter

        val profileTrainingsFractionalCounter = findViewById<TextView>(R.id.textViewProfileTrainingsFractionalCounter)
        val nbTrainingsFractionalDone = (profile.nbTrainingsFractionalDone).toString()
        val nbTrainingsFractionalTotal = (profile.nbTrainingsFractionalTotal).toString()
        val trainingsFractionalCounter = "$nbTrainingsFractionalDone / $nbTrainingsFractionalTotal"
        profileTrainingsFractionalCounter.text = trainingsFractionalCounter

        val profileTrainingsLongCounter = findViewById<TextView>(R.id.textViewProfileTrainingsLongCounter)
        val nbTrainingsLongDone = (profile.nbTrainingsLongDone).toString()
        val nbTrainingsLongTotal = (profile.nbTrainingsLongTotal).toString()
        val trainingsLongCounter = "$nbTrainingsLongDone / $nbTrainingsLongTotal"
        profileTrainingsLongCounter.text = trainingsLongCounter

        val profileTrainingsMuscularCounter = findViewById<TextView>(R.id.textViewProfileTrainingsMuscularCounter)
        val nbTrainingsMuscularDone = (profile.nbTrainingsMuscularDone).toString()
        val nbTrainingsMuscularTotal = (profile.nbTrainingsMuscularTotal).toString()
        val trainingsMuscularCounter = "$nbTrainingsMuscularDone / $nbTrainingsMuscularTotal"
        profileTrainingsMuscularCounter.text = trainingsMuscularCounter

        val profileDaysLeft = findViewById<TextView>(R.id.textViewProfileDaysLeft)
        val daysLeft = getDaysLeft(profile.dateMarathon)
        val days = resources.getText(R.string.days)
        profileDaysLeft.text = "$daysLeft $days"

        val progressTrainings = findViewById<ProgressBar>(R.id.progressBarTrainings)
        progressTrainings.max = profile.nbTrainingsTotal
        progressTrainings.progress = profile.nbTrainingsDone

        val randomTip = findViewById<TextView>(R.id.textViewRandomTip)
        val tip = resources.getString(tipsList[(0 until tipsList.size).random()])
        val titleTip = resources.getString(R.string.title_random_tip)
        randomTip.text = "$titleTip$tip"
        randomTip.setOnClickListener { view ->
            intent = Intent(view.context, TipsActivity::class.java)
            intent.putExtra("extra_profile", profile)
            startActivity(intent)
        }

        val dateToday = findViewById<TextView>(R.id.textViewDateToday)
        val trainingToday = findViewById<TextView>(R.id.textViewTrainingToday)
        val trainingTomorrow = findViewById<TextView>(R.id.textViewTrainingTomorrow)
        val dateNext = findViewById<TextView>(R.id.textViewDateNext)
        val trainingNext = findViewById<TextView>(R.id.textViewTrainingNext)
        val oneDayInMilliseconds = 1000 * 60 * 60 * 24
        val tomorrow = Date(Calendar.getInstance().timeInMillis + oneDayInMilliseconds)
        val nextDay = Date(Calendar.getInstance().timeInMillis + oneDayInMilliseconds * 2)
        val today = Calendar.getInstance().time
        dateToday.text = dateToString(today, resources)
        dateNext.text = dateToString(nextDay, resources)
        val dateFormat = SimpleDateFormat("yyyyMMdd")
        var nextTraining = 0
        if (dateFormat.format(today).equals(
                dateFormat.format(profile.trainingsList[nextTraining].date))){
            trainingToday.text = resources.getText(profile.trainingsList[nextTraining].details)
            nextTraining++
        }
        else{
            trainingToday.text = resources.getText(R.string.nothing)
        }
        if (dateFormat.format(tomorrow).equals(
                dateFormat.format(profile.trainingsList[nextTraining].date))){
            trainingTomorrow.text = resources.getText(profile.trainingsList[nextTraining].details)
            nextTraining++
        }
        else{
            trainingTomorrow.text = resources.getText(R.string.nothing)
        }
        if (dateFormat.format(nextDay).equals(
                dateFormat.format(profile.trainingsList[nextTraining].date))){
            trainingNext.text = resources.getText(profile.trainingsList[nextTraining].details)
            nextTraining++
        }
        else{
            trainingNext.text = resources.getText(R.string.nothing)
        }
    }
}