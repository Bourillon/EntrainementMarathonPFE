package com.example.optisportapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TipsActivity : AppCompatActivity() {

    private lateinit var profile: Profile

    override fun onBackPressed() {
        intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("extra_profile", profile)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)
        profile = intent.getSerializableExtra("extra_profile") as Profile
    }
}