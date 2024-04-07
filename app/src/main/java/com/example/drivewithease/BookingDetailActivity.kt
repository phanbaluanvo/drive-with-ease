package com.example.drivewithease

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

class BookingDetailActivity : AppCompatActivity() {

    private lateinit var pickUpDateEditText: TextView
    private lateinit var dropOffDateEditText: TextView
    private lateinit var carModelTextView: TextView
    private lateinit var carMakeTextView: TextView
    private lateinit var licensePlateTextView: TextView
    private lateinit var carYearTextView: TextView
    private lateinit var carPriceTextView: TextView
    private lateinit var carImg: ImageView
    private lateinit var totalAmount: TextView
    private lateinit var instructionsEditText: TextView
    private lateinit var btnCancel: Button
    private lateinit var btnConfirm: Button
    private lateinit var customerNameTextView: TextView
    private lateinit var customerEmailTextView: TextView
    private lateinit var bookingStatusTextView: TextView

    private val db = AppDatabase.getDatabase(this)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_booking_detail)

        pickUpDateEditText = findViewById(R.id.pickUpDate)
        dropOffDateEditText = findViewById(R.id.dropOffDate)
        carModelTextView = findViewById(R.id.textCarModel)
        carMakeTextView = findViewById(R.id.textViewCarMake)
        carYearTextView = findViewById(R.id.textCarYear)
        carPriceTextView = findViewById(R.id.textCarPrice)
        licensePlateTextView = findViewById(R.id.textCarLicensePlate)
        carImg = findViewById(R.id.carImg)
        instructionsEditText = findViewById(R.id.editTextInstructions)
        btnCancel = findViewById(R.id.buttonCancel)
        btnConfirm = findViewById(R.id.buttonConfirm)
        customerNameTextView = findViewById(R.id.customerName)
        customerEmailTextView = findViewById(R.id.customerEmail)
        bookingStatusTextView = findViewById(R.id.bookingStatus)

        totalAmount = findViewById(R.id.totalAmount)

        val bookingId = intent.getIntExtra("BOOKING_ID", -1)

        val booking = db.bookingHistoryDao().getBookingByID(bookingId)

        val userDetail = db.userDao().getUserProfile(booking.userId)

        val carDetail = db.carDao().getCarById(booking.carId)

        if(carDetail != null) {
            carModelTextView.text = carDetail.model
            carMakeTextView.text = carDetail.make
            licensePlateTextView.text = carDetail.licensePlate
            carYearTextView.text = carDetail.year
            carPriceTextView.text = carDetail.pricePerDay.toString()
            Glide.with(this)
                .load(getImageResourceId(carDetail.imageName))
                .placeholder(R.drawable.default_car_image)
                .error(R.drawable.default_car_image)
                .into(carImg)
        }

        customerNameTextView.text = userDetail.name
        customerEmailTextView.text = userDetail.email

        val startDate = Instant.ofEpochMilli(booking.startDate)
        val endDate = Instant.ofEpochMilli(booking.endDate)

        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.CANADA)
        val formattedStartDate = formatter.format(Date.from(startDate))
        val formattedEndDate = formatter.format(Date.from(endDate))

        pickUpDateEditText.text = formattedStartDate
        dropOffDateEditText.text = formattedEndDate

        totalAmount.text = booking.totalPrice.toString()

        instructionsEditText.text = booking.instructions

        bookingStatusTextView.text = booking.status

        if(booking.status.equals("WAITING")) {
            bookingStatusTextView.setTextColor(this.getColor(R.color.waiting))
        } else if(booking.status.equals("CANCEL")) {
            bookingStatusTextView.setTextColor(this.getColor(R.color.cancel))
        } else if(booking.status.equals("CONFIRMED")) {
            bookingStatusTextView.setTextColor(this.getColor(R.color.confirmed))
        }

        btnCancel.setOnClickListener {
            booking.status = "CANCEL"

            db.bookingHistoryDao().updateBooking(booking)

            Toast.makeText(this, "Booking is canceled.", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, AdminManagementActivity::class.java))
        }

        btnConfirm.setOnClickListener {
            booking.status = "CONFIRMED"

            db.bookingHistoryDao().updateBooking(booking)


            Toast.makeText(this, "Booking is confirmed!.", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, AdminManagementActivity::class.java))
        }

    }

    fun getImageResourceId(imageName: String): Int {
        return this.resources.getIdentifier(imageName, "drawable", this.packageName)
    }
}