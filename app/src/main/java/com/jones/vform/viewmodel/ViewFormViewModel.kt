package com.jones.vform.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import com.jones.vform.databinding.ActivityViewFormBinding

class ViewFormViewModel(
    val app: AppCompatActivity,
    val binding: ActivityViewFormBinding
    ) : AndroidViewModel(app.application) {

}