package com.example.optisportapp

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.TextView
import java.io.Serializable
import java.util.*


class MainActivity : AppCompatActivity(), Serializable {

    override fun onBackPressed() {
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun setLocale(lang: String) {
        val myLocale = Locale(lang)
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
    }

    private fun startFadeAnimation(textView: TextView, startTime: Int) {
        val fadeIn: Animation = AlphaAnimation(1.0f, 0.0f)
        val fadeOut: Animation = AlphaAnimation(0.0f, 1.0f)
        textView.startAnimation(fadeIn)
        textView.startAnimation(fadeOut)
        fadeIn.duration = 1000
        fadeIn.fillAfter = true
        fadeOut.duration = 1000
        fadeOut.fillAfter = true
        fadeOut.startOffset = startTime.toLong()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ModelPreferencesManager.with(this)
        var profile: Profile? = getProfile()
        if (profile != null)
        {
            setLocale(profile.language)
        }
        setContentView(R.layout.activity_main)
        val appTitle = findViewById<TextView>(R.id.appTitle)
        val tapScreen = findViewById<TextView>(R.id.tapScreen)
        startFadeAnimation(appTitle, 1000)
        startFadeAnimation(tapScreen, 2000)
        val buttonTapScreen = findViewById<Button>(R.id.buttonTapScreen)
        buttonTapScreen.setOnClickListener { view ->
            val intent: Intent
            if ((profile != null) && !profile!!.firstConnexion)
            {
                intent = Intent(view.context, HomeActivity::class.java)
                startActivity(intent)
            } else {
                profile = Profile()
                intent = Intent(view.context, QuestionsActivity::class.java)
            }
            intent.putExtra("extra_profile", profile)
            startActivity(intent)
        }
    }
}