package com.example.retrofitwrooom.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.FragmentLogin2Binding
import com.example.retrofitwrooom.repository.DoctorRepository
import com.example.retrofitwrooom.repository.benhNhanRepository
import com.example.retrofitwrooom.viewModel.benhNhanViewModel
import com.example.retrofitwrooom.viewModel.doctorViewModel
import com.example.retrofitwrooom.viewModel.viewModelFactory.benhNhanViewmodelFactory
import com.example.retrofitwrooom.viewModel.viewModelFactory.doctorViewmodelFactory



class login2Fragment : Fragment() {
    private lateinit var doctor_viewmodel: doctorViewModel
    private lateinit var benhnhan_viewmodel: benhNhanViewModel
    private var _binding: FragmentLogin2Binding? = null
    private val binding get() = _binding!!

    private var isPatient = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentLogin2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vmFactory = doctorViewmodelFactory(DoctorRepository())
        doctor_viewmodel = ViewModelProvider(this, vmFactory).get(doctorViewModel::class.java)



        val vmFactory2 = benhNhanViewmodelFactory(benhNhanRepository())
        benhnhan_viewmodel = ViewModelProvider(this, vmFactory2).get(benhNhanViewModel::class.java)

        binding.txtcheckbenhnah.setOnClickListener {
            isPatient = !isPatient
            if (isPatient) {
                binding.layoutNormalLogin.visibility = View.GONE
                binding.layoutPatientLogin.visibility = View.VISIBLE
                binding.txtcheckbenhnah.text = "I'm not a patient"
            } else {
                binding.layoutNormalLogin.visibility = View.VISIBLE
                binding.layoutPatientLogin.visibility = View.GONE
                binding.txtcheckbenhnah.text = "I'm a patient"
            }
        }

        binding.btnLogin.setOnClickListener {
            if (!isPatient) {
                // Login cho bác sĩ
                val txtUserName = binding.textUserName.text.toString()
                val txtPassWord = binding.textPassWord.text.toString()
                doctor_viewmodel.login(txtUserName, txtPassWord)

                doctor_viewmodel.loginSuccess.observe(viewLifecycleOwner) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(requireContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                        doctor_viewmodel.loginFind(txtUserName)
                    } else {
                        Toast.makeText(requireContext(), "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show()
                    }
                }

                doctor_viewmodel.currentDoctor.observe(viewLifecycleOwner) { doctor ->
                    if (doctor != null) {
                        val bundle = Bundle().apply {
                            putParcelable("doctor", doctor)
                        }
                        findNavController().navigate(R.id.action_login2Fragment_to_mainFragment, bundle)
                    } else {
                        Toast.makeText(requireContext(), "Không tìm thấy bác sĩ!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Login cho bệnh nhân (chưa có logic - bạn có thể bổ sung thêm sau)
                val email = binding.editTextEmail.text.toString()
                val patientId = binding.editTextPatientID.text.toString()
                Toast.makeText(requireContext(), "Login bệnh nhân: $email - $patientId", Toast.LENGTH_SHORT).show()
                benhnhan_viewmodel.login(email , patientId )
                benhnhan_viewmodel.loginSuccess.observe(viewLifecycleOwner) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(requireContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                        benhnhan_viewmodel.loginFind(email)
                    } else {
                        Toast.makeText(requireContext(), "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show()
                    }
                }
                benhnhan_viewmodel.currentDoctor.observe(viewLifecycleOwner) { bennhan ->
                    if (bennhan != null) {
                        val bundle = Bundle().apply {
                            putParcelable("bennhan", bennhan)
                        }
                        findNavController().navigate(R.id.action_login2Fragment_to_loginbenhnhanFragment, bundle)
                    } else {
                        Toast.makeText(requireContext(), "Không tìm thấy bác sĩ!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
