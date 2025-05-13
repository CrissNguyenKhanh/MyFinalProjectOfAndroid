package com.example.retrofitwrooom.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.FragmentFullthongtinvedoctorBinding
import com.example.retrofitwrooom.databinding.FragmentLyLichDoctorBinding
import com.example.retrofitwrooom.model.benhNhan
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.viewModel.doctorViewModel
import kotlin.random.Random

class fullthongtinvedoctorFragment : Fragment() {


    private lateinit var viewModel: doctorViewModel
    private var _binding: FragmentFullthongtinvedoctorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFullthongtinvedoctorBinding.inflate(inflater, container, false)

        binding.imgBack.setOnClickListener {
            findNavController().navigate(R.id.action_fullthongtinvedoctorFragment_to_loginbenhnhanFragment)
        }
        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val doctor = arguments?.getParcelable<doctor>("doctor")
        val bennhan = arguments?.getParcelable<benhNhan>("benhnhan")
        binding.edtNameDOctor.text = "BS."+doctor?.fullName ?: "Không rõ tên"

        Glide.with(binding.imgeDoctor.context)
            .load(doctor?.image)
            .placeholder(R.drawable.doctor)
            .error(R.drawable.doctor)
            .into(binding.imgeDoctor)

        // ⭐ Gán rating ngẫu nhiên từ 1.0 đến 5.0 (bước 0.5)
        val rating = Random.nextInt(2, 11) * 0.5f  // từ 1.0 đến 5.0
        binding.rating.rating = rating
        binding.edtDepartmentDoctor.text = "Khoa Làm Việc:" +doctor?.department?.name
        binding.phoneNumber.text = "SDT liên hệ:" +doctor?.phoneNumber
        val randomNumber = (2..10).random() // số ngẫu nhiên từ 0 đến 100
        binding.edtExp.text =randomNumber.toString() +"+Year"
        binding.txtbio.text = "Bác sĩ ${doctor?.fullName} là một chuyên gia trong lĩnh vực Nội tổng quát với hơn 10 năm kinh nghiệm công tác tại các bệnh viện tuyến trung ương. Sau khi tốt nghiệp loại Giỏi từ Trường Đại học Y Hà Nội, bác sĩ tiếp tục hoàn thành chương trình đào tạo chuyên khoa cấp I."

        binding.btnCuocHen.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("doctor" , doctor)
                putParcelable("benhnhan",bennhan)
            }
            findNavController().navigate(R.id.action_fullthongtinvedoctorFragment_to_addApointFragment , bundle)

        }






    }

}