package com.example.optisportapp

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.*
import androidx.appcompat.app.AlertDialog
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.io.Serializable
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*

class QuestionsActivity : AppCompatActivity(), Serializable {
    private lateinit var profile: Profile
    private val nbQuestion = 9
    private var questionCounter = -5
    private var lastQuestion = -5
    private var estimatedHours = 4
    private var estimatedMinutes = 1
    private var estimatedSeconds = 0
    private val STILL_VISIBLE = 0
    private val TO_VISIBLE = 2
    private val TO_INVISIBLE = 3
    private val MIN_DAYS_TRAINING = 30
    private var changingScreen = false
    private var popUpDisplayed = false

    private lateinit var tflite: Interpreter
    private var inputVal = FloatArray(15)
    private var output = Array(1) {
        FloatArray(
            1
        )
    }

    private val textBeforeQuestionsList = arrayListOf<Int>()
    private lateinit var textBeforeQuestions: TextView
    private lateinit var textEstimatedTime:   TextView
    private lateinit var estimatedTime:       TextView
    private lateinit var textQuestion:        TextView
    private lateinit var barTimeEstimated :   ProgressBar
    private lateinit var radioGroup:          RadioGroup
    private lateinit var radioButton1:        RadioButton
    private lateinit var radioButton2:        RadioButton
    private lateinit var radioButton3:        RadioButton
    private lateinit var radioButton4:        RadioButton
    private lateinit var radioButton5:        RadioButton
    private lateinit var radioButton6:        RadioButton
    private lateinit var radioButton7:        RadioButton
    private lateinit var checkBox1:           CheckBox
    private lateinit var checkBox2:           CheckBox
    private lateinit var checkBox3:           CheckBox
    private lateinit var checkBox4:           CheckBox
    private lateinit var checkBox5:           CheckBox
    private lateinit var checkBox6:           CheckBox
    private lateinit var checkBox7:           CheckBox
    private lateinit var actualCounter:       TextView
    private lateinit var totalCounter:        TextView
    private lateinit var delimiterCounter:   TextView
    private lateinit var buttonNext:          Button
    private lateinit var buttonLast:          Button
    private lateinit var datePickerMarathon:  DatePicker
    private lateinit var numberPickerMeter:       NumberPicker
    private lateinit var numberPickerCentimeter:  NumberPicker
    private lateinit var numberPickerWeight:      NumberPicker
    private lateinit var numberPickerAge:      NumberPicker


    override fun onBackPressed() {
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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

    private fun setEstimatedTime(answer: Int) {
        when (questionCounter) {
            1 -> when (answer) {
                R.id.radioButton1 -> {
                    profile.gender = 1
                }
                R.id.radioButton2 -> {
                    profile.gender = 0
                }
            }
            2 -> profile.age = answer
            3 -> profile.height = answer
            4 -> profile.weight = answer
            5 -> when (answer) {
                R.id.radioButton1 -> {
                    profile.experience = 1
                }
                R.id.radioButton2 -> {
                    profile.experience = 2
                }
                R.id.radioButton3 -> {
                    profile.experience = 3
                }
                R.id.radioButton4 -> {
                    profile.experience = 4
                }
                R.id.radioButton5 -> {
                    profile.experience = 5
                }
                R.id.radioButton6 -> {
                    profile.experience = 6
                }
            }
            6 -> when (answer) {
                R.id.radioButton1 -> {
                    profile.nbMarathonsDone = 0
                }
                R.id.radioButton2 -> {
                    profile.nbMarathonsDone = 1
                }
                R.id.radioButton3 -> {
                    profile.nbMarathonsDone = 2
                }
                R.id.radioButton4 -> {
                    profile.nbMarathonsDone = 3
                }
            }
            7 -> when (answer) {
                R.id.radioButton1 -> {
                    profile.nbTrainingsPerWeek = 1
                    profile.nbFootingPerWeek = 1
                }
                R.id.radioButton2 -> {
                    profile.nbTrainingsPerWeek = 2
                    profile.nbFootingPerWeek = 1
                    profile.nbFractionatePerWeek= 1
                }
                R.id.radioButton3 -> {
                    profile.nbTrainingsPerWeek = 3
                    profile.nbFootingPerWeek = 1
                    profile.nbFractionatePerWeek = 1
                    profile.nbLongPerWeek = 1
                }
                R.id.radioButton4 -> {
                    if (!popUpDisplayed){
                        popUp(R.string.warning_message, R.string.warning_confirm)
                    }
                    profile.nbTrainingsPerWeek = 4
                    profile.nbFootingPerWeek = 2
                    profile.nbFractionatePerWeek = 1
                    profile.nbLongPerWeek = 1
                }
                R.id.radioButton5 -> {
                    if (!popUpDisplayed){
                        popUp(R.string.warning_message, R.string.warning_confirm)
                    }
                    profile.nbTrainingsPerWeek = 5
                    profile.nbFootingPerWeek = 2
                    profile.nbFractionatePerWeek = 1
                    profile.nbLongPerWeek = 1
                    profile.nbMuscularPerWeek = 1
                }
                R.id.radioButton6 -> {
                    if (!popUpDisplayed){
                        popUp(R.string.warning_message, R.string.warning_confirm)
                    }
                    profile.nbTrainingsPerWeek = 6
                    profile.nbFootingPerWeek = 3
                    profile.nbFractionatePerWeek = 1
                    profile.nbLongPerWeek = 1
                    profile.nbMuscularPerWeek = 1
                }
                R.id.radioButton7 -> {
                    if (!popUpDisplayed){
                        popUp(R.string.warning_message, R.string.warning_confirm)
                    }
                    profile.nbTrainingsPerWeek = 7
                    profile.nbFootingPerWeek = 3
                    profile.nbFractionatePerWeek = 2
                    profile.nbLongPerWeek = 1
                    profile.nbMuscularPerWeek = 1
                }
            }
        }
        runAlgo()
        profile.goalTime = convertEstimatedTime()
        if (questionCounter in 2..4){
            estimatedTime.text = profile.goalTime
            barTimeEstimated.progress = estimatedSeconds
        }
        else if (questionCounter != 1){
            fadeAnimation(estimatedTime,    R.string.empty_text, STILL_VISIBLE)
            fadeAnimation(barTimeEstimated, R.string.empty_text,      STILL_VISIBLE)
        }
    }

    private fun changeScreen() {
        radioGroup.clearCheck()
        when (questionCounter) {
            -5 -> {
                fadeAnimation(buttonNext, R.string.buttonNext, TO_VISIBLE)
                fadeAnimation(
                    textQuestion,
                    textBeforeQuestionsList[questionCounter + textBeforeQuestionsList.size - 1],
                    TO_VISIBLE
                )
                fadeAnimation(radioButton1, R.string.question0_answer1, TO_VISIBLE)
                fadeAnimation(radioButton2, R.string.question0_answer2, TO_VISIBLE)
            }
            -4 -> {
                if (lastQuestion == (questionCounter - 1)){
                    fadeAnimation(
                        textBeforeQuestions,
                        textBeforeQuestionsList[questionCounter + textBeforeQuestionsList.size - 1],
                        TO_VISIBLE
                    )
                    fadeAnimation(buttonNext, R.string.buttonNext, TO_VISIBLE)
                }
                else {
                    fadeAnimation(
                        textBeforeQuestions,
                        textBeforeQuestionsList[questionCounter + textBeforeQuestionsList.size - 1],
                        STILL_VISIBLE
                    )
                    fadeAnimation(buttonLast, R.string.buttonLast, TO_INVISIBLE)
                }
            }
            -3 -> {
                if (lastQuestion == (questionCounter - 1)){
                    fadeAnimation(
                        textBeforeQuestions,
                        textBeforeQuestionsList[questionCounter + textBeforeQuestionsList.size - 1],
                        STILL_VISIBLE
                    )
                    fadeAnimation(buttonLast, R.string.buttonLast, TO_VISIBLE)
                }
                else {
                    fadeAnimation(
                        textBeforeQuestions,
                        textBeforeQuestionsList[questionCounter + textBeforeQuestionsList.size - 1],
                        STILL_VISIBLE
                    )
                }
            }
            -2 -> {
                fadeAnimation(
                    textBeforeQuestions,
                    textBeforeQuestionsList[questionCounter + textBeforeQuestionsList.size - 1],
                    STILL_VISIBLE
                )
            }
            -1 -> {
                if (lastQuestion == (questionCounter - 1)){
                    fadeAnimation(
                        textBeforeQuestions,
                        textBeforeQuestionsList[questionCounter + textBeforeQuestionsList.size - 1],
                        STILL_VISIBLE
                    )
                }
                else {
                    fadeAnimation(
                        textBeforeQuestions,
                        textBeforeQuestionsList[questionCounter + textBeforeQuestionsList.size - 1],
                        STILL_VISIBLE
                    )
                    fadeAnimation(buttonNext, R.string.buttonNext, STILL_VISIBLE)
                }
            }
            0 -> {
                fadeAnimation(
                    textBeforeQuestions,
                    textBeforeQuestionsList[questionCounter + textBeforeQuestionsList.size - 1],
                    STILL_VISIBLE
                )
                fadeAnimation(buttonNext, R.string.start_questions, STILL_VISIBLE)
            }
            1 -> {
                if (lastQuestion == (questionCounter - 1)){
                    fadeAnimation(textBeforeQuestions, R.string.empty_text,        TO_INVISIBLE)
                    fadeAnimation(textEstimatedTime,   R.string.estimation_text,   TO_VISIBLE)
                    fadeAnimation(estimatedTime,       R.string.empty_text,   TO_VISIBLE)
                    fadeAnimation(barTimeEstimated,    R.string.empty_text,        TO_VISIBLE)
                    fadeAnimation(textQuestion,        R.string.question1,         TO_VISIBLE)
                    fadeAnimation(radioButton1,        R.string.question1_answer1, TO_VISIBLE)
                    fadeAnimation(radioButton2,        R.string.question1_answer2, TO_VISIBLE)
                    fadeAnimation(actualCounter,       R.string.empty_text,        TO_VISIBLE)
                    fadeAnimation(delimiterCounter,    R.string.counter_delimiter, TO_VISIBLE)
                    fadeAnimation(totalCounter,        R.string.empty_text,        TO_VISIBLE)
                    fadeAnimation(buttonNext, R.string.buttonNext, STILL_VISIBLE)
                }
                else{
                    fadeAnimation(textQuestion,  R.string.question1,    STILL_VISIBLE)
                    fadeAnimation(numberPickerAge, R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(radioButton1,  R.string.question1_answer1, TO_VISIBLE)
                    fadeAnimation(radioButton2,  R.string.question1_answer2, TO_VISIBLE)
                    fadeAnimation(actualCounter, R.string.empty_text,    STILL_VISIBLE)
                }
                fadeAnimation(buttonLast, R.string.empty_text, TO_INVISIBLE)
            }
            2 -> {
                if (lastQuestion == (questionCounter - 1)){
                    fadeAnimation(radioButton1,        R.string.question1_answer1, TO_INVISIBLE)
                    fadeAnimation(radioButton2,        R.string.question1_answer2, TO_INVISIBLE)
                    fadeAnimation(numberPickerAge,      R.string.empty_text, TO_VISIBLE)
                    fadeAnimation(buttonLast,   R.string.buttonLast,        TO_VISIBLE)
                }
                else{
                    fadeAnimation(numberPickerAge,      R.string.empty_text, TO_VISIBLE)
                    fadeAnimation(numberPickerMeter,      R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(numberPickerCentimeter, R.string.empty_text, TO_INVISIBLE)
                }
                fadeAnimation(textQuestion,        R.string.question2,         STILL_VISIBLE)
                fadeAnimation(actualCounter,       R.string.empty_text,        STILL_VISIBLE)
            }
            3 -> {
                if (lastQuestion == (questionCounter - 1)){
                    fadeAnimation(numberPickerAge, R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(numberPickerMeter,      R.string.empty_text, TO_VISIBLE)
                    fadeAnimation(numberPickerCentimeter, R.string.empty_text, TO_VISIBLE)
                }
                else{
                    fadeAnimation(numberPickerMeter,      R.string.empty_text, TO_VISIBLE)
                    fadeAnimation(numberPickerCentimeter, R.string.empty_text, TO_VISIBLE)
                    fadeAnimation(numberPickerWeight, R.string.empty_text, TO_INVISIBLE)
                }
                fadeAnimation(textQuestion,        R.string.question3,         STILL_VISIBLE)
                fadeAnimation(actualCounter,       R.string.empty_text,        STILL_VISIBLE)
            }
            4 -> {
                if (lastQuestion == (questionCounter - 1)){
                    fadeAnimation(numberPickerMeter,      R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(numberPickerCentimeter, R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(numberPickerWeight, R.string.empty_text, TO_VISIBLE)
                }
                else{
                    fadeAnimation(numberPickerWeight, R.string.empty_text, TO_VISIBLE)
                    fadeAnimation(radioButton1,  R.string.empty_text,      TO_INVISIBLE)
                    fadeAnimation(radioButton2,  R.string.empty_text,        TO_INVISIBLE)
                    fadeAnimation(radioButton3,  R.string.empty_text,        TO_INVISIBLE)
                    fadeAnimation(radioButton4,  R.string.empty_text,        TO_INVISIBLE)
                    fadeAnimation(radioButton5,  R.string.empty_text,        TO_INVISIBLE)
                    fadeAnimation(radioButton6,  R.string.empty_text,        TO_INVISIBLE)
                }
                fadeAnimation(textQuestion,        R.string.question4,         STILL_VISIBLE)
                fadeAnimation(actualCounter,       R.string.empty_text,        STILL_VISIBLE)
            }
            5 -> {
                if (lastQuestion == (questionCounter - 1)) {
                    fadeAnimation(numberPickerWeight, R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(radioButton1,  R.string.question5_answer1, TO_VISIBLE)
                    fadeAnimation(radioButton2,  R.string.question5_answer2, TO_VISIBLE)
                    fadeAnimation(radioButton3, R.string.question5_answer3, TO_VISIBLE)
                    fadeAnimation(radioButton4, R.string.question5_answer4, TO_VISIBLE)
                    fadeAnimation(radioButton5,  R.string.question5_answer5, TO_VISIBLE)
                } else {
                    fadeAnimation(radioButton1,  R.string.question5_answer1, STILL_VISIBLE)
                    fadeAnimation(radioButton2,  R.string.question5_answer2, STILL_VISIBLE)
                    fadeAnimation(radioButton3, R.string.question5_answer3, STILL_VISIBLE)
                    fadeAnimation(radioButton4, R.string.question5_answer4, STILL_VISIBLE)
                    fadeAnimation(radioButton5,  R.string.question5_answer5, STILL_VISIBLE)
                }
                fadeAnimation(textQuestion,  R.string.question5,         STILL_VISIBLE)
                fadeAnimation(radioButton6,  R.string.question5_answer6, TO_VISIBLE)
                fadeAnimation(actualCounter, R.string.empty_text,    STILL_VISIBLE)
            }
            6 -> {
                if (lastQuestion == (questionCounter + 1)) {
                    fadeAnimation(radioButton7,  R.string.empty_text,        TO_INVISIBLE)
                }
                fadeAnimation(radioButton1,  R.string.question6_answer1, STILL_VISIBLE)
                fadeAnimation(radioButton2,  R.string.question6_answer2, STILL_VISIBLE)
                fadeAnimation(radioButton3,  R.string.question6_answer3, STILL_VISIBLE)
                fadeAnimation(radioButton4,  R.string.question6_answer4, STILL_VISIBLE)
                fadeAnimation(radioButton5,  R.string.question6_answer5, STILL_VISIBLE)
                fadeAnimation(radioButton6,  R.string.empty_text,        TO_INVISIBLE)
                fadeAnimation(textQuestion,  R.string.question6,  STILL_VISIBLE)
                fadeAnimation(actualCounter, R.string.empty_text, STILL_VISIBLE)
            }
            7 -> {
                if (lastQuestion == (questionCounter - 1)) {
                    fadeAnimation(radioButton1,  R.string.question7_answer1, STILL_VISIBLE)
                    fadeAnimation(radioButton2,  R.string.question7_answer2, STILL_VISIBLE)
                    fadeAnimation(radioButton3, R.string.question7_answer3, STILL_VISIBLE)
                    fadeAnimation(radioButton4, R.string.question7_answer4, STILL_VISIBLE)
                    fadeAnimation(radioButton5,  R.string.question7_answer5, STILL_VISIBLE)

                } else {
                    fadeAnimation(radioButton1,  R.string.question7_answer1, TO_VISIBLE)
                    fadeAnimation(radioButton2,  R.string.question7_answer2, TO_VISIBLE)
                    fadeAnimation(radioButton3, R.string.question7_answer3, TO_VISIBLE)
                    fadeAnimation(radioButton4, R.string.question7_answer4, TO_VISIBLE)
                    fadeAnimation(radioButton5,  R.string.question7_answer5, TO_VISIBLE)
                    fadeAnimation(checkBox1,     R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(checkBox2,     R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(checkBox3,     R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(checkBox4,     R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(checkBox5,     R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(checkBox6,     R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(checkBox7,     R.string.empty_text, TO_INVISIBLE)
                }
                fadeAnimation(textQuestion,  R.string.question7,         STILL_VISIBLE)
                fadeAnimation(radioButton6,  R.string.question7_answer6, TO_VISIBLE)
                fadeAnimation(radioButton7,  R.string.question7_answer7, TO_VISIBLE)
                fadeAnimation(actualCounter, R.string.empty_text,    STILL_VISIBLE)
            }
            8 -> {
                if (lastQuestion == (questionCounter - 1)) {
                    fadeAnimation(radioButton1,  R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(radioButton2,  R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(radioButton3,  R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(radioButton4,  R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(radioButton5,  R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(radioButton6,  R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(radioButton7,  R.string.empty_text, TO_INVISIBLE)
                }
                else{
                    fadeAnimation(datePickerMarathon, R.string.empty_text, TO_INVISIBLE)
                    fadeAnimation(buttonNext,    R.string.buttonNext,       STILL_VISIBLE)
                }
                fadeAnimation(textQuestion,  R.string.question8,         STILL_VISIBLE)
                fadeAnimation(checkBox1,     R.string.question8_answer1, TO_VISIBLE)
                fadeAnimation(checkBox2,     R.string.question8_answer2, TO_VISIBLE)
                fadeAnimation(checkBox3,     R.string.question8_answer3, TO_VISIBLE)
                fadeAnimation(checkBox4,     R.string.question8_answer4, TO_VISIBLE)
                fadeAnimation(checkBox5,     R.string.question8_answer5, TO_VISIBLE)
                fadeAnimation(checkBox6,     R.string.question8_answer6, TO_VISIBLE)
                fadeAnimation(checkBox7,     R.string.question8_answer7, TO_VISIBLE)
                fadeAnimation(actualCounter, R.string.empty_text,    STILL_VISIBLE)
            }
            9 -> {
                fadeAnimation(checkBox1,     R.string.empty_text, TO_INVISIBLE)
                fadeAnimation(checkBox2,     R.string.empty_text, TO_INVISIBLE)
                fadeAnimation(checkBox3,     R.string.empty_text, TO_INVISIBLE)
                fadeAnimation(checkBox4,     R.string.empty_text, TO_INVISIBLE)
                fadeAnimation(checkBox5,     R.string.empty_text, TO_INVISIBLE)
                fadeAnimation(checkBox6,     R.string.empty_text, TO_INVISIBLE)
                fadeAnimation(checkBox7,     R.string.empty_text, TO_INVISIBLE)
                fadeAnimation(textQuestion,  R.string.question9, STILL_VISIBLE)
                fadeAnimation(datePickerMarathon, R.string.empty_text, TO_VISIBLE)
                fadeAnimation(buttonNext,    R.string.button_save,       STILL_VISIBLE)
                fadeAnimation(actualCounter, R.string.empty_text,    STILL_VISIBLE)
            }
        }
    }

    private fun updateCheckedButtons(){
        changingScreen = true
        when (questionCounter) {
            1 -> when (profile.gender) {
                    0 -> radioButton2.isChecked = true
                    1 -> radioButton1.isChecked = true
                }
            5 -> when (profile.experience) {
                    1 -> radioButton1.isChecked = true
                    2 -> radioButton2.isChecked = true
                    3 -> radioButton3.isChecked = true
                    4 -> radioButton4.isChecked = true
                    5 -> radioButton5.isChecked = true
                    6 -> radioButton6.isChecked = true
                }
            6 -> when (profile.nbMarathonsDone) {
                0 -> radioButton1.isChecked = true
                1 -> radioButton2.isChecked = true
                2 -> radioButton3.isChecked = true
                3 -> radioButton4.isChecked = true
                4 -> radioButton5.isChecked = true
            }
            7 -> when (profile.nbTrainingsPerWeek) {
                1 -> radioButton1.isChecked = true
                2 -> radioButton2.isChecked = true
                3 -> radioButton3.isChecked = true
                4 -> radioButton4.isChecked = true
                5 -> radioButton5.isChecked = true
                6 -> radioButton5.isChecked = true
                7 -> radioButton5.isChecked = true
            }
        }
        changingScreen = false
    }

    private fun fadeAnimation(widget: View, text: Int, visibility: Int) {
        var textView = TextView(this)
        if (widget is TextView){
            textView = widget as TextView
        }
        val fadeStillVisible: Animation = AlphaAnimation(1.0f, 0.0f)
        fadeStillVisible.duration = 500
        fadeStillVisible.repeatCount = 1
        fadeStillVisible.repeatMode = Animation.REVERSE
        val fadeToVisible: Animation = AlphaAnimation(0.0f, 1.0f)
        fadeToVisible.startOffset = 500
        fadeToVisible.duration = 500
        val fadeToInvisible: Animation = AlphaAnimation(1.0f, 0.0f)
        fadeToInvisible.duration = 500
        when (visibility) {
            STILL_VISIBLE -> {
                widget.startAnimation(fadeStillVisible)
            }
            TO_VISIBLE -> {
                widget.startAnimation(fadeToVisible)
            }
            TO_INVISIBLE -> {
                widget.startAnimation(fadeToInvisible)
            }
        }
        fadeStillVisible.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                buttonNext.isClickable = false
                buttonLast.isClickable = false
                radioButton1.isClickable = false
                radioButton2.isClickable = false
                radioButton3.isClickable = false
                radioButton4.isClickable = false
                radioButton5.isClickable = false
                radioButton6.isClickable = false
                radioButton7.isClickable = false
                numberPickerAge.isActivated = false
            }
            override fun onAnimationEnd(animation: Animation) {
                buttonNext.isClickable = true
                buttonLast.isClickable = true
                radioButton1.isClickable = true
                radioButton2.isClickable = true
                radioButton3.isClickable = true
                radioButton4.isClickable = true
                radioButton5.isClickable = true
                radioButton6.isClickable = true
                radioButton7.isClickable = true
                numberPickerAge.isActivated = true
            }
            override fun onAnimationRepeat(animation: Animation) {
                when (widget) {
                    estimatedTime -> {
                        estimatedTime.text = convertEstimatedTime()
                    }
                    actualCounter -> {
                        actualCounter.text = "$questionCounter"
                    }
                    barTimeEstimated -> {
                        barTimeEstimated.progress = estimatedSeconds
                    }
                    else -> {
                        textView.setText(text)
                        updateCheckedButtons()
                    }
                }
            }
        })
        fadeToVisible.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                when (widget) {
                    estimatedTime -> {
                        estimatedTime.text = convertEstimatedTime()
                    }
                    actualCounter -> {
                        textView.text = "$questionCounter"
                    }
                    totalCounter -> {
                        textView.text = "$nbQuestion"
                    }
                    else -> {
                        textView.setText(text)
                    }
                }
                buttonNext.isClickable = false
                buttonLast.isClickable = false
            }
            override fun onAnimationEnd(animation: Animation) {
                widget.visibility = View.VISIBLE
                buttonNext.isClickable = true
                buttonLast.isClickable = true
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
        fadeToInvisible.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                widget.visibility = View.INVISIBLE
                textView.setText(text)
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    private fun popUp(message: Int, confirm: Int){
        val builder = AlertDialog.Builder(this, R.style.Theme_custom_dialog_alert)
        builder.setCancelable(true)
        builder.setMessage(message)
        builder.setPositiveButton(confirm) { _, _ -> }
        val dialog = builder.create()
        dialog.show()
        popUpDisplayed = true
    }

    private fun setLocale(lang: String) {
        val myLocale = Locale(lang)
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)
        profile = (intent.extras?.get("extra_profile") as Profile?)!!
        textBeforeQuestions = findViewById(R.id.textViewBeforeQuestions)
        textEstimatedTime   = findViewById(R.id.textViewEstimatedText)
        estimatedTime       = findViewById(R.id.textViewEstimatedTime)
        barTimeEstimated    = findViewById(R.id.progressBarTimeEstimated)
        textQuestion        = findViewById(R.id.textViewQuestions)
        radioGroup          = findViewById(R.id.radioGroupQuestions)
        radioButton1        = findViewById(R.id.radioButton1)
        radioButton2        = findViewById(R.id.radioButton2)
        radioButton3        = findViewById(R.id.radioButton3)
        radioButton4        = findViewById(R.id.radioButton4)
        radioButton5        = findViewById(R.id.radioButton5)
        radioButton6        = findViewById(R.id.radioButton6)
        radioButton7        = findViewById(R.id.radioButton7)
        checkBox1           = findViewById(R.id.checkBoxMonday)
        checkBox2           = findViewById(R.id.checkBoxTuesday)
        checkBox3           = findViewById(R.id.checkBoxWednesday)
        checkBox4           = findViewById(R.id.checkBoxThursday)
        checkBox5           = findViewById(R.id.checkBoxFriday)
        checkBox6           = findViewById(R.id.checkBoxSaturday)
        checkBox7           = findViewById(R.id.checkBoxSunday)
        actualCounter       = findViewById(R.id.textViewQuestionCounterActual)
        totalCounter        = findViewById(R.id.textViewQuestionCounterTotal)
        delimiterCounter    = findViewById(R.id.textViewQuestionCounterDelimiter)
        buttonNext          = findViewById(R.id.buttonNext)
        buttonLast          = findViewById(R.id.buttonLast)
        datePickerMarathon  = findViewById(R.id.datePickerMarathon)
        numberPickerMeter   = findViewById(R.id.numberPickerMeter)
        numberPickerCentimeter = findViewById(R.id.numberPickerCentimeter)
        numberPickerWeight     = findViewById(R.id.numberPickerWeight)
        numberPickerAge     = findViewById(R.id.numberPickerAge)
        textBeforeQuestionsList.add(R.string.question0)
        textBeforeQuestionsList.add(R.string.text_before_questions1)
        textBeforeQuestionsList.add(R.string.text_before_questions2)
        textBeforeQuestionsList.add(R.string.text_before_questions3)
        textBeforeQuestionsList.add(R.string.text_before_questions4)
        textBeforeQuestionsList.add(R.string.text_before_questions5)

        tflite = loadModelFile()?.let { Interpreter(it.asReadOnlyBuffer()) }!!
        runAlgo()
        estimatedTime.text = convertEstimatedTime()

        radioGroup.setOnCheckedChangeListener { _, answer ->
            if (answer != -1 &&
                (radioGroup.findViewById<RadioButton>(answer).isChecked) &&
                (questionCounter > 0) && !changingScreen) {
                setEstimatedTime(radioGroup.checkedRadioButtonId)
            }
        }
        numberPickerAge.setOnValueChangedListener { _, _, new ->
            setEstimatedTime(new)
        }
        numberPickerMeter.setOnValueChangedListener { _, _, new ->
            setEstimatedTime((new * 100) + numberPickerCentimeter.value)
        }
        numberPickerCentimeter.setOnValueChangedListener { _, _, new ->
            setEstimatedTime((numberPickerMeter.value * 100) + new)
        }
        numberPickerWeight.setOnValueChangedListener { _, _, new ->
            setEstimatedTime(new)
        }
        val date = Calendar.getInstance()
        date.time = Date()
        datePickerMarathon.init(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, day ->
            date.set(year, month, day)
            profile.dateMarathon = date.time
        }
        numberPickerAge.run {
            minValue = 18
            maxValue = 80
            value = profile.age
        }
        numberPickerMeter.run {
            minValue = 1
            maxValue = 1
            value = 1
        }
        numberPickerCentimeter.run {
            minValue = 0
            maxValue = 99
            value = profile.height - 100
        }
        numberPickerWeight.run {
            minValue = 30
            maxValue = 130
            value = profile.weight
        }
        buttonNext.setOnClickListener { view ->
            when {
                questionCounter < nbQuestion -> {
                    if (questionCounter == -5) {
                        if (radioGroup.checkedRadioButtonId == R.id.radioButton2){
                            profile.language = "fr"
                        }
                        else{
                            profile.language = "en"
                        }
                        setLocale(profile.language)
                        intent = Intent(view.context, QuestionsActivity::class.java)
                        intent.putExtra("extra_profile", profile)
                        startActivity(intent)
                    }
                    else{
                        if (questionCounter == 4) {
                            profile.preferenceDays.clear()
                            if (checkBox1.isChecked){profile.preferenceDays.add(1)}
                            if (checkBox2.isChecked){profile.preferenceDays.add(2)}
                            if (checkBox3.isChecked){profile.preferenceDays.add(3)}
                            if (checkBox4.isChecked){profile.preferenceDays.add(4)}
                            if (checkBox5.isChecked){profile.preferenceDays.add(5)}
                            if (checkBox6.isChecked){profile.preferenceDays.add(6)}
                            if (checkBox7.isChecked){profile.preferenceDays.add(7)}
                        }
                        lastQuestion = questionCounter
                        questionCounter++
                        changeScreen()
                    }
                }
                else -> {
                    if (getDaysLeft(profile.dateMarathon) < MIN_DAYS_TRAINING) {
                        popUp(R.string.wrong_date_message, R.string.wrong_date_confirm)
                    }
                    else {
                        profile.setTrainingsList()
                        profile.firstConnexion = false
                        saveProfile(profile)
                        val intentHome = Intent(view.context, HomeActivity::class.java)
                        intentHome.putExtra("extra_profile", profile)
                        startActivity(intentHome)
                    }
                }
            }
        }
        buttonLast.setOnClickListener {
            lastQuestion = questionCounter
            questionCounter--
            changeScreen()
        }
        if(profile.language != "default"){
            questionCounter++
        }
        changeScreen()
    }
}