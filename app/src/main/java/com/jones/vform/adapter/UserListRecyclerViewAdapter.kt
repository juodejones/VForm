package com.jones.vform.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jones.vform.R

class UserListRecyclerViewAdapter(
    val users: List<String?>,
    val cardClickListener: UserCardClickListener
) : RecyclerView.Adapter<UserListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_card, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (users.isNotEmpty()) {
            val user = users[position]
            holder.userMailTV.text = user
            holder.itemView.setOnClickListener {
                cardClickListener.onItemClick(user)
            }
            holder.deleteBt.setOnClickListener {
                cardClickListener.onDeleteClick(user)
            }
        }
    }

    override fun getItemCount(): Int = users.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val userMailTV: TextView = itemView.findViewById(R.id.card_user_mail)
        val deleteBt: ImageButton = itemView.findViewById(R.id.user_card_delete)
    }

    interface UserCardClickListener {
        fun onItemClick(userId: String?)
        fun onDeleteClick(userId: String?)
    }
}