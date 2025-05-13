package com.example.retrofitwrooom.ui.fragments.notification

import android.app.*
import android.content.*
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.adapter.notificationAdapter
import com.example.retrofitwrooom.database.NotificationDatabase
import com.example.retrofitwrooom.databinding.FragmentNotificationBinding
import com.example.retrofitwrooom.model.Notification
import com.example.retrofitwrooom.notification.NotificationReceiver
import com.example.retrofitwrooom.repository.appointmentRepository
import com.example.retrofitwrooom.viewModel.appointmentViewmodel
import com.example.retrofitwrooom.viewModel.viewModelFactory.appointmentViewmodelFacotry
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import android.content.pm.PackageManager
import android.Manifest
class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: appointmentViewmodel
    private lateinit var notificationAdapter: notificationAdapter
    private val notificationDao by lazy {
        NotificationDatabase.getDatabase(requireContext()).notificationDao()
    }

    private var selectedTime: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)

        val factory = appointmentViewmodelFacotry(appointmentRepository())
        viewModel = ViewModelProvider(this, factory)[appointmentViewmodel::class.java]
        viewModel.getPostItemBydoctorId(1)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPermissions()

        setupRecyclerView()
        observeAppointmentData()

        binding.btnSetTime.setOnClickListener {
            showDatePickerDialog()
        }

        binding.them.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val detail = binding.etDetail.text.toString().trim()

            if (name.isEmpty() || detail.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val notification = Notification(
                title = name,
                detail = detail,
                image = R.drawable.bell_icon,
                time = selectedTime
            )

            lifecycleScope.launch {
                notificationDao.insertNotification(notification)
                Toast.makeText(requireContext(), "Đã thêm thông báo", Toast.LENGTH_SHORT).show()
                loadNotifications()
                if (selectedTime.isNotEmpty()) setNotificationAlarm(notification)
            }
        }

        loadNotifications()
    }

    private fun setupRecyclerView() {
        notificationAdapter = notificationAdapter(
            requireContext(),
            mutableListOf(),
            { notification -> deleteNotification(notification) },
            { notification -> editNotification(notification) }
        )
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notificationAdapter
        }
    }

    private fun observeAppointmentData() {
        viewModel.getPostItemBydoctorId(1)
        viewModel.responseDataDoctorid.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                val appointmentList = response.body().orEmpty()

                val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val sdfOutput = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

                appointmentList.forEach { appointment ->
                    val dateString = appointment.appointment_date
                    try {
                        val date = sdfInput.parse(dateString)
                        if (date != null) {
                            val formattedTime = sdfOutput.format(date)

                            val notification = Notification(
                                title = "Lịch hẹn với ${appointment.benhNhan.fullName}",
                                detail = "Lý do: ${appointment.reason}",
                                image = R.drawable.bell_icon,
                                time = formattedTime
                            )

                            lifecycleScope.launch {
                                notificationDao.insertNotification(notification)
                                setNotificationAlarm(notification)
                            }
                        }
                    } catch (e: Exception) {
                        Log.w("NotificationFragment", "Lỗi parse ngày lịch hẹn: ${e.message}")
                    }
                }

                loadNotifications()
            } else {
                Toast.makeText(requireContext(), "Không tải được lịch hẹn", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            TimePickerDialog(requireContext(), { _, hour, minute ->
                selectedTime = "$year-${month + 1}-$dayOfMonth $hour:$minute:00"
                Toast.makeText(requireContext(), selectedTime, Toast.LENGTH_SHORT).show()
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun setNotificationAlarm(notification: Notification) {
        val calendar = Calendar.getInstance()
        try {
            val parts = notification.time.split(" ")
            val date = parts[0].split("-").map { it.toInt() }
            val time = parts[1].split(":").map { it.toInt() }

            calendar.set(date[0], date[1] - 1, date[2], time[0], time[1], 0)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Lỗi định dạng thời gian: ${e.message}", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(requireContext(), NotificationReceiver::class.java).apply {
            putExtra("title", notification.title)
            putExtra("detail", notification.detail)
        }

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            return
        }

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } catch (e: SecurityException) {
            Toast.makeText(requireContext(), "Không có quyền báo thức chính xác", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadNotifications() {
        lifecycleScope.launch {
            val list = notificationDao.getAllNotifications()
            notificationAdapter.updateNotifications(list)
        }
    }

    private fun deleteNotification(notification: Notification) {
        lifecycleScope.launch {
            notificationDao.deleteNotification(notification.id)
            loadNotifications()
            Toast.makeText(requireContext(), "Đã xóa thông báo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editNotification(notification: Notification) {
        val view = layoutInflater.inflate(R.layout.dialog_edit_notification, null)
        val editTitle = view.findViewById<EditText>(R.id.editTitle)
        val editDetail = view.findViewById<EditText>(R.id.editDetail)

        editTitle.setText(notification.title)
        editDetail.setText(notification.detail)

        AlertDialog.Builder(requireContext())
            .setTitle("Chỉnh sửa Thông báo")
            .setView(view)
            .setPositiveButton("Lưu") { _, _ ->
                val newTitle = editTitle.text.toString()
                val newDetail = editDetail.text.toString()

                if (newTitle.isNotEmpty() && newDetail.isNotEmpty()) {
                    notification.title = newTitle
                    notification.detail = newDetail

                    lifecycleScope.launch {
                        notificationDao.updateNotification(notification)
                        loadNotifications()
                        Toast.makeText(requireContext(), "Đã cập nhật thông báo", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SCHEDULE_EXACT_ALARM)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.SCHEDULE_EXACT_ALARM), 102)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Đã cấp quyền thông báo", Toast.LENGTH_SHORT).show()
        }
        if (requestCode == 102 && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Đã cấp quyền báo thức chính xác", Toast.LENGTH_SHORT).show()
        }
    }
}
