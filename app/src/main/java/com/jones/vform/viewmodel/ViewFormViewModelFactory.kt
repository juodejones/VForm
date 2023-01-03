package com.jones.vform.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jones.vform.databinding.ActivityViewFormBinding

class ViewFormViewModelFactory(
    val app: AppCompatActivity,
    val binding: ActivityViewFormBinding
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewFormViewModel(app, binding) as T
    }
}