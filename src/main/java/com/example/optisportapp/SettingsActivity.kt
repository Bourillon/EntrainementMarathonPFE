package com.example.optisportapp

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.util.DisplayMetrics
import android.widget.RadioGroup
import java.util.*


class SettingsActivity : AppCompatActivity() {

    private lateinit var profile: Profile

    override fun onBackPressed() {
        intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("extra_profile", profile)
        startActivity(intent)
    }

    private fun updateLanguage(lang: String, profile: Profile) {
        profile.language = lang
        saveProfile(profile)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        profile = intent.getSerializableExtra("extra_profile") as Profile
        val buttonDeleteData = findViewById<Button>(R.id.buttonDeleteData)
        buttonDeleteData.setOnClickListener { view ->
            deleteProfile()
            finish()
            intent = Intent(view.context, MainActivity::class.java)
            startActivity(intent)
        }
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupLanguage)
        radioGroup.setOnCheckedChangeListener { _, _ ->
            if (radioGroup.checkedRadioButtonId == R.id.radioButtonFrench) {
                updateLanguage("fr", profile)
            }
            else{
                updateLanguage("en", profile)
            }
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}