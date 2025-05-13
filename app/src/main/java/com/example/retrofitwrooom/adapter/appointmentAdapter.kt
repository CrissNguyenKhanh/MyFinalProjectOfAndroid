package com.example.retrofitwrooom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.ItemAponitmentBinding
import com.example.retrofitwrooom.databinding.ItemBenhnhanBinding
import com.example.retrofitwrooom.model.appointment
import com.example.retrofitwrooom.model.benhNhan
import java.text.SimpleDateFormat
import java.util.Locale

class appointmentAdapter(
   var data:List<appointment>

) : RecyclerView.Adapter<appointmentAdapter.ViewHolder>() {
    inner class ViewHolder(var appointmentItemBinding: ItemAponitmentBinding) :
        RecyclerView.ViewHolder(appointmentItemBinding.root) {
        val context = itemView.context
        fun bindItem(appointment: appointment) {
            appointmentItemBinding.appTitle.text=  appointment.reason
            appointmentItemBinding.appDESC.text = appointment.status
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemAponitmentBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position])
    }

    fun updateData(newData: List<appointment>) {
        data = newData
        notifyDataSetChanged()
    }
}