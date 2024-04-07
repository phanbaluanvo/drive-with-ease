package com.example.drivewithease

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivewithease.adapter.BookingHistoryAdapter
import java.security.MessageDigest

class UserProfileActivity : AppCompatActivity() {

    private lateinit var headingListCars: TextView
    private lateinit var customerNameHeading: TextView
    private lateinit var customerEmailHeading: TextView
    private lateinit var customerUsernameHeading: TextView
    private lateinit var customerPasswordHeading: TextView
    private lateinit var headingListBooking: TextView
    private lateinit var headingPastBooking: TextView

    // EditTexts
    private lateinit var customerNameInfo: EditText
    private lateinit var customerEmail: EditText
    private lateinit var customerUsername: EditText
    private lateinit var customerPassword: EditText

    // Buttons
    private lateinit var btnSave: Button

    private val db = AppDatabase.getDatabase(this)


    // RecyclerViews
    private lateinit var rvCurrentBooking: RecyclerView
    private lateinit var rvPastBooking: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_profile)

        headingListCars = findViewById(R.id.headingListCars)
        customerNameHeading = findViewById(R.id.customerNameHeading)
        customerEmailHeading = findViewById(R.id.customerEmailHeading)
        customerUsernameHeading = findViewById(R.id.customerUsernameHeading)
        customerPasswordHeading = findViewById(R.id.customerPasswordHeading)
        headingListBooking = findViewById(R.id.headingListBooking)
        headingPastBooking = findViewById(R.id.headingPastBooking)
        customerNameInfo = findViewById(R.id.customerNameInfo)
        customerEmail = findViewById(R.id.customerEmail)
        customerUsername = findViewById(R.id.customerUsername)
        customerPassword = findViewById(R.id.customerPassword)
        btnSave = findViewById(R.id.buttonSave)
        rvCurrentBooking = findViewById(R.id.rvCurrentBooking)
        rvPastBooking = findViewById(R.id.rvPastBooking)

        var userId = getSharedPreferences("USER_PREFS", MODE_PRIVATE).getInt("USER_ID", -1)

        val user = db.userDao().getUserProfile(userId)

        var userPassword = getSharedPreferences("USER_PREFS", MODE_PRIVATE).getString("PASSWORD", "")

        customerNameInfo.setText(user.name)
        customerEmail.setText(user.email)
        customerUsername.setText(user.username)
        customerPassword.setText(userPassword)

        btnSave.setOnClickListener {
            val updateName = customerNameInfo.text.toString()
            val updateEmail = customerEmail.text.toString()
            val updateUsername = customerUsername.text.toString()
            val updatePassword = hashPassword(customerPassword.text.toString())

            user.email = updateEmail
            user.password = updatePassword
            user.name = updateName
            user.username = updateUsername

            db.userDao().updateUser(user)

            Toast.makeText(this, "Update Information Successfully!", Toast.LENGTH_SHORT).show()
        }

        val currentBookingList = db.bookingHistoryDao().getCurrentBooking(userId, System.currentTimeMillis())

        val currentBookingAdapter = BookingHistoryAdapter(this, currentBookingList)

        rvCurrentBooking.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvCurrentBooking.adapter = currentBookingAdapter;



        val pastBookingList = db.bookingHistoryDao().getPastBooking(userId, System.currentTimeMillis())

        val pastBookingAdapter = BookingHistoryAdapter(this, pastBookingList)
        rvPastBooking.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvPastBooking.adapter = pastBookingAdapter


    }

    private fun hashPassword(password: String): String {
        val data = password.toByteArray()
        val salt = "randomSalt".toByteArray()
        val sha256 = MessageDigest.getInstance("SHA-256")
        sha256.update(salt)
        val hashValue = sha256.digest(data);
        return hashValue.joinToString("") { "%02x".format(it) }
    }
}