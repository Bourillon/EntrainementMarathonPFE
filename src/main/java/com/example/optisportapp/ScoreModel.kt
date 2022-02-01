package com.example.optisportapp
import org.pmml4s.model.Model
import java.util.*

class ScoreModel {
    private val model: Model = Model.fromFile(
        ScoreModel::class.java.classLoader.getResource("model.xml").file
    )

    fun getScore(values: Map<String, Double>): Double {
        val valuesMap: Array<Any>? = Arrays.stream(model.inputNames())
            .map { o: Any? -> values[o] }
            .toArray()
        //val result = model.predict(valuesMap)
        //return result[0] as Double
        return 0.0
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val scoreModel = ScoreModel()
            val valuesKotlin = mapOf("age" to 10.0, "sex" to 1.0)
            /*
            val values = java.util.Map.of(
                "age", 20.0,
                "sex", 1.0,
                "bmi", -100.0,
                "bp", -200.0,
                "s1", 1.0,
                "s2", 2.0,
                "s3", 3.0,
                "s4", 4.0,
                "s5", 5.0,
                "s6", 6.0
            )*/
            val predicted = scoreModel.getScore(valuesKotlin)
        }
    }
}