package com.example.drivewithease.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "booking_history")
data class BookingHistory(
    @PrimaryKey val id: Int,
    val userId: Int,
    val carId: Int,
    val startDate: Long,
    val endDate: Long,
    val totalPrice: Double,
    val instructions: String
)