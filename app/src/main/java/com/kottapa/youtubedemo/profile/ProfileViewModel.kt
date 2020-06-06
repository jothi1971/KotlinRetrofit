package com.kottapa.youtubedemo.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var userName: String
    private lateinit var cellNumber: String
    private val context = getApplication<Application>().applicationContext


    private fun getUserName() {

    }

}
