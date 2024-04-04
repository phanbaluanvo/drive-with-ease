package com.example.drivewithease

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.drivewithease.dao.AdminDao
import com.example.drivewithease.dao.BookingHistoryDao
import com.example.drivewithease.dao.CarDao
import com.example.drivewithease.dao.UserDao
import com.example.drivewithease.entity.Admin
import com.example.drivewithease.entity.BookingHistory
import com.example.drivewithease.entity.Car
import com.example.drivewithease.entity.User

@Database(entities = [User::class, Car::class, Admin::class, BookingHistory::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao() : UserDao;
    abstract fun adminDao() : AdminDao;
    abstract fun carDao() : CarDao;
    abstract fun bookingHistoryDao() : BookingHistoryDao;

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "drive_with_ease_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}