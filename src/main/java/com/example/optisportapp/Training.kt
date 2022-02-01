package com.example.optisportapp

import android.content.res.Resources
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class Training(var date: Date, var type: Int, var details: Int) : Serializable

val TRAINING_NORMAL = R.string.training_normal
val TRAINING_FRACTIONAL = R.string.training_fractional
val TRAINING_LONG = R.string.training_long
val TRAINING_MUSCULAR = R.string.training_muscular
val trainingListNormal = arrayListOf(R.string.footing1, R.string.footing2, R.string.footing3)
val trainingListFractional = arrayListOf(R.string.fractional1, R.string.fractional2,
    R.string.fractional3, R.string.fractional4, R.string.fractional5, R.string.fractional6,
    R.string.fractional7)
val trainingListLong = arrayListOf(R.string.long1, R.string.long2, R.string.long3, R.string.long4,
    R.string.long5, R.string.long6, R.string.long7)
val trainingListMuscular= arrayListOf(R.string.muscular1, R.string.muscular2,
    R.string.muscular3, R.string.muscular4)
val tipsList = arrayListOf(R.string.tip1, R.string.tip2, R.string.tip3, R.string.tip4,
    R.string.tip5, R.string.tip6, R.string.tip7, R.string.tip8, R.string.tip9, R.string.tip10,
    R.string.tip11, R.string.tip12, R.string.tip13, R.string.tip14, R.string.tip15, R.string.tip16,
    R.string.tip17, R.string.tip18, R.string.tip19, R.string.tip20, R.string.tip21)

fun getTrainingIndex(profile: Profile, date: Date): Int{
    val dateFormat = SimpleDateFormat("yyyyMMdd")
    var trainingIndex = -1
    for (training in 0 until profile.trainingsList.size) {
        if (dateFormat.format(date).equals(
                dateFormat.format(profile.trainingsList[training].date))) {
            trainingIndex = training
        }
    }
    return trainingIndex
}