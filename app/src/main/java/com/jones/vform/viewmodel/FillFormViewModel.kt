package com.jones.vform.viewmodel

import android.app.Application
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.size
import androidx.lifecycle.AndroidViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.jones.vform.R
import com.jones.vform.data.AppController
import com.jones.vform.data.Field
import com.jones.vform.databinding.ActivityFillFormBinding
import com.jones.vform.databinding.RowFormFillBinding

class FillFormViewModel(
    val app: AppCompatActivity,
    private val binding: ActivityFillFormBinding
) : AndroidViewModel(app.application) {

    private var fireDB = FirebaseDatabase.getInstance()
    private var formDB = fireDB.getReference("Forms")
    private var database = fireDB.getReference("Data")
    private var keyDB = fireDB.getReference("Keys")
    private var formFields: MutableList<Field> = mutableListOf()

    fun openForm(formName: String?) {
        var formKey: String? = null
        keyDB.child(formName!!).get().addOnSuccessListener {
            formKey = it.getValue(String::class.java)
            Log.d("MYTAG", "openForm: $formKey")
            if (formKey != null) {
                formDB.child(formKey!!).child("fields").get()
                    .addOnSuccessListener {
                        if (it.exists()) {
//                            Log.d("MYTAG", "openForm: ${it.childrenCount}")
                            for (c in it.children) {
                                Log.d("MYTAG", "onSuccess: ${c.value}")
                                formFields.add(c.getValue(Field::class.java)!!)
                            }
                            addFields()
                        }
                    }
                    .addOnFailureListener {
                        Log.d("MYTAG", "onFailure: $it")
                    }
            }
        }
//        if (formKey == null || formKey?.isEmpty()!!) {
//            return
//        }

    }

    private fun addFields() {
//        var list:
        formFields.forEach {
//            Log.d("TAGDATA", "execute: ${it.label} : ${it.dataType}")
//            val fieldView = layoutInflater.inflate(R.layout.row_form_fill, null , false)
            val formBinding = RowFormFillBinding.inflate(LayoutInflater.from(app))

            formBinding.fillFormFieldTV.text = it.label

            when (it.dataType) {
                EditText::class.simpleName -> {
                    formBinding.fillFormEt.visibility = View.VISIBLE
//                    formBinding.fillFormEt.hint = it.label
                }
                Spinner::class.simpleName -> {
                    val spinnerAdapter =
                        ArrayAdapter(app, android.R.layout.simple_spinner_item, it.items!!)
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    formBinding.fillFormSpinner.adapter = spinnerAdapter
                    formBinding.fillFormSpinner.visibility = View.VISIBLE
                    formBinding.fillFormSpinner.prompt = "Select " + it.label!!

                }
            }
            binding.fillLinearLayout.addView(formBinding.root)
        }
    }

    fun submitForm(formName: String?) {
        var map = mutableMapOf<String, String>()
        for (i in 0 until binding.fillLinearLayout.size) {
            var inputText = ""
            when (formFields[i].dataType!!) {
                EditText::class.simpleName -> {
                    inputText =
                        binding.fillLinearLayout[i].findViewById<EditText>(R.id.fill_form_et).text.toString()
                            .trim()
                }
                Spinner::class.simpleName -> {
                    inputText =
                        binding.fillLinearLayout[i].findViewById<Spinner>(R.id.fill_form_spinner).selectedItem.toString()
                            .trim()
                }
            }
                if (inputText.isNotEmpty() && inputText != "") {
                    map[formFields[i].label!!] = inputText
                    database.child(formName!!)
                        .child(AppController.instance.user?.email!!.split("@")[0])
                        .setValue(map)
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                app.finish()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(app, "Cannot submit form!", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(
                        app,
                        formFields[i].label + " field is empty!",
                        Toast.LENGTH_SHORT
                    ).show()
                    break
                }
        }

    }

}