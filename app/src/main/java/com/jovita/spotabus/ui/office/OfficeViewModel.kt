package com.jovita.spotabus.ui.office

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OfficeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is office Fragment"
    }
    val text: LiveData<String> = _text
}