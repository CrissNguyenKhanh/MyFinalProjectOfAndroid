package com.example.retrofitwrooom.ui.fragments.doctor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.FragmentDetailsBinding
import com.example.retrofitwrooom.databinding.FragmentLyLichDoctorBinding
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.viewModel.doctorViewModel


class DetailsFragment : Fragment() {
    private lateinit var viewModel: doctorViewModel
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val doctor = arguments?.getParcelable<doctor>("doctor")

        Glide.with(binding.imageView19.context)
            .load(doctor?.image)
            .placeholder(R.drawable.khanhxingai)
            .error(R.drawable.doctor)
            .into(binding.imageView19)
        binding.imageView19

        binding.button.setOnClickListener{
            val phoneNumber = doctor?.phoneNumber ?: return@setOnClickListener
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("sms:$phoneNumber")
                putExtra("sms_body", "Chào bác sĩ!")
            }
            startActivity(intent)
        }

        return binding.root
    }


}