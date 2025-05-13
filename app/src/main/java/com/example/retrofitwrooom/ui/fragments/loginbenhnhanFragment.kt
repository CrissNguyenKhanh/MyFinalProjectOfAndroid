package com.example.retrofitwrooom.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.adapter.doctormainAdapter
import com.example.retrofitwrooom.databinding.FragmentLoginbenhnhanBinding
import com.example.retrofitwrooom.databinding.FragmentMainDoctorBinding
import com.example.retrofitwrooom.model.benhNhan
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.model.request.doctorFilter
import com.example.retrofitwrooom.repository.DoctorRepository
import com.example.retrofitwrooom.viewModel.doctorViewModel
import com.example.retrofitwrooom.viewModel.viewModelFactory.doctorViewmodelFactory


class loginbenhnhanFragment : Fragment() {
    private lateinit var viewModel: doctorViewModel
    private var _binding: FragmentLoginbenhnhanBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: doctormainAdapter

    private var bennhan: benhNhan? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginbenhnhanBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Khởi tạo ViewModel với Factory
        val factory = doctorViewmodelFactory(DoctorRepository())
        viewModel = ViewModelProvider(this, factory)[doctorViewModel::class.java]
        bennhan = arguments?.getParcelable("bennhan")
        setupRecyclerView()
        binding.imageBack.setOnClickListener {
            findNavController().navigate(R.id.action_loginbenhnhanFragment_to_login2Fragment2)
        }
        // Gửi filter để gọi API
        val click:Int = 0;

        val filter = doctorFilter(1)
        viewModel.getPostItem(filter)

        // Quan sát dữ liệu trả về
        viewModel.responseData.observe(viewLifecycleOwner) { response ->
            val doctors = response.body() ?: emptyList()
            adapter.updateData(doctors)
        }
        binding.imageTeeth.setOnClickListener {
            val filter = doctorFilter(2)
            viewModel.getPostItem(filter)
        }
        binding.imageLung.setOnClickListener {
            val filter = doctorFilter(4)
            viewModel.getPostItem(filter)
        }
        binding.imageHeart.setOnClickListener {
            val filter = doctorFilter(3)
            viewModel.getPostItem(filter)
        }
        binding.imageBrain.setOnClickListener {
            val filter = doctorFilter(1)
            viewModel.getPostItem(filter)
        }

        binding.fabChat.setOnClickListener {
            findNavController().navigate(R.id.action_loginbenhnhanFragment_to_chatFragment)
        }
        binding.textchat.setOnClickListener {
            findNavController().navigate(R.id.action_loginbenhnhanFragment_to_chatFragment)
        }

    }

    private fun setupRecyclerView() {

        adapter = doctormainAdapter(
            emptyList()
        ) { doctor: doctor, _: Int ->
            Toast.makeText(
                requireContext(),
                "Bạn đã chọn bác sĩ: ${doctor.fullName}",
                Toast.LENGTH_SHORT
            ).show()
                val bundle = Bundle().apply {
                    putParcelable("doctor" , doctor)
                        putParcelable("benhnhan" , bennhan)
                }
                findNavController().navigate(R.id.action_loginbenhnhanFragment_to_fullthongtinvedoctorFragment , bundle)

        }

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
