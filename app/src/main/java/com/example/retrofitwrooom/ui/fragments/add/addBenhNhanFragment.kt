package com.example.retrofitwrooom.ui.fragments.add

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.retrofitwrooom.databinding.FragmentAddBenhNhanBinding
import com.example.retrofitwrooom.model.request.benhnhanRequest
import com.example.retrofitwrooom.repository.benhNhanRepository
import com.example.retrofitwrooom.viewModel.benhNhanViewModel
import com.example.retrofitwrooom.viewModel.viewModelFactory.benhNhanViewmodelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class addBenhNhanFragment : Fragment() {

    private lateinit var viewModel: benhNhanViewModel
    private var _binding: FragmentAddBenhNhanBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageView: ImageView
    private var selectedImageUri: Uri? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri ->
            imageView.setImageURI(uri)
            selectedImageUri = uri
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("image", ".jpg", requireContext().cacheDir)
        inputStream?.use { input -> tempFile.outputStream().use { output -> input.copyTo(output) } }
        return tempFile
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBenhNhanBinding.inflate(inflater, container, false)
        imageView = binding.imageView
        val vmFactory = benhNhanViewmodelFactory(benhNhanRepository())
        viewModel = ViewModelProvider(this, vmFactory)[benhNhanViewModel::class.java]

        // Quan sát sự kiện thêm thành công
        viewModel.addSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Thêm bệnh nhân thành công", Toast.LENGTH_SHORT).show()
                Log.d("add", "Thêm thành công")
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, "Thêm bệnh nhân thất bại", Toast.LENGTH_SHORT).show()
            }
        }

        // Tải danh sách bác sĩ
        viewModel.getPostItemDoctor()
        viewModel.allDocor.observe(viewLifecycleOwner) { response ->
            val doctorList = response.body()
            val adapterItems = doctorList?.map { "${it.id} - ${it.fullName}" } ?: emptyList()
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, adapterItems)
            binding.spinnerDoctor.adapter = adapter
        }

        // Chọn ngày sinh
        binding.editDob.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(), { _, y, m, d ->
                val selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d)
                binding.editDob.setText(selectedDate)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        // Chọn ảnh
        imageView.setOnClickListener {
            pickImage.launch("image/*")
        }

        // Xử lý khi nhấn "Thêm"
        binding.btnAdd.setOnClickListener {
            val fullName = binding.editFullName.text.toString().trim()
            val address = binding.editAddress.text.toString().trim()
            val phoneNumber = binding.editPhone.text.toString().trim()
            val email = binding.editEmail.text.toString().trim()
            val gender = binding.editGender.text.toString().trim()
            val dobStr = binding.editDob.text.toString().trim()
            val desc = binding.editDescription.text.toString().trim()
            val selectedDoctor = binding.spinnerDoctor.selectedItem?.toString()
            val doctorId = selectedDoctor?.split(" - ")?.get(0)?.toLongOrNull() ?: 0L
            val checkKham = when {
                binding.daKham.isChecked -> true
                binding.chuaKham.isChecked -> false
                else -> false
            }

            // Kiểm tra dữ liệu đầu vào và hiển thị lỗi nếu cần
            var isValid = true

            if (fullName.isEmpty()) {
                binding.editFullName.error = "Họ tên không được để trống"
                isValid = false
            } else {
                binding.editFullName.error = null
            }

            if (dobStr.isEmpty()) {
                binding.editDob.error = "Ngày sinh không được để trống"
                isValid = false
            } else {
                binding.editDob.error = null
            }

            if (address.isEmpty()) {
                binding.editAddress.error = "Địa chỉ không được để trống"
                isValid = false
            } else {
                binding.editAddress.error = null
            }

            if (phoneNumber.isEmpty()) {
                binding.editPhone.error = "Số điện thoại không được để trống"
                isValid = false
            } else {
                binding.editPhone.error = null
            }

            // Kiểm tra ảnh
            if (selectedImageUri == null) {
                Toast.makeText(context, "Bạn chưa chọn ảnh!", Toast.LENGTH_SHORT).show()
                isValid = false
            }

            // Nếu dữ liệu hợp lệ, tiếp tục xử lý
            if (isValid) {
                // Chuyển định dạng ngày
                val dobFormatted = try {
                    val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dobStr)
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(date)
                } catch (e: Exception) {
                    Toast.makeText(context, "Sai định dạng ngày. Dùng yyyy-MM-dd", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                selectedImageUri?.let { uri ->
                    val imageFile = uriToFile(uri)
                    val imagePart = MultipartBody.Part.createFormData(
                        "image", imageFile.name, imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                    )

                    val request = benhnhanRequest(
                        fullName = fullName,
                        dob = dobFormatted,
                        address = address,
                        phoneNumber = phoneNumber,
                        email = email,
                        doctorId = doctorId,
                        gender = gender,
                        image = imageFile.name,
                        description = desc,
                        checkKham = checkKham
                    )

                    viewModel.addBenhNhan(request, imagePart)
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
