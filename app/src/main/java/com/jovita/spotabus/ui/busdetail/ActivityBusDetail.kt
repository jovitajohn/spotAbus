package com.jovita.spotabus.ui.busdetail

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jovita.spotabus.HomeActivity
import com.jovita.spotabus.RegisterActivity
import com.jovita.spotabus.databinding.ActivityBusDetailBinding
import com.jovita.spotabus.databinding.ActivityLoginBinding

class ActivityBusDetail: Activity() {
    private lateinit var binding: ActivityBusDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }
}