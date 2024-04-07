package com.example.drivewithease.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.drivewithease.entity.Car

@Dao
interface CarDao {
    @Query("SELECT * FROM cars")
    fun getAllCar(): List<Car>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    fun insertCar(car: Car);

    @Delete
    fun deleteCar(car: Car)

    @Update
    fun updateCar(car: Car)

    @Query("SELECT * FROM cars WHERE carId = :carId")
    fun getCarById(carId: Int): Car?
}