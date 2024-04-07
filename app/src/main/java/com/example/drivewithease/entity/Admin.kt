package com.example.drivewithease.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admins")
open class Admin(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val username: String,
    val email: String,
    val password: String
)