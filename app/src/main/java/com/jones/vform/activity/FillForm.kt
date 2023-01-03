package com.jones.vform.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.size
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jones.vform.R
import com.jones.vform.data.Field
import com.jones.vform.databinding.ActivityFillFormBinding
import com.jones.vform.databinding.RowFormFillBinding
import com.jones.vform.viewmodel.FillFormViewModel
import com.jones.vform.viewmodel.FillFormViewModelFactory

class FillForm : AppCompatActivity() {

    private lateinit var binding: ActivityFillFormBinding
    private lateinit var viewModel: FillFormViewModel
    private lateinit var viewModelFactory: FillFormViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory = FillFormViewModelFactory(this, binding)
        viewModel = ViewModelProvider(this,viewModelFactory)[FillFormViewModel::class.java]

        val formName: String? = intent.getStringExtra("FormName")!!
        binding.fillFormTitle.text = StringBuilder(getString(R.string.form_name) + formName)

        viewModel.openForm(formName)

        binding.submitButton.setOnClickListener {
            viewModel.submitForm(formName)
        }
    }

}