package com.example.drivewithease.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "booking_history")
data class BookingHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val carId: Int,
    val startDate: Long,
    val endDate: Long,
    val totalPrice: Double,
    val instructions: String?,
    var status: String
)