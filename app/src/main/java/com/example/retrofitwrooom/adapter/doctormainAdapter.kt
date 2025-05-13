package com.example.retrofitwrooom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.ItemDoctorBinding
import com.example.retrofitwrooom.databinding.ItemdoctormainBinding
import com.example.retrofitwrooom.model.doctor
import kotlin.random.Random

class doctormainAdapter(
    var data:List<doctor>,
    private  val  listener :(doctor , Int) ->Unit
    ): RecyclerView.Adapter<doctormainAdapter.ViewHolder>() {

    inner class ViewHolder(var doctorItemdoctormainBinding: ItemdoctormainBinding) :
        RecyclerView.ViewHolder(doctorItemdoctormainBinding.root) {
        fun bindItem(doctor: doctor) {
           doctorItemdoctormainBinding.textView12.text = "Name:BS." +doctor.fullName
           doctorItemdoctormainBinding.textView15.text = "Khoa" +doctor.department?.name
            Glide.with(doctorItemdoctormainBinding.imageView6.context)
                .load(doctor.image)
                .placeholder(R.drawable.doctor)
                .error(R.drawable.doctor)
                .into(doctorItemdoctormainBinding.imageView6)


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemdoctormainBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position])
        holder.itemView.setOnClickListener{
            listener(data[position] , position)
        }
    }

    override fun getItemCount(): Int {
       return data.size
    }

    fun updateData(newData: List<doctor>) {
        data = newData
        notifyDataSetChanged()
    }

}


