package com.jones.vform.viewmodel

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jones.vform.databinding.ActivityMainBinding
import com.jones.vform.databinding.ActivityNewFormBinding

class MainViewModelFactory (
    val app: AppCompatActivity,
    val binding: ActivityMainBinding
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(app, binding) as T
    }
}