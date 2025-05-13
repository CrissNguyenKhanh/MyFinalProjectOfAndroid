package com.example.retrofitwrooom.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.FragmentLoginBinding
import com.example.retrofitwrooom.repository.DoctorRepository
import com.example.retrofitwrooom.viewModel.doctorViewModel
import com.example.retrofitwrooom.viewModel.viewModelFactory.doctorViewmodelFactory
import kotlin.math.log

class loginFragment : Fragment() {


    private lateinit var doctor_viewmodel: doctorViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         val vmFactory =  doctorViewmodelFactory(DoctorRepository())
         doctor_viewmodel = ViewModelProvider(this, vmFactory).get(doctorViewModel::class.java)
         val txtUserName = binding.fullNameEt.text.toString()
         val txtPassWord = binding.passWOrdet.text.toString()
        binding.loginBtn.setOnClickListener {
            val txtUserName = binding.fullNameEt.text.toString()
            val txtPassWord = binding.passWOrdet.text.toString()
            doctor_viewmodel.login(txtUserName, txtPassWord)
        }
     // xu li bat dong bo
        doctor_viewmodel.loginSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(requireContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
//            findNavController().navigate(R.id.action_loginFragment_to_headlinesFragment)
            } else {
                Toast.makeText(requireContext(), "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show()
            }
        }


    }
}