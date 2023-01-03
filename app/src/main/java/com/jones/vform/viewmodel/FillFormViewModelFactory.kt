package com.jones.vform.viewmodel

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jones.vform.databinding.ActivityFillFormBinding
import com.jones.vform.databinding.ActivityMainBinding

class FillFormViewModelFactory (
    val app: AppCompatActivity,
    val binding: ActivityFillFormBinding
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FillFormViewModel(app, binding) as T
    }
}