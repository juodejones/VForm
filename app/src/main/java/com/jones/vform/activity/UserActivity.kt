package com.jones.vform.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jones.vform.adapter.UserListRecyclerViewAdapter
import com.jones.vform.data.AppController
import com.jones.vform.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity(), UserListRecyclerViewAdapter.UserCardClickListener {

    private lateinit var binding: ActivityUserBinding
    private lateinit var recyclerViewAdapter: UserListRecyclerViewAdapter
    private var users: MutableLiveData<List<String?>> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val formName = intent.getStringExtra("FormName")

        users.observe(this) {
            recyclerViewAdapter = UserListRecyclerViewAdapter(it, this)
            binding.userListRv.adapter = recyclerViewAdapter
        }

        AppController.instance.dataDB?.child(formName!!)
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val list: MutableList<String?> = mutableListOf()
                        for (c in snapshot.children) {
                            list.add(c.key)
                        }
                        users.postValue(list)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    override fun onItemClick(userId: String?) {
        TODO("Not yet implemented")
    }

    override fun onDeleteClick(userId: String?) {
        TODO("Not yet implemented")
    }
}