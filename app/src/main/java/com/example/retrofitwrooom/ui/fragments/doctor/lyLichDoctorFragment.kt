package com.example.retrofitwrooom.ui.fragments.doctor

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
import com.example.retrofitwrooom.databinding.FragmentAddDoctorBinding
import com.example.retrofitwrooom.databinding.FragmentDoctorBinding
import com.example.retrofitwrooom.databinding.FragmentLyLichDoctorBinding
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.viewModel.doctorViewModel
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class lyLichDoctorFragment : Fragment() {

    private lateinit var viewModel: doctorViewModel
    private var _binding: FragmentLyLichDoctorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLyLichDoctorBinding.inflate(inflater, container, false)

        binding.imgBack.setOnClickListener {
            findNavController().navigate(R.id.action_lyLichDoctorFragment_to_mainDoctorFragment)
        }
        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val doctor = arguments?.getParcelable<doctor>("doctor")
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


        binding.imgDriection2.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("doctor", doctor) // doctor là đối tượng cần truyền
            }
            findNavController().navigate(R.id.action_lyLichDoctorFragment_to_callFragment,bundle)
        }
        binding.imgMess.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("doctor", doctor) // doctor là đối tượng cần truyền
            }
         findNavController().navigate(R.id.action_lyLichDoctorFragment_to_detailsFragment,bundle)
        }
        binding.web.setOnClickListener {

        }

        binding.imgDriection.setOnClickListener {
            val address = doctor?.password ?: return@setOnClickListener
            val gmmIntentUri = Uri.parse("geo:0,0?q=$address")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(mapIntent)
            }
        }
        binding.imgShare.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("doctor", doctor) // doctor là đối tượng cần truyền
            }
            findNavController().navigate(R.id.action_lyLichDoctorFragment_to_shareFragment,bundle)
        }




    }

}