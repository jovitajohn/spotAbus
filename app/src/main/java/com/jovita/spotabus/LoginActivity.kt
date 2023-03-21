package com.jovita.spotabus

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jovita.mycustomarimagelabeling.files.MainActivity
import com.jovita.spotabus.databinding.ActivityLoginBinding

class LoginActivity: Activity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLogin.setOnClickListener(View.OnClickListener {
            var home = Intent(this@LoginActivity,HomeActivity::class.java)
            startActivity(home)
        })

        binding.btnRegister.setOnClickListener(View.OnClickListener {
            var register = Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(register)
        })

        binding.btnForgot.setOnClickListener(View.OnClickListener {

                //Instantiate builder variable to show alert dialog
                val builder = AlertDialog.Builder(view.context)
                builder.setTitle("")
                builder.setMessage("Please check email for password reset!")
                builder.setPositiveButton(
                    "Ok") { dialog, id ->
                }
                builder.show()
        })
    }
}