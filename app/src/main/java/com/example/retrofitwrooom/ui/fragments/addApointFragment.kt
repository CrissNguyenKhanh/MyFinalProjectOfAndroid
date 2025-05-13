package com.example.retrofitwrooom.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.FragmentAddApointBinding
import com.example.retrofitwrooom.databinding.FragmentAddBenhNhanBinding
import com.example.retrofitwrooom.databinding.FragmentLyLichDoctorBinding
import com.example.retrofitwrooom.model.benhNhan
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.model.request.appontmentRequest
import com.example.retrofitwrooom.repository.appointmentRepository
import com.example.retrofitwrooom.repository.benhNhanRepository
import com.example.retrofitwrooom.viewModel.appointmentViewmodel
import com.example.retrofitwrooom.viewModel.benhNhanViewModel
import com.example.retrofitwrooom.viewModel.doctorViewModel
import com.example.retrofitwrooom.viewModel.viewModelFactory.appointmentViewmodelFacotry
import com.example.retrofitwrooom.viewModel.viewModelFactory.benhNhanViewmodelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class addApointFragment : Fragment() {
    private lateinit var viewModel: appointmentViewmodel
    private var _binding: FragmentAddApointBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAddApointBinding.inflate(inflater, container, false)
        val vmFactory = appointmentViewmodelFacotry(appointmentRepository())
        viewModel = ViewModelProvider(this, vmFactory)[appointmentViewmodel::class.java]
        val doctor = arguments?.getParcelable<doctor>("doctor")
        val bennhan = arguments?.getParcelable<benhNhan>("benhnhan")
        binding.edtAppointmentDate.setOnClickListener {
            // Chọn ngày sinh
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(), { _, y, m, d ->
                val timePicker = TimePickerDialog(requireContext(), { _, hour, minute ->
                    calendar.set(y, m, d, hour, minute)
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                    binding.edtAppointmentDate.setText(sdf.format(calendar.time))
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                timePicker.show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()

        }

        binding.btnAddAppointment.setOnClickListener {
            val dobStr = binding.edtAppointmentDate.text.toString().trim()
            val reason = binding.edtReason.text.toString().trim()
            val status = binding.edtStatus.text.toString().trim()

            // Chuyển định dạng ngày
            val sdfInput = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            val sdfOutput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)

            val dobFormatted = try {
                val date = sdfInput.parse(dobStr)
                sdfOutput.format(date!!)
            } catch (e: Exception) {
                Toast.makeText(context, "Sai định dạng ngày. Dùng yyyy-MM-dd HH:mm", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val request  = appontmentRequest(
                appointment_date =  dobFormatted,
                reason =  reason,
                status =  status,
                doctor_id = doctor?.id ?: 0L,
                patient_id = bennhan?.id  ?: 0L
            )

            Toast.makeText(context, "đang tạo cuộc hẹn", Toast.LENGTH_LONG).show()

            Toast.makeText(context, "  tạo cuộc hẹn thành cong", Toast.LENGTH_SHORT).show()
            binding.loadingLayout.visibility = View.VISIBLE

            // Giả lập delay 2s và gọi ViewModel
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                viewModel.addCuochen(request)

                binding.loadingLayout.visibility = View.GONE
                Toast.makeText(context, "Tạo cuộc hẹn thành công", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addApointFragment_to_appointmentDetailsFragment2)
            }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }


}