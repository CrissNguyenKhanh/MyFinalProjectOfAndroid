package com.example.retrofitwrooom.ui.fragments.doctor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.adapter.benhNhanAdapter
import com.example.retrofitwrooom.adapter.doctorAdapter
import com.example.retrofitwrooom.databinding.FragmentAddDoctorBinding
import com.example.retrofitwrooom.databinding.FragmentBenhNhanBinding
import com.example.retrofitwrooom.databinding.FragmentDoctorBinding
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.model.request.doctorFilter
import com.example.retrofitwrooom.repository.DoctorRepository
import com.example.retrofitwrooom.repository.benhNhanRepository
import com.example.retrofitwrooom.viewModel.benhNhanViewModel
import com.example.retrofitwrooom.viewModel.doctorViewModel
import com.example.retrofitwrooom.viewModel.viewModelFactory.benhNhanViewmodelFactory
import com.example.retrofitwrooom.viewModel.viewModelFactory.doctorViewmodelFactory


class doctorFragment : Fragment() {

    private lateinit var viewModel: doctorViewModel
    private var _binding: FragmentDoctorBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: doctorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentDoctorBinding.inflate(inflater, container, false)



        adapter = doctorAdapter(
            emptyList(),
            onlyLichClick = {doctor ->
                if (doctor != null) {
                    val bundle = Bundle().apply {
                        putParcelable("doctor", doctor)
                    }
                    findNavController().navigate(R.id.action_doctorFragment_to_lyLichDoctorFragment, bundle)
                } else {

                    Toast.makeText(requireContext(), "Không tìm thấy bác sĩ!", Toast.LENGTH_SHORT).show()
                }
            },
            {doctor:doctor, i: Int ->
                Toast.makeText(requireContext(),
                    "you have click on doctor name : " + doctor.fullName,
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        val departmentId = arguments?.getInt("departmentid") ?: 0 // gán mặc định 0 nếu null
        binding.recyclerViewDoctor.adapter = adapter
        binding.recyclerViewDoctor.layoutManager = LinearLayoutManager(requireContext())

        val vmFactory = doctorViewmodelFactory(DoctorRepository())

        viewModel = ViewModelProvider(this, vmFactory)[doctorViewModel::class.java]
        val khanh = doctorFilter(departmentId)
        viewModel.getPostItem(khanh)


        viewModel.responseData.observe(viewLifecycleOwner){data ->
            val result = data.body() ?: emptyList()
            adapter.updateData(result)
        }


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val departmentId = arguments?.getInt("departmentid") ?: 0

        binding.imageBackto.setOnClickListener {
            if (departmentId == 1) {
                findNavController().navigate(R.id.action_doctorFragment_to_mainDoctorFragment)
            } else if (departmentId == 2) {
                findNavController().navigate(R.id.action_doctorFragment_to_mainDoctorNaoFragment)
            } else if (departmentId == 3) {
                findNavController().navigate(R.id.action_doctorFragment_to_benhNhanTimFragment)
            } else if (departmentId == 4) {
                findNavController().navigate(R.id.action_doctorFragment_to_doctorPhoiFragment)
            }
        }
        val khanh = doctorFilter(departmentId)
        viewModel.getPostItem(khanh)
        binding.floatingActionButton3.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("departmentId" ,departmentId)
            }

            findNavController().navigate(R.id.action_doctorFragment_to_addDoctorFragment,bundle)
        }
    }



}