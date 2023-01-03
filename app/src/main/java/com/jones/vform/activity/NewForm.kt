package com.jones.vform.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.size
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jones.vform.R
import com.jones.vform.databinding.ActivityNewFormBinding
import com.jones.vform.databinding.RowAddFieldBinding
import com.jones.vform.viewmodel.NewFormViewModel
import com.jones.vform.viewmodel.NewFormViewModelFactory

class NewForm : AppCompatActivity() {

    private val TAG = "NewFormInfo"

    private lateinit var binding: ActivityNewFormBinding
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var database: DatabaseReference

    private lateinit var newFormViewModel: NewFormViewModel
    private lateinit var newFormViewmodelFactory: NewFormViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newFormViewmodelFactory = NewFormViewModelFactory(this, binding)
        newFormViewModel =
            ViewModelProvider(this, newFormViewmodelFactory)[NewFormViewModel::class.java]
        database = Firebase.database.getReference("Forms")

        val types = arrayOf(EditText::class.simpleName, Spinner::class.simpleName)
        spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        binding.formName.requestFocus()
        binding.buttonAdd.setOnClickListener {
            var preText = "Initial"
            if (binding.layoutList.size > 0) {
                preText =
                    binding.layoutList[binding.layoutList.size - 1].findViewById<EditText>(R.id.field_label).text.toString()
                        .trim()
            }
            if (preText == "" || preText.isEmpty()) {
                Toast.makeText(this, "Previous Field is empty!", Toast.LENGTH_SHORT).show()
            } else {
                val newField = RowAddFieldBinding.inflate(LayoutInflater.from(this))
                newField.fieldLabel.requestFocus()
                newField.typeSpinner.adapter = spinnerAdapter
                newField.typeSpinner.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (position == 1) {
                            newField.spinnerListEt.visibility = View.VISIBLE
                        } else {
                            newField.spinnerListEt.visibility = View.GONE
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
//                        TODO("Not yet implemented")
                    }

                }
                newField.rowAddDeleteBt.setOnClickListener {
                    binding.layoutList.removeView(newField.root)
                }
                binding.layoutList.addView(newField.root)
            }
        }

        binding.buttonCreate.setOnClickListener {
            val key = newFormViewModel.createButtonClickListener()
            if (key!= null) {
                setClipboard(this,key)
                this.finish()
            }
        }
    }

    private fun setClipboard(context: Context, text: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as android.text.ClipboardManager
            clipboard.text = text
        } else {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", text)
            clipboard.setPrimaryClip(clip)
        }
        Toast.makeText(this,"Key copied to clipboard!", Toast.LENGTH_SHORT ).show()
    }

/*
    private fun addSpinnerItem(newField: RowAddFieldBinding) {
        val newSpinnerItem = SpinnerDatarowBinding.inflate(LayoutInflater.from(this))
        newSpinnerItem.addItemBt.setOnClickListener {
            val preItem = newField.spinnerItemLL[newField.spinnerItemLL.size - 1]
                .findViewById<EditText>(R.id.spinner_itemName).toString().trim()
            if ( preItem == "" || preItem.isEmpty()) {
                Toast.makeText(this, "Previous Field is empty!", Toast.LENGTH_SHORT).show()
            } else {
                addSpinnerItem(newField)
            }
        }
        newField.spinnerItemLL.addView(newSpinnerItem.root)
    }
*/
}