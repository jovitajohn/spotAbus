package com.jovita.spotabus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jovita.spotabus.databinding.ActivityLoginBinding
import com.jovita.spotabus.databinding.ActivityMainBinding

class LoginActivity: Activity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnRegister.setOnClickListener(View.OnClickListener {
            var register = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(register)
        })
    }
}