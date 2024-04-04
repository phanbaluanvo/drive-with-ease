package com.example.drivewithease.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admins")
class Admin(
    id: Int,
    name: String,
    username: String,
    email: String,
    password: String
) : User(id, name, username, email, password)