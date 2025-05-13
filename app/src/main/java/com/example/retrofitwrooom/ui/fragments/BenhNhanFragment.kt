package com.example.retrofitwrooom.ui.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.adapter.benhNhanAdapter
import com.example.retrofitwrooom.databinding.FragmentBenhNhanBinding
import com.example.retrofitwrooom.databinding.FragmentBenhnhanUpdateBinding
import com.example.retrofitwrooom.databinding.FragmentLogin2Binding
import com.example.retrofitwrooom.model.benhNhan
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.model.request.benhnhanFind
import com.example.retrofitwrooom.repository.benhNhanRepository
import com.example.retrofitwrooom.viewModel.benhNhanViewModel
import com.example.retrofitwrooom.viewModel.doctorViewModel
import com.example.retrofitwrooom.viewModel.viewModelFactory.benhNhanViewmodelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BenhNhanFragment : Fragment() {
    private lateinit var viewModel: benhNhanViewModel
    private var _binding: FragmentBenhNhanBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: benhNhanAdapter
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBenhNhanBinding.inflate(inflater, container, false)
        binding.imageView18.setOnClickListener {
            findNavController().navigate(R.id.action_benhNhanFragment_to_mainFragment)
        }
        adapter = benhNhanAdapter(
            emptyList(),
            onEditClick = { benhnhan ->
                val bundle = Bundle().apply {
                    putLong("id", benhnhan.id)
                    putString("fullName", benhnhan.fullName)
                    putString("address", benhnhan.address)
                    putString("phoneNumber", benhnhan.phoneNumber)
                    putString("email", benhnhan.email)
                    putString("gender", benhnhan.gender)
                    putSerializable("dob", benhnhan.dob)
                    putParcelable("doctor", benhnhan.doctor)
                    putString("image", benhnhan.image)
                    putBoolean("checkKham", benhnhan.checkKham)

                }

                findNavController().navigate(R.id.action_benhNhanFragment_to_benhnhanUpdateFragment, bundle)
            },
            onDeleteClick = { benhnhan ->
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Confirm Window")
                    setMessage("Bạn có muốn xóa ${benhnhan.fullName} không?")
                    setNegativeButton("Không") { dialog, _ -> dialog.dismiss() }
                    setPositiveButton("Có") { _, _ -> viewModel.deleteBenhNhan(benhnhan.id) }
                    setCancelable(false)
                    show()
                }
            },
            onBenhAn = { benhnhan ->
                val bundle = Bundle().apply {
                    putLong("id", benhnhan.id)
                    putString("fullName", benhnhan.fullName)
                    putString("address", benhnhan.address)
                    putString("phoneNumber", benhnhan.phoneNumber)
                    putString("email", benhnhan.email)
                    putString("gender", benhnhan.gender)
                    putSerializable("dob", benhnhan.dob)
                    putParcelable("doctor", benhnhan.doctor)
                    putString("image", benhnhan.image)
                    putBoolean("checkKham", benhnhan.checkKham)
                    putString("descs", benhnhan.description)
                }

                findNavController().navigate(R.id.action_benhNhanFragment_to_hosobenhanFragment, bundle)
            }
        )

        binding.recyclerSearch.adapter = adapter
        binding.recyclerSearch.layoutManager = LinearLayoutManager(requireContext())
        val doctor = arguments?.getParcelable<doctor>("doctor")
        val vmFactory = benhNhanViewmodelFactory(benhNhanRepository())
        viewModel = ViewModelProvider(this, vmFactory)[benhNhanViewModel::class.java]
//        viewModel.getPostItem()
        viewModel.getPostItembydoctorid(doctor?.id ?: 1)

        // Gọi chung response handler
       viewModel.responseDatadoctorid.observe(viewLifecycleOwner) { res -> handleResponse(res) }
//        viewModel.responseData.observe(viewLifecycleOwner) { res -> handleResponse(res) }
        viewModel.responseDataFind.observe(viewLifecycleOwner) { res -> handleResponse(res) }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_benhNhanFragment_to_addBenhNhanFragment)
        }

        binding.searchEdit.addTextChangedListener(object : TextWatcher {
            val doctor = arguments?.getParcelable<doctor>("doctor")
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                val benhnhanFind = benhnhanFind(query)
                searchJob?.cancel()
                searchJob = MainScope().launch {
                    delay(300)
                    if (query.isNotBlank()) {
                        viewModel.findBenhNhan(benhnhanFind)
                    } else {
                        viewModel.getPostItembydoctorid(doctor?.id ?: 1)
//                        viewModel.getPostItem()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun handleResponse(res: retrofit2.Response<List<benhNhan>>) {
        if (res.isSuccessful) {
            val result = res.body() ?: emptyList()
            adapter.updateData(result)
            binding.itemSearchError.visibility = if (result.isEmpty()) View.VISIBLE else View.GONE
        } else {
            binding.itemSearchError.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

