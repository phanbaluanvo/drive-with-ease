package com.example.drivewithease.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
open class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var username: String,
    var email: String,
    var password: String
)