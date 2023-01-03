package com.jones.vform.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.lifecycle.ViewModelProvider
import com.jones.vform.R
import com.jones.vform.adapter.FormListRecyclerViewAdapter
import com.jones.vform.data.AppController
import com.jones.vform.data.Form
import com.jones.vform.databinding.ActivityMainBinding
import com.jones.vform.viewmodel.MainViewModel
import com.jones.vform.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity(), FormListRecyclerViewAdapter.FormCardClickListener {

    val TAG = "LogdInMainActivity"

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerViewAdapter: FormListRecyclerViewAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory = MainViewModelFactory(this, binding)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
//        Log.d(TAG, "onCreate: ${intent.getStringExtra("username")}")

        viewModel.forms.observe(this) {
            Log.d(TAG, "onCreate: ${it.size}")
            recyclerViewAdapter = FormListRecyclerViewAdapter(it, this)
            binding.formListRecView.adapter = recyclerViewAdapter
        }
        viewModel.getForms()
        binding.newFab.setOnClickListener {
            startActivity(Intent(this, NewForm::class.java))
        }

    }

    override fun onItemClick(form: Form?) {
        if (form?.creator == AppController.instance.user?.email) {
            val intent = Intent(this,UserActivity::class.java)
            intent.putExtra("FormName", form?.name)
            startActivity(intent)
        } else {
            val intent = Intent(this, FillForm::class.java)
            intent.putExtra("FormName", form?.name)
            startActivity(intent)
        }
    }

    override fun onDeleteClick(form: Form?) {
        viewModel.deleteForm(form)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }
}