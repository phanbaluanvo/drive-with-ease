package com.example.drivewithease.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.drivewithease.entity.Car

@Dao
interface CarDao {
    @Query("SELECT * FROM cars")
    fun getAllCar(): List<Car>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCar(car: Car);

    @Delete
    fun deleteCar(car: Car)
}