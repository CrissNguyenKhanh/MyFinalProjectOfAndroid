package com.example.retrofitwrooom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.ItemPendingappointmentBinding
import com.example.retrofitwrooom.model.appointment


class appointmentAdapter(
    var data: List<appointment>,
    private val onAccpetClick: (appointment) -> Unit,


    ) : RecyclerView.Adapter<appointmentAdapter.ViewHolder>() {
    inner class ViewHolder(var itemPendingappointmentBinding: ItemPendingappointmentBinding) :
        RecyclerView.ViewHolder(itemPendingappointmentBinding.root) {
        val context = itemView.context
        fun bindItem(appointment: appointment) {
            itemPendingappointmentBinding.tvReason.text = appointment.reason
            itemPendingappointmentBinding.tvStatus.text = appointment.status
            itemPendingappointmentBinding.imgAvatar.setImageResource(R.drawable.bell_icon)

            itemPendingappointmentBinding.btnAccept.setOnClickListener {
                onAccpetClick(appointment)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemPendingappointmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
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