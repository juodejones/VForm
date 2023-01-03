package com.jones.vform.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.jones.vform.data.AppController
import com.jones.vform.data.Form
import com.jones.vform.databinding.ActivityMainBinding

class MainViewModel(val app: AppCompatActivity, val binding: ActivityMainBinding) :
    AndroidViewModel(app.application) {

    private val formsDB: DatabaseReference = AppController.instance.formsDB!!
    var forms: MutableLiveData<List<Form?>> = MutableLiveData()

    fun getForms() {
        formsDB.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Form?>()
                forms.postValue(list)
                if(snapshot.exists()){
                    for(c in snapshot.children){
                        Log.d("TAG", "onChildAdded: ${c.key}")
                        list.add(c.getValue(Form::class.java)!!)
                    }
                    forms.postValue(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun deleteForm(form: Form?) {
        val key: String? = AppController.instance.keys.value?.get(form?.name)
        formsDB.child(key!!).removeValue()
    }
}