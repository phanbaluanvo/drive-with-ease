package com.example.drivewithease

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.drivewithease.entity.BookingHistory
import com.example.drivewithease.entity.Car
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class CarBookingActivity : AppCompatActivity() {

    private lateinit var pickUpDateEditText: EditText
    private lateinit var dropOffDateEditText: EditText
    private lateinit var carModelTextView: TextView
    private lateinit var carMakeTextView: TextView
    private lateinit var licensePlateTextView: TextView
    private lateinit var carYearTextView: TextView
    private lateinit var carPriceTextView: TextView
    private lateinit var carImg: ImageView
    private lateinit var rentalTotal: TextView
    private lateinit var gstTotal: TextView
    private lateinit var pstTotal: TextView
    private lateinit var pvrtTotal: TextView
    private lateinit var pvrtTax: TextView
    private lateinit var totalAmount: TextView
    private lateinit var instructionsEditText: EditText
    private lateinit var btnBook: Button

    private var totalPrice: Double = 0.0

    private val db = AppDatabase.getDatabase(this)

    private val myCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_car_booking)

        pickUpDateEditText = findViewById(R.id.editTextPickupDate)
        dropOffDateEditText = findViewById(R.id.editTextDropoffDate)
        carModelTextView = findViewById(R.id.textCarModel)
        carMakeTextView = findViewById(R.id.textViewCarMake)
        carYearTextView = findViewById(R.id.textCarYear)
        carPriceTextView = findViewById(R.id.textCarPrice)
        licensePlateTextView = findViewById(R.id.textCarLicensePlate)
        carImg = findViewById(R.id.carImg)
        instructionsEditText = findViewById(R.id.editTextInstructions)
        btnBook = findViewById(R.id.buttonBook)

        rentalTotal = findViewById(R.id.textRentalTotal)
        gstTotal = findViewById(R.id.gstTotal)
        pstTotal = findViewById(R.id.pstTotal)
        pvrtTotal = findViewById(R.id.pvrtTotal)
        pvrtTax = findViewById(R.id.pvrtTaxTotal)
        totalAmount = findViewById(R.id.totalAmount)

        val carId = intent.getIntExtra("CAR_ID", -1)

        val userId = this.getSharedPreferences("USER_PREFS", MODE_PRIVATE).getInt("USER_ID", -1)

        val db = AppDatabase.getDatabase(this)
        val car = db.carDao().getCarById(carId)

        if (car != null) {
            showCarDetail(car)
        }

        val pickUpDateSet = DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, month: Int, day: Int ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            updateLabel(pickUpDateEditText)
            updateTotalCost()
        }

        val dropOffDateSet = DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, month: Int, day: Int ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            updateLabel(dropOffDateEditText)
            updateTotalCost()
        }

        pickUpDateEditText.setOnClickListener {
            showDatePickerDialog(pickUpDateSet)
        }

        dropOffDateEditText.setOnClickListener {
            showDatePickerDialog(dropOffDateSet)
        }

        btnBook.setOnClickListener {
            bookCar(userId, carId, pickUpDateEditText, dropOffDateEditText, instructionsEditText, totalPrice)

            Toast.makeText(this, "Your Reservation is booked. Waiting for confirmation", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, CarCatalogueActivity::class.java))
        }
    }

    private fun updateLabel(datePick: EditText) {
        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        datePick.setText(dateFormat.format(myCalendar.time))
    }

    private fun showDatePickerDialog(dateSetListener: DatePickerDialog.OnDateSetListener) {
        DatePickerDialog(
            this,
            dateSetListener,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateTotalCost() {
        val pickUpDate = pickUpDateEditText.text.toString()
        val dropOffDate = dropOffDateEditText.text.toString()

        // Check if both pick-up and drop-off dates are set
        if (pickUpDate.isNotEmpty() && dropOffDate.isNotEmpty()) {
            val startDate = Calendar.getInstance()
            val endDate = Calendar.getInstance()

            val sdf = SimpleDateFormat("MM/dd/yy", Locale.CANADA)
            startDate.time = sdf.parse(pickUpDate)
            endDate.time = sdf.parse(dropOffDate)

            val daysBetween = calculateDaysBetween(startDate, endDate)
            val pricePerDay = carPriceTextView.text.toString().toDouble()

            val totalRental = daysBetween * pricePerDay
            rentalTotal.text = String.format("%.2f", totalRental)

            val gst = totalRental * 0.05
            gstTotal.text = String.format("%.2f", gst)

            val pst = totalRental * 0.07
            pstTotal.text = String.format("%.2f", pst)

            var pvrt = 0.0
            if (daysBetween <= 28) {
                pvrt = daysBetween * 1.5
            }
            pvrtTotal.text = String.format("%.2f", pvrt)

            val pvrtTaxx = pvrt * 0.05
            pvrtTax.text = String.format("%.2f", pvrtTaxx)

            val total = totalRental + gst + pst + pvrt + pvrtTaxx
            totalAmount.text = String.format("%.2f", total)

            totalPrice = total
        }
    }


    fun calculateDaysBetween(startDate: Calendar, endDate: Calendar): Long {
        val diffInMillis = endDate.timeInMillis - startDate.timeInMillis
        return TimeUnit.MILLISECONDS.toDays(diffInMillis)
    }

    private fun showCarDetail(car: Car) {
        carModelTextView.text = car.model
        carMakeTextView.text = car.make
        carYearTextView.text = car.year
        carPriceTextView.text = car.pricePerDay.toString()
        licensePlateTextView.text = car.licensePlate

        Glide.with(this)
            .load(getImageResourceId(car.imageName))
            .placeholder(R.drawable.default_car_image)
            .error(R.drawable.default_car_image)
            .into(carImg)
    }

    private fun getImageResourceId(imageName: String): Int {
        return resources.getIdentifier(imageName, "drawable", packageName)
    }

    private fun bookCar(userId: Int, carId: Int, startDateEditText: EditText, endDateEditText: EditText, instructionsEditText: EditText, totalPrice: Double) {
        val startDateString = startDateEditText.text.toString()
        val endDateString = endDateEditText.text.toString()

        var instructions = "";

        if(instructionsEditText.getText().toString().isNotEmpty()) {
            instructions = instructionsEditText.text.toString()
        }

        // Convert start and end dates to milliseconds
        val sdf = SimpleDateFormat("MM/dd/yy", Locale.US)
        val startDateMillis = sdf.parse(startDateString)?.time ?: 0
        val endDateMillis = sdf.parse(endDateString)?.time ?: 0

        // Get instructions from EditText

        // Create BookingHistory object
        val bookingHistory = BookingHistory(
            userId = userId,
            carId = carId,
            startDate = startDateMillis,
            endDate = endDateMillis,
            totalPrice = totalPrice,
            instructions = instructions,
            status = "WAITING"
        )

        // Insert booking history into database
        db.bookingHistoryDao().insertBookingHistory(bookingHistory)
    }

}
