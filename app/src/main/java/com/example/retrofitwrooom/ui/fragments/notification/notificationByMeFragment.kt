package com.example.retrofitwrooom.ui.fragments.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.adapter.appointmentAdapter
import com.example.retrofitwrooom.adapter.appointmentAdapter_Accept
import com.example.retrofitwrooom.adapter.doctormainAdapter
import com.example.retrofitwrooom.databinding.FragmentMainBinding
import com.example.retrofitwrooom.databinding.FragmentNotificationByMeBinding
import com.example.retrofitwrooom.model.appointment
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.repository.appointmentRepository
import com.example.retrofitwrooom.viewModel.appointmentViewmodel
import com.example.retrofitwrooom.viewModel.viewModelFactory.appointmentViewmodelFacotry
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class notificationByMeFragment : Fragment() {
    private lateinit var viewmodel: appointmentViewmodel
    private var _binding: FragmentNotificationByMeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: appointmentAdapter_Accept

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vmFactory = appointmentViewmodelFacotry(appointmentRepository())
        viewmodel = ViewModelProvider(this, vmFactory)[appointmentViewmodel::class.java]

        _binding = FragmentNotificationByMeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val doctor = arguments?.getParcelable<doctor>("doctor")
        setupRecyclerView()
        setupRecyclerView()

        val filter = doctor?.id
        if (filter != null) {
            viewmodel.getPostItemBydoctorId_Accept(filter)
        }
        viewmodel.responseDataDoctorid_Accept.observe(viewLifecycleOwner) { response ->
            val appointments = response.body() ?: emptyList()
            adapter.updateData(appointments)
        }

        doctor?.let { doc ->
            viewmodel.getCountDoctorById(doc.id)

            // Lắng nghe kết quả count từ ViewModel
            viewmodel.responseDataCountDoctorByid.observe(
                viewLifecycleOwner
            ) { response ->
                if (response.isSuccessful) {
                    val count = response.body() ?: 0
                    binding.badgeText.text = "$count"
                } else {
                    binding.badgeText.text = "0"
                }
            }
        }

        binding.btnNotification.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("doctor", doctor)
            }
            findNavController().navigate(
                R.id.action_notificationByMeFragment_to_mailFragment,
                bundle
            )
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupRecyclerView() {
        adapter = appointmentAdapter_Accept(
            emptyList(),
            onEditClick = { appointment ->

                val dialogView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_edit_appointment, null)

                val imgAvatar = dialogView.findViewById<ImageView>(R.id.imgAvatar)
                val tvName = dialogView.findViewById<TextView>(R.id.tvName)
                val tvDob = dialogView.findViewById<TextView>(R.id.tvDob)
                val tvAppointmentDate = dialogView.findViewById<TextView>(R.id.tvAppointmentDate)
                val tvReason = dialogView.findViewById<TextView>(R.id.tvReason)

                // Gán dữ liệu từ appointment
                Glide.with(requireContext())
                    .load(appointment.benhNhan.image ?: R.drawable.ic_person)
                    .into(imgAvatar)// Có thể dùng Glide/Picasso nếu có URL ảnh
                tvName.text = appointment.benhNhan.fullName
                tvDob.text = "Ngày sinh: ${formatDob(appointment.benhNhan.dob.toString())}"
                tvAppointmentDate.text =
                    "Ngày hẹn khám: ${formatAppointmentDate(appointment.appointment_date)}"

                tvReason.text = "Lý do: ${appointment.reason}"

                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setView(dialogView)
                    .setPositiveButton("OK") { d, _ -> d.dismiss() }
                    .setNegativeButton("Hủy") { d, _ -> d.dismiss() }
                    .create()

                dialog.show()
            },

            onDeleteClick = { appointment ->
                DeleteItem(appointment)
            }
        )

        binding.recyclerAppointments.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerAppointments.adapter = adapter
    }

    private fun formatDob(dobString: String): String {
        return try {
            // "Sun May 04 00:00:00 GMT 2025"
            val parser = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
            val date = parser.parse(dobString)
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            formatter.format(date!!)
        } catch (e: Exception) {
            dobString
        }
    }

    private fun formatAppointmentDate(dateString: String): String {
        return try {
            // "2025-05-16T07:54:02"
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = parser.parse(dateString)
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            formatter.format(date!!)
        } catch (e: Exception) {
            dateString
        }
    }


    private fun DeleteItem(appointment: appointment) {
        CoroutineScope(Dispatchers.Main).launch {
            // Thêm vào appointmentaccept
            // Xoá appointment cũ
            viewmodel.deleteAppointment_Accept(appointment.id, appointment.doctor.id)

            // Hiển thị thông báo + điều hướng
            Toast.makeText(context, "Đã xóa cuộc hẹn", Toast.LENGTH_SHORT).show()

        }
    }

}