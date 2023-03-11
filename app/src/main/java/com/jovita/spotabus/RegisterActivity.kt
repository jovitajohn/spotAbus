package com.jovita.spotabus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jovita.spotabus.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        binding.btnRegisterDone.setOnClickListener(View.OnClickListener {
            var home = Intent(this@RegisterActivity,HomeActivity::class.java)
            startActivity(home)
        })
    }
}