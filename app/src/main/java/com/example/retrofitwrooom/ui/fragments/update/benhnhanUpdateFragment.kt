package com.example.retrofitwrooom.ui.fragments.update

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.FragmentBenhnhanUpdateBinding
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.model.update.BenhNhanUpdate
import com.example.retrofitwrooom.repository.benhNhanRepository
import com.example.retrofitwrooom.viewModel.benhNhanViewModel
import com.example.retrofitwrooom.viewModel.viewModelFactory.benhNhanViewmodelFactory
import java.text.SimpleDateFormat
import java.util.*

class benhnhanUpdateFragment : Fragment() {

    private lateinit var viewModel: benhNhanViewModel
    private var _binding: FragmentBenhnhanUpdateBinding? = null
    private val binding get() = _binding!!
    private var doctorList: List<doctor> = listOf()
    // Các biến nhận từ Bundle
    private var id: Long = 0
    private var fullName: String? = null
    private var address: String? = null
    private var phoneNumber: String? = null
    private var email: String? = null
    private var gender: String? = null
    private var dob: Date? = null
    private var doctor: doctor? = null
    private var image: String? = null
    private var checkKham: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBenhnhanUpdateBinding.inflate(inflater, container, false)
        // Nhận dữ liệu từ Bundle
        val vmFactory = benhNhanViewmodelFactory(benhNhanRepository())
        viewModel = ViewModelProvider(this, vmFactory).get(benhNhanViewModel::class.java)
        viewModel.getPostItemDoctor()


        arguments?.let {
            id = it.getLong("id")
            fullName = it.getString("fullName")
            address = it.getString("address")
            phoneNumber = it.getString("phoneNumber")
            email = it.getString("email")
            gender = it.getString("gender")
            dob = it.getSerializable("dob") as? Date
            doctor = it.getParcelable("doctor")
            image = it.getString("image")
            checkKham = it.getBoolean("checkKham", false) // mặc định là false nếu null
        }

        // Binding dữ liệu ra giao diện
        binding.editFullName.setText(fullName)
        binding.editAddress.setText(address)
        binding.editPhone.setText(phoneNumber)
        binding.editEmail.setText(email)
        binding.editGender.setText(gender)
        binding.editDob.setText(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(dob ?: Date()))

        viewModel.allDocor.observe(viewLifecycleOwner) { response ->
            val doctorList = response.body()
            val adapterItems = doctorList?.map { "${it.id} - ${it.fullName}" } ?: emptyList()
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, adapterItems)
            binding.spinnerDoctor.adapter = adapter
            // Sau khi đã gán adapter xong mới setSelection
            val selectedDoctorId = doctor?.id
            val index = doctorList?.indexOfFirst { it.id == selectedDoctorId }?: 0L
            binding.spinnerDoctor.setSelection(index.toInt())

        }

        Glide.with(requireContext())
            .load(image)
            .placeholder(R.drawable.doctor)
            .error(R.drawable.doctor)
            .into(binding.imageView)

        // Gán RadioButton
        binding.daKham.isChecked = checkKham
        binding.chuaKham.isChecked = !checkKham

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUpdate.setOnClickListener {
            val fullName = binding.editFullName.text.toString().trim()
            val address = binding.editAddress.text.toString().trim()
            val phoneNumber = binding.editPhone.text.toString().trim()
            val email = binding.editEmail.text.toString().trim()
            val gender = binding.editGender.text.toString().trim()
            val selectedDoctor = binding.spinnerDoctor.selectedItem?.toString()
            val doctorId = selectedDoctor?.split(" - ")?.get(0)?.toLongOrNull() ?: 0L
            val dobString = binding.editDob.text.toString().trim()

            // Validate ngày sinh
            val dob: Date = try {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dobString)!!
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Ngày sinh không hợp lệ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate dữ liệu bắt buộc
            if (fullName.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ họ tên và số điện thoại", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val checkKham = binding.daKham.isChecked

            val benhNhanUpdate = BenhNhanUpdate(
                fullName = fullName,
                dob = dob,
                address = address,
                phoneNumber = phoneNumber,
                email = email,
                doctorId = doctorId,
                gender = gender,
                checkKham = checkKham
            )

            viewModel.updateBenhNhan(id, benhNhanUpdate)
            viewModel.getPostItembydoctorid(1)
            Toast.makeText(requireContext(), "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_benhnhanUpdateFragment_to_benhNhanFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
