package com.example.retrofitwrooom.ui.fragments.doctor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.FragmentAddBenhNhanBinding
import com.example.retrofitwrooom.databinding.FragmentAddDoctorBinding
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.model.request.doctorFilter
import com.example.retrofitwrooom.model.request.doctorRequest
import com.example.retrofitwrooom.repository.DoctorRepository
import com.example.retrofitwrooom.repository.benhNhanRepository
import com.example.retrofitwrooom.viewModel.benhNhanViewModel
import com.example.retrofitwrooom.viewModel.doctorViewModel
import com.example.retrofitwrooom.viewModel.viewModelFactory.benhNhanViewmodelFactory
import com.example.retrofitwrooom.viewModel.viewModelFactory.doctorViewmodelFactory
import kotlinx.coroutines.CoroutineStart
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.io.encoding.Base64


class addDoctorFragment : Fragment() {
    private lateinit var viewModel: doctorViewModel
    private var _binding: FragmentAddDoctorBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageView: ImageView
    private var imageFileName: String = "default.jpg"
    private var selectedImageUri: Uri? = null
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri ->
            imageView.setImageURI(uri)
            selectedImageUri = uri // <- lưu lại URI
        }
    }
    private fun uriToFile(uri: Uri): File {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("image", ".jpg", requireContext().cacheDir)
        inputStream?.use { input ->
            tempFile.outputStream().use { output -> input.copyTo(output) }
        }
        return tempFile
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddDoctorBinding.inflate(inflater, container, false)
        val departmentId = arguments?.getInt("departmentId") ?: 0
        imageView = binding.imgDoctor
        val vmFactory = doctorViewmodelFactory(DoctorRepository())
        viewModel = ViewModelProvider(this, vmFactory).get(doctorViewModel::class.java)


        viewModel.getpostItemDepartment()
        viewModel.allDepartment.observe(viewLifecycleOwner) { response ->
            val doctorList = response.body()
            val adapterItems = doctorList?.map { "${it.id} - ${it.name}" } ?: emptyList()
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, adapterItems)
            binding.spinnerDepartment.adapter = adapter
        }





        val edtUsername = binding.edtUsername
        val edtPassword = binding.edtPassword
        val edtFullName = binding.edtFullName
        val edtEmail = binding.edtEmail
        val edtPhone = binding.edtPhone
        val edtSpecialization = binding.edtSpecialization
        val edtRole = binding.edtRole

        val btnAdd = binding.btnAddDoctor

        imageView.setOnClickListener {
            pickImage.launch("image/*")
        }

        btnAdd.setOnClickListener {
            val selectedDoctor = binding.spinnerDepartment.selectedItem?.toString()
            val edtDepartmentId = selectedDoctor?.split(" - ")?.get(0)?.toIntOrNull() ?: 0

            val doctorRequest = doctorRequest(
                username = edtUsername.text.toString(),
                password = edtPassword.text.toString(),
                fullName = edtFullName.text.toString(),
                email = edtEmail.text.toString(),
                phoneNumber = edtPhone.text.toString(),
                specialization = edtSpecialization.text.toString(),
                role = edtRole.text.toString(),
                department_id = edtDepartmentId,
                image = imageFileName
            )
            selectedImageUri?.let { uri ->
                val imageFile = uriToFile(uri)
                val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageFile.asRequestBody())
                viewModel.addDocTor(doctorRequest, imagePart)

                viewModel.addSuccess.observe(viewLifecycleOwner) { success ->
                    if (success) {
                        val bundle = Bundle().apply {
                            putInt("departmentid", edtDepartmentId )
                        }
                        findNavController().navigate(R.id.action_addDoctorFragment_to_doctorFragment, bundle)
                        Toast.makeText(context, "Đang gửi yêu cầu với ảnh...", Toast.LENGTH_SHORT).show()
                        Toast.makeText(context, "Thêm thành công ", Toast.LENGTH_SHORT).show()
                    }
                }


            } ?: run {
                Toast.makeText(context, "Bạn chưa chọn ảnh!", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }


    private fun getFileNameFromUri(uri: Uri): String {
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        } ?: "default.jpg"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
