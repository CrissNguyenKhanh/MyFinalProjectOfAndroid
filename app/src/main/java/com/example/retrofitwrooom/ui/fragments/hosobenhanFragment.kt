package com.example.retrofitwrooom.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.FragmentHosobenhanBinding
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.model.request.benhAnRequest
import com.example.retrofitwrooom.repository.benhAnRepository
import com.example.retrofitwrooom.repository.benhNhanRepository
import com.example.retrofitwrooom.viewModel.benhNhanViewModel
import com.example.retrofitwrooom.viewModel.benhanViewModel
import com.example.retrofitwrooom.viewModel.viewModelFactory.benhNhanViewmodelFactory
import com.example.retrofitwrooom.viewModel.viewModelFactory.benhanviewmodelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import android.graphics.*
import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream

class hosobenhanFragment : Fragment() {
    private lateinit var viewModel: benhanViewModel
    private var _binding: FragmentHosobenhanBinding? = null
    private val binding get() = _binding!!

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
    private var descs: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHosobenhanBinding.inflate(inflater, container, false)
        val vmFactory = benhanviewmodelFactory(benhAnRepository())
        viewModel = ViewModelProvider(this, vmFactory).get(benhanViewModel::class.java)


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
            checkKham = it.getBoolean("checkKham", false)
            descs = it.getString("descs")

        }


        binding.fullnamebenhnhan.setText("Họ và tên: " + fullName)
        binding.address.setText("Địa chỉ: " + address)
        binding.phonennumber.setText("Liên lạc: " + phoneNumber)
        binding.email.setText("Email: " + email)
        binding.gioitin.setText("Giới tính: " + gender)
        binding.dob.setText("Ngày sinh: " + SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(dob ?: Date()))
        binding.description.setText("Ghi chú: " + descs)

        Glide.with(requireContext())
            .load(image)
            .placeholder(R.drawable.doctor)
            .error(R.drawable.doctor)
            .into(binding.anhprofilebenhnhan)

// Thông tin bác sĩ
        binding.fullnamedoctor.setText("Bác sĩ: " + (doctor?.fullName ?: ""))
        binding.department.setText("Khoa: " + (doctor?.department?.name ?: ""))
        binding.camketcuabs.setText("Cam kết: Bác sĩ chịu trách nhiệm theo dõi và điều trị cho bệnh nhân.")

// Chữ ký
        binding.anhchukibacsi.setImageResource(R.drawable.signaturebacsi)
        binding.anhchukibenhnhan.setImageResource(R.drawable.signaturebenhnahn)

        binding.button3.setOnClickListener {
            Log.d("thongtinaddbenhan", fullName.toString() + descs.toString() + doctor?.id.toString() + doctor?.department?.id.toString())

            if (fullName != null && descs != null && doctor?.id != null &&
                doctor?.fullName != null && doctor?.department?.id != null) {

                val benhanRequest = benhAnRequest(
                    hoTenBenhNhan = fullName!!,
                    moTaBenh = descs!!,
                    doctor_id = doctor!!.id!!,
                    tenBacSi = doctor!!.fullName!!,
                    benhnhan_id = id,
                    khoa_id = doctor!!.department!!.id!!
                )

                viewModel.addBenhAn(benhanRequest)


// --- Ghi file PDF ---
                val pdfDocument = PdfDocument()
                val paint = Paint()
                val titlePaint = Paint()

                val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
                val page = pdfDocument.startPage(pageInfo)
                val canvas = page.canvas

                titlePaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                titlePaint.textSize = 18f
                titlePaint.color = Color.BLACK
                paint.textSize = 14f

                var y = 50

                canvas.drawText("BỆNH ÁN BỆNH NHÂN", 200f, y.toFloat(), titlePaint)
                y += 40

                canvas.drawText("Họ và tên: $fullName", 40f, y.toFloat(), paint); y += 25
                canvas.drawText("Địa chỉ: $address", 40f, y.toFloat(), paint); y += 25
                canvas.drawText("Liên lạc: $phoneNumber", 40f, y.toFloat(), paint); y += 25
                canvas.drawText("Email: $email", 40f, y.toFloat(), paint); y += 25
                canvas.drawText("Giới tính: $gender", 40f, y.toFloat(), paint); y += 25
                canvas.drawText("Ngày sinh: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(dob ?: Date())}", 40f, y.toFloat(), paint); y += 25
                canvas.drawText("Ghi chú: $descs", 40f, y.toFloat(), paint); y += 40

                canvas.drawText("Bác sĩ: ${doctor!!.fullName}", 40f, y.toFloat(), paint); y += 25
                canvas.drawText("Khoa: ${doctor!!.department!!.name}", 40f, y.toFloat(), paint); y += 25
                canvas.drawText("Cam kết: Bác sĩ chịu trách nhiệm theo dõi và điều trị cho bệnh nhân.", 40f, y.toFloat(), paint)

                pdfDocument.finishPage(page)

                try {
                    val fileName = "benh_an_${id}.pdf"
                    val file = File(requireContext().getExternalFilesDir(null), fileName)
                    pdfDocument.writeTo(FileOutputStream(file))
                    Toast.makeText(requireContext(), "Đã tạo file PDF bệnh án: ${file.absolutePath}", Toast.LENGTH_LONG).show()

                    findNavController().navigate(R.id.action_hosobenhanFragment_to_benhNhanFragment)
                } catch (e: Exception) {
                    Log.e("PDF_ERROR", "Lỗi ghi file PDF: ${e.message}")
                    Toast.makeText(requireContext(), "Lỗi ghi file PDF: ${e.message}", Toast.LENGTH_LONG).show()
                } finally {
                    pdfDocument.close()
                }


            } else {
                Toast.makeText(requireContext(), "Thông tin không đầy đủ để tạo bệnh án", Toast.LENGTH_SHORT).show()
            }
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressLayout = binding.progressBar   // LinearLayout chứa progress + text
        val contentScroll = binding.contentScroll  // ScrollView chứa nội dung hồ sơ

        Handler(Looper.getMainLooper()).postDelayed({
            progressLayout.visibility = View.GONE
            contentScroll.visibility = View.VISIBLE
        }, 2000)






    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
