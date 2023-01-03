package com.jones.vform.viewmodel

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jones.vform.databinding.ActivityNewFormBinding

class NewFormViewModelFactory(
    val app: AppCompatActivity,
    val binding: ActivityNewFormBinding
    ): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewFormViewModel(app, binding) as T
    }
}