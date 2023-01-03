package com.jones.vform.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jones.vform.R
import com.jones.vform.databinding.ActivityViewFormBinding

class ViewForm : AppCompatActivity() {

    private lateinit var binding: ActivityViewFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}