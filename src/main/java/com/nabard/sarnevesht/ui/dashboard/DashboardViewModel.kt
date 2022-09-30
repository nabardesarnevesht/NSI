package com.nabard.sarnevesht.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "محتوای آموزشی به زودی در این قسمت نمایش داده می\u200Cشود."
    }
    val text: LiveData<String> = _text
}