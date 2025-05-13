package com.example.retrofitwrooom.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.adapter.appointmentAdapter
import com.example.retrofitwrooom.adapter.benhNhanAdapter
import com.example.retrofitwrooom.databinding.FragmentAddApointBinding
import com.example.retrofitwrooom.databinding.FragmentAppointmentDetailsBinding
import com.example.retrofitwrooom.databinding.FragmentFullthongtinvedoctorBinding
import com.example.retrofitwrooom.model.appointment
import com.example.retrofitwrooom.repository.appointmentRepository
import com.example.retrofitwrooom.repository.benhNhanRepository
import com.example.retrofitwrooom.viewModel.appointmentViewmodel
import com.example.retrofitwrooom.viewModel.benhNhanViewModel
import com.example.retrofitwrooom.viewModel.doctorViewModel
import com.example.retrofitwrooom.viewModel.viewModelFactory.appointmentViewmodelFacotry
import com.example.retrofitwrooom.viewModel.viewModelFactory.benhNhanViewmodelFactory

class appointmentDetailsFragment  : Fragment() {

    private lateinit var viewModel: appointmentViewmodel
    private var _binding: FragmentAppointmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: appointmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentDetailsBinding.inflate(inflater, container, false)

        adapter = appointmentAdapter(emptyList())

//        binding.recyclerView2.apply {
//            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//            setHasFixedSize(true)
//            adapter = this@appointmentDetailsFragment.adapter
//        }

//       binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        val staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView2.layoutManager = staggeredLayoutManager
        binding.recyclerView2.adapter = adapter // <-- bạn QUÊN dòng này
        val vmFactory = appointmentViewmodelFacotry(appointmentRepository())
        viewModel = ViewModelProvider(this, vmFactory)[appointmentViewmodel::class.java]
        viewModel.getPostItem()

        viewModel.responseData.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                val result = response.body() ?: emptyList()
                adapter.updateData(result)
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Lỗi")
                    .setMessage("Không thể tải lịch hẹn.")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgBack.setOnClickListener {
            findNavController().navigate(R.id.action_appointmentDetailsFragment2_to_loginbenhnhanFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
