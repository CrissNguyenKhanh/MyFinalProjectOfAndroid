package com.example.retrofitwrooom.Activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.database.ArticleDatabase
import com.example.retrofitwrooom.databinding.ActivityNewsBinding
import com.example.retrofitwrooom.repository.NewsRepository
import com.example.retrofitwrooom.viewModel.NewsViewModel
import com.example.retrofitwrooom.viewModel.NewsViewmodelProvider

class NewsActivity : AppCompatActivity() {

    lateinit var newsViewModel: NewsViewModel
    lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannel()
        setContentView(R.layout.activity_news)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewmodelProvider(application, newsRepository)
        newsViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Kiểm tra trạng thái đăng nhập và ẩn BottomNavigationView nếu chưa đăng nhập
        val isLoggedIn = checkLoginStatus() // Hàm kiểm tra trạng thái đăng nhập
        if (!isLoggedIn) {
            binding.bottomNavigationView.visibility = View.GONE
        }

        // Lắng nghe khi login thành công và hiển thị lại BottomNavigationView
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.login2Fragment || destination.id == R.id.mainFragment || destination.id == R.id.benhNhanFragment || destination.id == R.id.addBenhNhanFragment
                || destination.id == R.id.homeFragment || destination.id == R.id.addNoteFragment || destination.id == R.id.editNoteFragment || destination.id == R.id.benhnhanUpdateFragment
                || destination.id == R.id.doctorFragment || destination.id == R.id.addDoctorFragment || destination.id == R.id.mainDoctorFragment || destination.id == R.id.lyLichDoctorFragment
                || destination.id == R.id.mainDoctorNaoFragment || destination.id == R.id.detailsFragment || destination.id == R.id.callFragment || destination.id == R.id.benhNhanStaticsFragment
                || destination.id == R.id.startFragment || destination.id == R.id.shareFragment || destination.id == R.id.doctorPhoiFragment || destination.id == R.id.benhNhanTimFragment
                || destination.id == R.id.hosobenhanFragment || destination.id == R.id.loginbenhnhanFragment || destination.id == R.id.fullthongtinvedoctorFragment
                || destination.id == R.id.notificationFragment || destination.id == R.id.appointmentDetailsFragment2
                || destination.id == R.id.addApointFragment || destination.id == R.id.chatFragment || destination.id == R.id.doctorStaticFragment2


            ) {
                binding.bottomNavigationView.visibility = View.GONE
            } else {
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

        binding.bottomNavigationView.setupWithNavController(navController)
    }

    // Hàm kiểm tra trạng thái đăng nhập của người dùng
    private fun checkLoginStatus(): Boolean {
        // Kiểm tra trạng thái đăng nhập của người dùng (ví dụ: từ SharedPreferences, ViewModel...)
        // Trả về true nếu đã đăng nhập, false nếu chưa đăng nhập
        return false // Đây chỉ là ví dụ, bạn cần thay thế với logic thực tế
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "appointment_channel",
                "Appointment Reminder",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Nhắc nhở lịch hẹn khám"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}
