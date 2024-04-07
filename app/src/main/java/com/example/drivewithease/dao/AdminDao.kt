package com.example.drivewithease.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.drivewithease.entity.Admin

@Dao
interface AdminDao {
    @Query("SELECT * FROM admins")
    fun getAll() : List<Admin>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAdmin(admin: Admin)

    @Delete
    fun deleteAdmin(admin: Admin)

    @Query("SELECT * FROM admins a WHERE a.username = :username")
    fun getAdminByUsername(username: String): Admin?
}