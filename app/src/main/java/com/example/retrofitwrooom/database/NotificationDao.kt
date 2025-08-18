package com.example.retrofitwrooom.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.retrofitwrooom.model.Notification

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotifications(notifications: List<Notification>)

    @Query("SELECT * FROM notification_table")
    suspend fun getAllNotifications(): List<Notification>

    @Query("DELETE FROM notification_table WHERE id = :id")
    suspend fun deleteNotification(id: Int)

    @Update
    suspend fun updateNotification(notification: Notification)  // Update function

    @Query("SELECT * FROM notification_table WHERE title = :title AND time = :time LIMIT 1")
    suspend fun getNotificationByTitleAndTime(title: String, time: String): Notification?
}