package com.example.drivewithease.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.drivewithease.entity.BookingHistory

@Dao
interface BookingHistoryDao {
    @Query("SELECT * FROM booking_history bh")
    fun getAll() : List<BookingHistory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    fun insertBookingHistory(bookingHistory: BookingHistory)

    @Delete
    fun deleteBookingHistory(bookingHistory: BookingHistory)

    @Query("SELECT * FROM booking_history bh WHERE bh.id = :bookingId")
    fun getBookingByID(bookingId: Int): BookingHistory

    @Update
    fun updateBooking(bookingHistory: BookingHistory)

    @Query("SELECT * FROM booking_history bh WHERE bh.userId = :userId AND bh.endDate < :currentDate")
    fun getPastBooking(userId: Int, currentDate: Long): List<BookingHistory>

    @Query("SELECT * FROM booking_history bh WHERE bh.userId = :userId AND bh.endDate >= :currentDate")
    fun getCurrentBooking(userId: Int, currentDate: Long): List<BookingHistory>

}