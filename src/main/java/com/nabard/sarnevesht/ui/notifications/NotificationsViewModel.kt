package com.nabard.sarnevesht.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "اطلاع\u200Cرسانی\u200Cهای مربوط به شهر و ارتش انتخاب شده پس از انتشار در این قسمت اضافه خواهد شد. منتظر اخبار جدید باشید."
    }
    val text: LiveData<String> = _text
}