package com.jones.vform.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jones.vform.R
import com.jones.vform.data.AppController
import com.jones.vform.data.Form

class FormListRecyclerViewAdapter(
    private var forms: List<Form?>,
    private var formCardClickListener: FormCardClickListener
) : RecyclerView.Adapter<FormListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.form_card, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (forms.isNotEmpty()) {
            val item = forms[position]
            val creator = item?.creator
            holder.formName.text = item?.name.toString()
            holder.itemView.setOnClickListener{
                formCardClickListener.onItemClick(item!!)
            }
            holder.deleteButton.setOnClickListener {
                formCardClickListener.onDeleteClick(item!!)
            }
            if (creator != null) {
                if (creator == AppController.instance.user?.email) {
                    holder.deleteButton.visibility = View.VISIBLE
                }
            }

        }
    }

    override fun getItemCount(): Int = forms.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val formName: TextView = itemView.findViewById(R.id.card_form_name)
        val deleteButton: ImageButton = itemView.findViewById(R.id.form_card_delete)
    }

    interface FormCardClickListener {
        fun onItemClick(form: Form?)
        fun onDeleteClick(form: Form?)
    }

}