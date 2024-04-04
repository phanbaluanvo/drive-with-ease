package com.example.drivewithease.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
open class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val username: String,
    val email: String,
    val password: String
)