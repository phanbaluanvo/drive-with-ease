package com.example.drivewithease.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class Car(
    @PrimaryKey
    @ColumnInfo(name = "carId")
    val carId: Int,
    @ColumnInfo(name = "model")
    val model: String,
    @ColumnInfo(name="make")
    val make: String,
    @ColumnInfo(name="year")
    val year: String,
    @ColumnInfo(name="licensePlate")
    val licensePlate: String,
    @ColumnInfo(name="pricePerDay")
    val pricePerDay: Double,
    @ColumnInfo(name="availabilityStatus")
    val availabilityStatus: Boolean,
    @ColumnInfo(name="imageName")
    val imageName: String
)