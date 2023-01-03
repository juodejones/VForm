package com.jones.vform.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AppController : Application() {

    companion object {
        lateinit var instance: AppController
    }

    var user: FirebaseUser? = null
    var firebase: FirebaseDatabase? = null
    var formsDB: DatabaseReference? = null
    var dataDB: DatabaseReference? = null
    var keysDB: DatabaseReference? = null
    var keys: MutableLiveData<Map<String?, String?>> = MutableLiveData()
    var forms: MutableList<Form?> = mutableListOf()


    override fun onCreate() {
        super.onCreate()

        user = FirebaseAuth.getInstance().currentUser
        firebase = FirebaseDatabase.getInstance()
        formsDB = firebase!!.getReference("Forms")
        dataDB = firebase!!.getReference("Data")
        keysDB = firebase!!.getReference("Keys")

        instance = this

        keysDB!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val map = mutableMapOf<String?,String?>()
                    for (c in snapshot.children) {
                        Log.d("AppControllerTag", "onDataChange: ${c.value}")
                        map[c.key]= c.getValue(String::class.java)!!
                    }
                    keys.postValue(map)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        formsDB!!.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("AppControllerTag", "onChildAdded: ${snapshot.key}")
                forms.add(snapshot.getValue(Form::class.java))
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("AppControllerTag", "onChildChanged: ${snapshot.key}")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val form = snapshot.getValue(Form::class.java)
                Log.d("AppControllerTag", "onChildRemoved: ${snapshot.key}")
                forms.remove(form)
                keysDB?.child(form?.name!!)?.removeValue()
                dataDB?.child(form?.name!!)?.removeValue()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("AppControllerTag", "onChildMoved: ${snapshot.key}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("AppControllerTag", "onCancelled: ${error.message}")
            }
        })
    }
}