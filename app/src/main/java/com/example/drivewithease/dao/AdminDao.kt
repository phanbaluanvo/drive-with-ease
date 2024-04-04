package com.example.drivewithease.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.drivewithease.entity.Admin

@Dao
interface AdminDao {
    @Query("SELECT * FROM admins")
    fun getAll() : List<Admin>

    @Insert
    fun insertAdmin(admin: Admin)

    @Delete
    fun deleteAdmin(admin: Admin)
}