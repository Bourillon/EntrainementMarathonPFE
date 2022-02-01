package com.example.optisportapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ProfileActivity : AppCompatActivity() {

    private lateinit var profile: Profile
    private var editMode = false
    private var estimatedHours = 4
    private var estimatedMinutes = 1
    private var estimatedSeconds = 0

    private lateinit var tflite: Interpreter
    private var inputVal = FloatArray(15)
    private var output = Array(1) {
        FloatArray(
            1
        )
    }

    override fun onBackPressed() {
        intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("extra_profile", profile)
        startActivity(intent)
    }

    @Throws(IOException::class)
    private fun loadModelFile(): MappedByteBuffer? {
        val fileDescriptor = this.assets.openFd("modelFinal.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel: FileChannel = inputStream.getChannel()
        val startOffset = fileDescriptor.startOffset
        val declareLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declareLength)
    }

    private fun runAlgo(){
        inputVal[0] = profile.nbMarathonsDone.toFloat()
        inputVal[1] = profile.gender.toFloat()
        inputVal[2] = profile.height.toFloat()
        inputVal[3] = profile.weight.toFloat()
        inputVal[4] = profile.imc.toFloat()
        inputVal[5] = profile.age.toFloat()
        inputVal[6] = profile.nbTrainingsPerWeek.toFloat()
        inputVal[7] = profile.eat.toFloat()
        inputVal[8] = profile.nbFractionatePerWeek.toFloat()
        inputVal[9] = profile.nbLongPerWeek.toFloat()
        inputVal[10] = profile.trainingDuration.toFloat()
        inputVal[11] = profile.muscular.toFloat()
        inputVal[12] = profile.activities.toFloat()
        inputVal[13] = profile.eatBefore.toFloat()
        inputVal[14] = profile.experience.toFloat()
        if (tflite != null) {
            tflite.run(inputVal, output)
        }
        estimatedSeconds = output[0][0].toInt()
    }

    private fun convertEstimatedTime(): String {
        estimatedHours = estimatedSeconds / 3600
        estimatedMinutes = ((estimatedSeconds % 3600)/60)
        return if (estimatedMinutes < 10) {
            "$estimatedHours'0$estimatedMinutes"
        } else {
            "$estimatedHours'$estimatedMinutes"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        profile = intent.getSerializableExtra("extra_profile") as Profile
        val profileGoal = findViewById<TextView>(R.id.textViewProfileGoalProfile)
        tflite = loadModelFile()?.let { Interpreter(it.asReadOnlyBuffer()) }!!
        runAlgo()
        profileGoal.text = convertEstimatedTime()

        val activitiesChoiceList = arrayOf(resources.getString(R.string.activities_choice1),
            resources.getString(R.string.activities_choice2),
            resources.getString(R.string.activities_choice3),
            resources.getString(R.string.activities_choice4),
            resources.getString(R.string.activities_choice5),
            resources.getString(R.string.activities_choice6))

        val muscularChoiceList = arrayOf(resources.getString(R.string.muscular_choice1),
            resources.getString(R.string.muscular_choice2),
            resources.getString(R.string.muscular_choice3),
            resources.getString(R.string.muscular_choice4))

        val experienceChoiceList = arrayOf(resources.getString(R.string.experience_choice1),
            resources.getString(R.string.experience_choice2),
            resources.getString(R.string.experience_choice3),
            resources.getString(R.string.experience_choice4),
            resources.getString(R.string.experience_choice5),
            resources.getString(R.string.experience_choice6))

        val numberPickerAge = findViewById<NumberPicker>(R.id.numberPickerProfileAge)
        numberPickerAge.run {
            minValue = profile.age
            maxValue = profile.age
            value = profile.age
        }
        numberPickerAge.setOnValueChangedListener { _, _, new ->
            profile.age = new
            runAlgo()
            profile.goalTime = convertEstimatedTime()
            profileGoal.text = profile.goalTime
        }
        val numberPickerHeight = findViewById<NumberPicker>(R.id.numberPickerProfileHeight)
        numberPickerHeight.run {
            minValue = profile.height
            maxValue = profile.height
            value = profile.height
        }
        numberPickerHeight.setOnValueChangedListener { _, _, new ->
            profile.height = new
            runAlgo()
            profile.goalTime = convertEstimatedTime()
            profileGoal.text = profile.goalTime
        }
        val numberPickerWeight = findViewById<NumberPicker>(R.id.numberPickerProfileWeight)
        numberPickerWeight.run {
            minValue = profile.weight
            maxValue = profile.weight
            value = profile.weight
        }
        numberPickerWeight.setOnValueChangedListener { _, _, new ->
            profile.weight = new
            runAlgo()
            profile.goalTime = convertEstimatedTime()
            profileGoal.text = profile.goalTime
        }
        val numberPickerTrainingWeek = findViewById<NumberPicker>(R.id.numberPickerProfileTrainingsWeek)
        numberPickerTrainingWeek.run {
            minValue = profile.nbTrainingsPerWeek
            maxValue = profile.nbTrainingsPerWeek
            value = profile.nbTrainingsPerWeek
        }
        numberPickerTrainingWeek.setOnValueChangedListener { _, _, new ->
            profile.nbTrainingsPerWeek = new
            runAlgo()
            profile.goalTime = convertEstimatedTime()
            profileGoal.text = profile.goalTime
        }
        val numberPickerActivities = findViewById<NumberPicker>(R.id.numberPickerProfileAtivities)
        numberPickerActivities.run {
            minValue = profile.activities
            maxValue = profile.activities
            value = profile.activities
            numberPickerActivities.displayedValues = arrayOf(activitiesChoiceList[profile.activities - 1])
        }
        numberPickerActivities.setOnValueChangedListener { _, _, new ->
            profile.activities = new
            runAlgo()
            profile.goalTime = convertEstimatedTime()
            profileGoal.text = profile.goalTime
        }
        val numberPickerMuscular = findViewById<NumberPicker>(R.id.numberPickerProfileMuscular)
        numberPickerMuscular.run {
            minValue = profile.muscular
            maxValue = profile.muscular
            value = profile.muscular
            numberPickerMuscular.displayedValues = arrayOf(muscularChoiceList[profile.muscular - 1])

        }
        numberPickerMuscular.setOnValueChangedListener { _, _, new ->
            profile.muscular = new
            runAlgo()
            profile.goalTime = convertEstimatedTime()
            profileGoal.text = profile.goalTime
        }
        val numberPickerMarathonsDone = findViewById<NumberPicker>(R.id.numberPickerProfileMarathonsDone)
        numberPickerMarathonsDone.run {
            minValue = profile.nbMarathonsDone
            maxValue = profile.nbMarathonsDone
            value = profile.nbMarathonsDone
        }
        numberPickerMarathonsDone.setOnValueChangedListener { _, _, new ->
            profile.nbMarathonsDone = new
            runAlgo()
            profile.goalTime = convertEstimatedTime()
            profileGoal.text = profile.goalTime
        }
        val numberPickerExperience = findViewById<NumberPicker>(R.id.numberPickerProfileExperience)
        numberPickerExperience.run {
            minValue = profile.experience
            maxValue = profile.experience
            value = profile.experience
            numberPickerExperience.displayedValues = arrayOf(experienceChoiceList[profile.experience - 1])
        }
        numberPickerExperience.setOnValueChangedListener { _, _, new ->
            profile.experience = new
            runAlgo()
            profile.goalTime = convertEstimatedTime()
            profileGoal.text = profile.goalTime
        }
        val buttonChangeProfileInfos = findViewById<Button>(R.id.buttonChangeProfileInfos)
        buttonChangeProfileInfos.setOnClickListener { view ->
            if (editMode) {
                saveProfile(profile)
                buttonChangeProfileInfos.text = resources.getString(R.string.edit)
                editMode = false
                numberPickerAge.minValue = profile.age
                numberPickerAge.maxValue = profile.age
                numberPickerHeight.minValue = profile.height
                numberPickerHeight.maxValue = profile.height
                numberPickerWeight.minValue = profile.weight
                numberPickerWeight.maxValue = profile.weight
                numberPickerTrainingWeek.minValue = profile.nbTrainingsPerWeek
                numberPickerTrainingWeek.maxValue = profile.nbTrainingsPerWeek
                numberPickerActivities.minValue = profile.activities
                numberPickerActivities.maxValue = profile.activities
                numberPickerActivities.displayedValues = arrayOf(activitiesChoiceList[profile.activities - 1])
                numberPickerMuscular.minValue = profile.muscular
                numberPickerMuscular.maxValue = profile.muscular
                numberPickerMuscular.displayedValues = arrayOf(muscularChoiceList[profile.muscular - 1])
                numberPickerMarathonsDone.minValue = profile.nbMarathonsDone
                numberPickerMarathonsDone.maxValue = profile.nbMarathonsDone
                numberPickerExperience.minValue = profile.experience
                numberPickerExperience.maxValue = profile.experience
                numberPickerExperience.displayedValues = arrayOf(experienceChoiceList[profile.experience - 1])
            }
            else {
                buttonChangeProfileInfos.text = resources.getString(R.string.button_save)
                editMode = true
                numberPickerAge.minValue = 18
                numberPickerAge.maxValue = 80
                numberPickerHeight.minValue = 100
                numberPickerHeight.maxValue = 200
                numberPickerWeight.minValue = 30
                numberPickerWeight.maxValue = 130
                numberPickerTrainingWeek.minValue = 1
                numberPickerTrainingWeek.maxValue = 7
                numberPickerActivities.displayedValues = activitiesChoiceList
                numberPickerActivities.minValue = 1
                numberPickerActivities.maxValue = 6
                numberPickerMuscular.displayedValues = muscularChoiceList
                numberPickerMuscular.minValue = 1
                numberPickerMuscular.maxValue = 4
                numberPickerMarathonsDone.minValue = 0
                numberPickerMarathonsDone.maxValue = 4
                numberPickerExperience.displayedValues = experienceChoiceList
                numberPickerExperience.minValue = 1
                numberPickerExperience.maxValue = 6
            }
        }
    }
}