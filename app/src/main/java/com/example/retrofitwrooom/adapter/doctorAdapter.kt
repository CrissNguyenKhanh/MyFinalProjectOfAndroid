package com.example.retrofitwrooom.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.ItemBenhnhanBinding
import com.example.retrofitwrooom.databinding.ItemDoctorBinding
import com.example.retrofitwrooom.model.benhNhan
import com.example.retrofitwrooom.model.doctor
import kotlin.random.Random

class doctorAdapter(
    var data:List<doctor>,
    private val onlyLichClick: (doctor) -> Unit,

    private  val  listener :(doctor , Int) ->Unit
):RecyclerView.Adapter<doctorAdapter.ViewHolder>() {

    inner class ViewHolder(var doctorItemBinding: ItemDoctorBinding ) :
        RecyclerView.ViewHolder(doctorItemBinding.root) {
        fun bindItem(doctor: doctor) {
            doctorItemBinding.txtName.text = doctor.fullName
            doctorItemBinding.Role.text = doctor.role
            doctorItemBinding.txtDepartMent.text = doctor.department?.name

            Glide.with(doctorItemBinding.imageView5.context)
                .load(doctor.image)
                .placeholder(R.drawable.doctor)
                .error(R.drawable.doctor)
                .into(doctorItemBinding.imageView5)

            // ⭐ Gán rating ngẫu nhiên từ 1.0 đến 5.0 (bước 0.5)
            val rating = Random.nextInt(2, 11) * 0.5f  // từ 1.0 đến 5.0
            doctorItemBinding.ratingBar.rating = rating

            // Gán text theo rating
            doctorItemBinding.textView6.text = String.format("%.1f", rating)


            doctorItemBinding.lyLichButton.setOnClickListener {
                onlyLichClick(doctor)
            }
        }

    }
    private fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemDoctorBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position])
        holder.itemView.setOnClickListener{
            listener(data[position] , position)
        }
    }

    fun updateData(newData: List<doctor>) {
        data = newData
        notifyDataSetChanged()
    }


}