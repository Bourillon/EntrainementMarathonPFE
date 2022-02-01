package com.example.optisportapp

import android.content.res.Resources
import java.io.Serializable
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class Profile : Serializable {
    var firstConnexion = true
    var gender = 1
    var age = 30
    var height = 185
    var weight = 75
    var eat = 1
    var eatBefore = 2
    var imc = (weight.toDouble()) / ((height.toDouble()/100)*(height.toDouble()/100))
    var nbFootingPerWeek = 1
    var nbLongPerWeek = 1
    var nbFractionatePerWeek = 1
    var nbMuscularPerWeek = 0
    var trainingDuration = 2
    var muscular = 1
    var activities = 1
    var nbTrainingsPerWeek = 3
    var nbMarathonsDone = 1
    var experience = 3
    var language = "default"
    var goalTime = "4\'01"
    var nbTrainingsDone = 0
    var nbTrainingsTotal = 0
    var nbTrainingsNormalDone = 0
    var nbTrainingsNormalTotal = 0
    var nbTrainingsFractionalDone = 0
    var nbTrainingsFractionalTotal = 0
    var nbTrainingsLongDone = 0
    var nbTrainingsLongTotal = 0
    var nbTrainingsMuscularDone = 0
    var nbTrainingsMuscularTotal = 0
    var dateMarathon = Date()
    val trainingsList = arrayListOf<Training>()
    private var trainingsListType = arrayListOf<Int>()
    private var trainingsListDetails = arrayListOf<Int>()
    val preferenceDays = arrayListOf<Int>()
    var daysBeforeTraining = 0
    var editTraining = false

    private fun setTrainingsListTypes(){
        var trainingIndex = 0
        when (nbTrainingsPerWeek) {
            1 -> {
                trainingIndex = (0 until trainingListNormal.size).random()
                trainingsListType.add(TRAINING_NORMAL)
                trainingsListDetails.add(trainingListNormal[trainingIndex])
                nbTrainingsNormalTotal++
            }
            2 -> {
                trainingIndex = (0 until trainingListNormal.size).random()
                trainingsListType.add(TRAINING_NORMAL)
                trainingsListDetails.add(trainingListNormal[trainingIndex])
                nbTrainingsNormalTotal++
                trainingIndex = (0 until trainingListFractional.size).random()
                trainingsListType.add(TRAINING_FRACTIONAL)
                trainingsListDetails.add(trainingListFractional[trainingIndex])
                nbTrainingsFractionalTotal++
            }
            3 -> {
                trainingIndex = (0 until trainingListNormal.size).random()
                trainingsListType.add(TRAINING_NORMAL)
                trainingsListDetails.add(trainingListNormal[trainingIndex])
                nbTrainingsNormalTotal++
                trainingIndex = (0 until trainingListFractional.size).random()
                trainingsListType.add(TRAINING_FRACTIONAL)
                trainingsListDetails.add(trainingListFractional[trainingIndex])
                nbTrainingsFractionalTotal++
                trainingIndex = (0 until trainingListLong.size).random()
                trainingsListType.add(TRAINING_LONG)
                trainingsListDetails.add(trainingListLong[trainingIndex])
                nbTrainingsLongTotal++
            }
            4 -> {
                trainingIndex = (0 until trainingListNormal.size).random()
                trainingsListType.add(TRAINING_NORMAL)
                trainingsListDetails.add(trainingListNormal[trainingIndex])
                nbTrainingsNormalTotal++
                do {
                    trainingIndex = (0 until trainingListNormal.size).random()
                }while (trainingsListDetails.contains(trainingListNormal[trainingIndex]))
                trainingsListType.add(TRAINING_NORMAL)
                trainingsListDetails.add(trainingListNormal[trainingIndex])
                nbTrainingsNormalTotal++
                trainingIndex = (0 until trainingListFractional.size).random()
                trainingsListType.add(TRAINING_FRACTIONAL)
                trainingsListDetails.add(trainingListFractional[trainingIndex])
                nbTrainingsFractionalTotal++
                trainingIndex = (0 until trainingListLong.size).random()
                trainingsListType.add(TRAINING_LONG)
                trainingsListDetails.add(trainingListLong[trainingIndex])
                nbTrainingsLongTotal++
            }
            5 -> {
                trainingIndex = (0 until trainingListNormal.size).random()
                trainingsListType.add(TRAINING_NORMAL)
                trainingsListDetails.add(trainingListNormal[trainingIndex])
                nbTrainingsNormalTotal++
                do {
                    trainingIndex = (0 until trainingListNormal.size).random()
                }while (trainingsListDetails.contains(trainingListNormal[trainingIndex]))
                trainingsListType.add(TRAINING_NORMAL)
                trainingsListDetails.add(trainingListNormal[trainingIndex])
                nbTrainingsNormalTotal++
                trainingIndex = (0 until trainingListFractional.size).random()
                trainingsListType.add(TRAINING_FRACTIONAL)
                trainingsListDetails.add(trainingListFractional[trainingIndex])
                nbTrainingsFractionalTotal++
                trainingIndex = (0 until trainingListLong.size).random()
                trainingsListType.add(TRAINING_LONG)
                trainingsListDetails.add(trainingListLong[trainingIndex])
                nbTrainingsLongTotal++
                trainingIndex = (0 until trainingListMuscular.size).random()
                trainingsListType.add(TRAINING_MUSCULAR)
                trainingsListDetails.add(trainingListMuscular[trainingIndex])
                nbTrainingsMuscularTotal++
            }
            6 -> {
                trainingIndex = (0 until trainingListNormal.size).random()
                trainingsListType.add(TRAINING_NORMAL)
                trainingsListDetails.add(trainingListNormal[trainingIndex])
                nbTrainingsNormalTotal++
                do {
                    trainingIndex = (0 until trainingListNormal.size).random()
                }while (trainingsListDetails.contains(trainingListNormal[trainingIndex]))
                trainingsListType.add(TRAINING_NORMAL)
                trainingsListDetails.add(trainingListNormal[trainingIndex])
                nbTrainingsNormalTotal++
                do {
                    trainingIndex = (0 until trainingListNormal.size).random()
                }while (trainingsListDetails.contains(trainingListNormal[trainingIndex]))
                trainingsListType.add(TRAINING_NORMAL)
                trainingsListDetails.add(trainingListNormal[trainingIndex])
                nbTrainingsNormalTotal++
                trainingIndex = (0 until trainingListFractional.size).random()
                trainingsListType.add(TRAINING_FRACTIONAL)
                trainingsListDetails.add(trainingListFractional[trainingIndex])
                nbTrainingsFractionalTotal++
                trainingIndex = (0 until trainingListLong.size).random()
                trainingsListType.add(TRAINING_LONG)
                trainingsListDetails.add(trainingListLong[trainingIndex])
                nbTrainingsLongTotal++
                trainingIndex = (0 until trainingListMuscular.size).random()
                trainingsListType.add(TRAINING_MUSCULAR)
                trainingsListDetails.add(trainingListMuscular[trainingIndex])
                nbTrainingsMuscularTotal++
            }
            7 -> {
                trainingIndex = (0 until trainingListNormal.size).random()
                trainingsListType.add(TRAINING_NORMAL)
                trainingsListDetails.add(trainingListNormal[trainingIndex])
                nbTrainingsNormalTotal++
                do {
                    trainingIndex = (0 until trainingListNormal.size).random()
                }while (trainingsListDetails.contains(trainingListNormal[trainingIndex]))
                trainingsListType.add(TRAINING_NORMAL)
                trainingsListDetails.add(trainingListNormal[trainingIndex])
                nbTrainingsNormalTotal++
                do {
                    trainingIndex = (0 until trainingListNormal.size).random()
                }while (trainingsListDetails.contains(trainingListNormal[trainingIndex]))
                trainingsListType.add(TRAINING_NORMAL)
                trainingsListDetails.add(trainingListNormal[trainingIndex])
                nbTrainingsNormalTotal++
                trainingIndex = (0 until trainingListFractional.size).random()
                trainingsListType.add(TRAINING_FRACTIONAL)
                trainingsListDetails.add(trainingListFractional[trainingIndex])
                nbTrainingsFractionalTotal++
                do {
                    trainingIndex = (0 until trainingListFractional.size).random()
                }while (trainingsListDetails.contains(trainingListFractional[trainingIndex]))
                trainingsListType.add(TRAINING_FRACTIONAL)
                trainingsListDetails.add(trainingListFractional[trainingIndex])
                nbTrainingsFractionalTotal++
                trainingIndex = (0 until trainingListLong.size).random()
                trainingsListType.add(TRAINING_LONG)
                trainingsListDetails.add(trainingListLong[trainingIndex])
                nbTrainingsLongTotal++
                trainingIndex = (0 until trainingListMuscular.size).random()
                trainingsListType.add(TRAINING_MUSCULAR)
                trainingsListDetails.add(trainingListMuscular[trainingIndex])
                nbTrainingsMuscularTotal++
            }
        }
    }

    fun setTrainingsList(){
        val today = Calendar.getInstance()
        val dayMilliseconds = 1000 * 60 * 60 * 24
        var dayOfWeek = today.get(Calendar.DAY_OF_WEEK) - 1
        while (dayOfWeek != 7){
            dayOfWeek++
            daysBeforeTraining++
        }
        val nbOfWeeks = (getDaysLeft(dateMarathon) - daysBeforeTraining) / 7
        var weeksInMilliseconds: Long
        var preferenceDaysWeek: ArrayList<Int>
        var preferenceDaysWeekLen: Int
        var trainingDay: Int
        val trainingsDaysWeek = arrayListOf<Int>()
        for (week in 0 until nbOfWeeks){
            trainingsDaysWeek.clear()
            trainingsListType.clear()
            trainingsListDetails.clear()
            setTrainingsListTypes()
            preferenceDaysWeek = ArrayList(this.preferenceDays)
            for (training in 0 until nbTrainingsPerWeek){
                preferenceDaysWeekLen = preferenceDaysWeek.size
                if (preferenceDaysWeekLen > 0){
                    trainingDay = preferenceDaysWeek.removeAt((0 until preferenceDaysWeekLen).random())
                }
                else{
                    do {
                        trainingDay = (1..7).random()
                    }while (trainingsDaysWeek.contains(trainingDay))
                }
                trainingsDaysWeek.add(trainingDay)
                weeksInMilliseconds = dayMilliseconds.toLong() * week.toLong()  * 7
                trainingsList.add(Training(Date(today.timeInMillis +
                        (dayMilliseconds * daysBeforeTraining) +
                        (dayMilliseconds * trainingDay) +
                        weeksInMilliseconds),
                    trainingsListType[training],
                    trainingsListDetails[training]))
            }
        }
        trainingsList.sortBy {  it.date }
        nbTrainingsTotal = trainingsList.size
    }
}

fun getProfile(): Profile? {
    return ModelPreferencesManager.get("Profile")
}

fun saveProfile(profile: Profile) {
    ModelPreferencesManager.put(profile, "Profile")
}

fun deleteProfile() {
    ModelPreferencesManager.clear()
}

fun getDaysLeft(date: Date): Int {
    val today = Calendar.getInstance().time
    return TimeUnit.MILLISECONDS.toDays(date.time - today.time).toInt()
}

fun dateToString(date: Date, resources: Resources): String{
    var day = "Monday"
    val number: String
    var month = "January"
    val year: String
    val calendarDay = Calendar.getInstance()
    calendarDay.time = date
    when (calendarDay.get(Calendar.DAY_OF_WEEK)) {
        1 -> {
            day = resources.getString(R.string.sunday)
        }
        2 -> {
            day = resources.getString(R.string.monday)
        }
        3 -> {
            day = resources.getString(R.string.tuesday)
        }
        4 -> {
            day = resources.getString(R.string.wednesday)
        }
        5 -> {
            day = resources.getString(R.string.thursday)
        }
        6 -> {
            day = resources.getString(R.string.friday)
        }
        7 -> {
            day = resources.getString(R.string.saturday)
        }
    }
    number = calendarDay.get(Calendar.DAY_OF_MONTH).toString()
    when (calendarDay.get(Calendar.MONTH)) {
        0 -> {
            month = resources.getString(R.string.january)
        }
        1 -> {
            month = resources.getString(R.string.february)
        }
        2 -> {
            month = resources.getString(R.string.march)
        }
        3 -> {
            month = resources.getString(R.string.april)
        }
        4 -> {
            month = resources.getString(R.string.may)
        }
        5 -> {
            month = resources.getString(R.string.june)
        }
        6 -> {
            month = resources.getString(R.string.july)
        }
        7 -> {
            month = resources.getString(R.string.august)
        }
        8 -> {
            month = resources.getString(R.string.september)
        }
        9 -> {
            month = resources.getString(R.string.october)
        }
        10 -> {
            month = resources.getString(R.string.november)
        }
        11 -> {
            month = resources.getString(R.string.december)
        }
    }
    year = calendarDay.get(Calendar.YEAR).toString()
    return "$day $number $month $year"
}


