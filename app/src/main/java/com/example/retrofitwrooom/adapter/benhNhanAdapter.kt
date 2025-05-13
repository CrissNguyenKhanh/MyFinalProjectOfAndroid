package com.example.retrofitwrooom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.ItemBenhnhanBinding
import com.example.retrofitwrooom.model.benhNhan
import com.example.retrofitwrooom.model.doctor
import java.text.SimpleDateFormat
import java.util.Locale


class benhNhanAdapter(
     var data:List<benhNhan> ,
     private val onEditClick: (benhNhan) -> Unit,
     private val onDeleteClick: (benhNhan) -> Unit,
     private val onBenhAn: (benhNhan) -> Unit


) : RecyclerView.Adapter<benhNhanAdapter.ViewHolder>() {
     inner class ViewHolder(var benhNhanItemBinding: ItemBenhnhanBinding ) :
          RecyclerView.ViewHolder(benhNhanItemBinding.root) {
          val context = itemView.context
          fun bindItem(benh_Nhan: benhNhan) {
               benhNhanItemBinding.doctor.text =  "Bác sĩ phụ trách:BS."+ benh_Nhan.doctor.fullName
               val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
               benhNhanItemBinding.Role.text = "Ngày Sinh:"  +formatter.format(benh_Nhan.dob)
               benhNhanItemBinding.txtDepartMent.text = "giới tính:" +benh_Nhan.gender
               benhNhanItemBinding.phone.text = "SDT:"+ benh_Nhan.phoneNumber
               benhNhanItemBinding.txtName.text ="Bệnh Nhân: "+benh_Nhan.fullName
               Glide.with(benhNhanItemBinding.imageView5.context)
                    .load(benh_Nhan.image)
                    .placeholder(R.drawable.doctor)
                    .error(R.drawable.doctor)
                    .into(benhNhanItemBinding.imageView5)
               benhNhanItemBinding.btnDelete.setOnClickListener {
                    onDeleteClick(benh_Nhan)
               }
               benhNhanItemBinding.btnEdit.setOnClickListener {
                    onEditClick(benh_Nhan)
               }
               benhNhanItemBinding.btnprint.setOnClickListener {
                    onBenhAn(benh_Nhan)
               }
               benhNhanItemBinding.description.text = benh_Nhan.description
               if(benh_Nhan.checkKham == true){
                    benhNhanItemBinding.statusText.text = "Tình Trạng : Đã Khám"
               }else{
                    benhNhanItemBinding.statusText.text = "Tình Trạng : Chưa Khám"
                    benhNhanItemBinding.statusText.setTextColor(ContextCompat.getColor(context, R.color.red))
                    benhNhanItemBinding.statusText.setCompoundDrawablesWithIntrinsicBounds(
                         R.drawable.ic_cancel, // icon ❌
                         0, 0, 0
                    )
               }
          }
     }

     override fun getItemCount(): Int {
          return data.size
     }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
          val view = ItemBenhnhanBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
          return ViewHolder(view)
     }

     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
          holder.bindItem(data[position])
     }

     fun updateData(newData: List<benhNhan>) {
           data = newData
           notifyDataSetChanged()
     }
}