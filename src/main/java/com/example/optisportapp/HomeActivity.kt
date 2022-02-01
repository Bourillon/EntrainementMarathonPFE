package com.example.optisportapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import org.tensorflow.lite.Interpreter
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.MappedByteBuffer
import kotlin.io.path.outputStream
import android.content.res.AssetFileDescriptor
import java.io.FileInputStream
import java.io.IOException
import java.nio.channels.FileChannel


class HomeActivity : AppCompatActivity() {

    private lateinit var profile: Profile

    override fun onBackPressed() {
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        /*
        val model = ModelScore(this)
        val score = model.regressionValue
         */

        var extraFound = false
        if (intent.getSerializableExtra("extra_profile") != null){
            profile = intent.getSerializableExtra("extra_profile") as Profile
            extraFound = true
        }
        else{
            finish()
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            profile = Profile()
        }
        val buttonProfile = findViewById<Button>(R.id.buttonProfile)
        buttonProfile.setOnClickListener { view ->
            val intentProfile = Intent(view.context, ProfileActivity::class.java)
            if (extraFound){
                intentProfile.putExtra("extra_profile", profile)
            }
            startActivity(intentProfile)
        }
        val buttonTraining = findViewById<Button>(R.id.buttonTraining)
        buttonTraining.setOnClickListener { view ->
            val intentTraining = Intent(view.context, TrainingActivity::class.java)
            if (extraFound) {
                intentTraining.putExtra("extra_profile", profile)
            }
            startActivity(intentTraining)
        }
        val buttonTips = findViewById<Button>(R.id.buttonTips)
        buttonTips.setOnClickListener { view ->
            val intentTips = Intent(view.context, TipsActivity::class.java)
            if (extraFound) {
                intentTips.putExtra("extra_profile", profile)
            }
            startActivity(intentTips)
        }
        val buttonSettings = findViewById<Button>(R.id.buttonSettings)
        buttonSettings.setOnClickListener { view ->
            val intentSettings = Intent(view.context, SettingsActivity::class.java)
            if (extraFound) {
                intentSettings.putExtra("extra_profile", profile)
            }
            startActivity(intentSettings)
        }
    }
}