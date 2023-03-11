package com.jovita.spotabus.ui.supermarket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SuperMarketViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is super market Fragment"
    }
    val text: LiveData<String> = _text
}