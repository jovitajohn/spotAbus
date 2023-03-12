package com.jovita.spotabus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jovita.spotabus.databinding.ActivitySplashBinding

class SplashActivity : Activity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var intent = Intent(this@SplashActivity,LoginActivity::class.java)
        startActivity(intent)

    }
}