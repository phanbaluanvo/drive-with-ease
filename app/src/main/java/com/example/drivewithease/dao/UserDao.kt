package com.example.drivewithease.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.drivewithease.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM users")
    fun getAll(): List<User>;

    @Query("SELECT u.name FROM users u WHERE u.id = :userId")
    fun getCustomerNameById(userId: Int): String

    @Query("SELECT * FROM users u WHERE u.username = :username OR u.email = :email")
    fun findUsernameAndEmailRegistration(username: String, email: String): List<User>

    @Query("SELECT * FROM users u WHERE u.username = :username")
    fun findUserByUsername(username: String) : User?

    @Query("DELETE FROM users")
    fun deleteAllUsers()

    @Query("SELECT * FROM users u WHERE u.id = :id")
    fun getUserProfile(id: Int): User

    @Update
    fun updateUser(user: User)
}