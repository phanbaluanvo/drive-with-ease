package com.example.drivewithease.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.drivewithease.entity.BookingHistory

@Dao
interface BookingHistoryDao {
    @Query("SELECT * FROM booking_history bh " +
            "JOIN users u ON bh.userId = u.id")
    fun getAll() : List<BookingHistory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookingHistory(bookingHistory: BookingHistory)

    @Delete
    fun deleteBookingHistory(bookingHistory: BookingHistory)
}