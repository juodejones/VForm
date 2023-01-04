package com.jones.vform.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.MutableLiveData
import com.jones.vform.R
import com.jones.vform.data.AppController
import com.jones.vform.data.Field
import com.jones.vform.databinding.ActivityViewFormBinding
import com.jones.vform.databinding.RowFormViewBinding

class ViewForm : AppCompatActivity() {

    private lateinit var binding: ActivityViewFormBinding
    private var fieldData: MutableLiveData<Map<String?, String?>> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val formName = intent.getStringExtra("formname")
        val userId = intent.getStringExtra("userid")

        if (formName != null && userId != null) {
            binding.viewFormTitle.text = buildString {
                append(getString(R.string.form_name))
                append("$formName\n")
                append(getString(R.string.filled_by))
                append(userId)
                append("\n\nData:")
            }

            AppController.instance.dataDB
                ?.child("$formName/$userId")
                ?.get()
                ?.addOnSuccessListener {
                    if (it.exists()) {
                        val map = mutableMapOf<String?, String?>()
                        for (c in it.children) {
                            Log.d("ViewFormTag", "onCreate: ${c.value}")
                            map[c.key] = c.getValue(String::class.java)
                        }
                        fieldData.postValue(map)
                    }
                }
        }

        fieldData.observe(this) {
            for (m in it.keys.reversed()) {
                Log.d("ViewFormTag", "onCreate: $m\t${it[m]}")
                val newField = RowFormViewBinding.inflate(LayoutInflater.from(this))
                newField.viewLabel.text = buildString {
                    append(m)
                    append(" : ")
                }
                newField.viewData.text = it[m]
                binding.viewLl.addView(newField.root)
            }
        }
    }
}