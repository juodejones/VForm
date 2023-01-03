package com.jones.vform.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.size
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.database.*
import com.jones.vform.R
import com.jones.vform.data.AppController
import com.jones.vform.data.Field
import com.jones.vform.data.Form
import com.jones.vform.databinding.ActivityNewFormBinding

class NewFormViewModel(val app: AppCompatActivity, val binding: ActivityNewFormBinding) :
    AndroidViewModel(app.application) {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val formsDB: DatabaseReference = database.getReference("Forms")
    private val keyDB: DatabaseReference = database.getReference("Keys")
    private var formKey: String? = null

    private fun createForm(name: String, formFields: List<Field>) {
        val newForm = Form(name, formFields, AppController.instance.user?.email)
        formKey = formsDB.push().key
        database.getReference("Forms/${formKey}").setValue(newForm)
            .addOnSuccessListener {
                keyDB.child(newForm.name.toString()).setValue(formKey)
                app.finish()
            }
            .addOnFailureListener {
                Toast.makeText(app, "Unable to create form", Toast.LENGTH_SHORT).show()
            }
        Log.d("Mytag", "createForm: $formKey")
    }

    fun createButtonClickListener(): String? {
        formKey = null
        val formName: String = binding.formName.text.toString()
        val size = binding.layoutList.size
        if (formName.isNotEmpty() && formName != "" && size > 0) {
            for (form in AppController.instance.forms) {
                if (formName == form?.name!!) {
                    Toast.makeText(app, "Form name exists!", Toast.LENGTH_SHORT).show()
                    return null
                }
            }
            val thisFormList = mutableListOf<Field>()
            for (i in 0 until size) {
                val v: View = binding.layoutList[i]
                val label = v.findViewById<EditText>(R.id.field_label)
                val dataType = v.findViewById<Spinner>(R.id.type_spinner)
                val spinnerItems =
                    v.findViewById<EditText>(R.id.spinner_list_et).text.toString().trim()
                        .split(',')
                val formField =
                    if (dataType.selectedItem.toString() == Spinner::class.simpleName) {
                        Field(
                            label.text!!.toString().trim(),
                            dataType.selectedItem.toString().trim(),
                            spinnerItems
                        )
                    } else {
                        Field(
                            label.text!!.toString().trim(),
                            dataType.selectedItem.toString().trim()
                        )
                    }
//                    Log.d(TAG, "onCreate: $formField")
                thisFormList.add(formField)
            }
            this.createForm(formName, thisFormList)
        } else {
//                Log.d(TAG, "onCreate: Data Upload Failed")
        }

        Log.d("Mytag", "createButtonClickListener: $formKey")
        return formKey
    }

}