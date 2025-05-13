package com.example.retrofitwrooom.ui.fragments.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.adapter.doctormainAdapter
import com.example.retrofitwrooom.databinding.FragmentDoctorPhoiBinding
import com.example.retrofitwrooom.databinding.FragmentMainDoctorBinding
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.model.request.doctorFilter
import com.example.retrofitwrooom.repository.DoctorRepository
import com.example.retrofitwrooom.viewModel.doctorViewModel
import com.example.retrofitwrooom.viewModel.viewModelFactory.doctorViewmodelFactory

class DoctorPhoiFragment : Fragment() {

    private lateinit var viewModel: doctorViewModel
    private var _binding: FragmentDoctorPhoiBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: doctormainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorPhoiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Khởi tạo ViewModel với Factory
        val factory = doctorViewmodelFactory(DoctorRepository())
        viewModel = ViewModelProvider(this, factory)[doctorViewModel::class.java]

        setupRecyclerView()
        binding.imageBack.setOnClickListener {
            findNavController().navigate(R.id.action_doctorPhoiFragment_to_mainFragment)
        }
        // Gửi filter để gọi API
        val filter = doctorFilter(4)
        viewModel.getPostItem(filter)

        // Quan sát dữ liệu trả về
        viewModel.responseData.observe(viewLifecycleOwner) { response ->
            val doctors = response.body() ?: emptyList()
            adapter.updateData(doctors)
        }

        binding.seeAll.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("departmentid" , 4)
            }
            findNavController().navigate(R.id.action_doctorPhoiFragment_to_doctorFragment , bundle)
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
