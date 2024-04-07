package com.example.drivewithease

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.drivewithease.entity.Car

class CarModifyActivity : AppCompatActivity() {

    private lateinit var carModelEditText: EditText
    private lateinit var carMakeEditText: EditText
    private lateinit var licensePlateEditText: EditText
    private lateinit var carYearEditText: EditText
    private lateinit var carPriceEditText: EditText
    private lateinit var availabbilityStatusSwitch: Switch
    private lateinit var carImg: ImageView

    private lateinit var saveBtn: Button
    private lateinit var deleteBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_car_modify)

        carModelEditText = findViewById(R.id.editTextCarModel)
        carMakeEditText = findViewById(R.id.editTextCarMake)
        licensePlateEditText = findViewById(R.id.editTextCarLicensePlate)
        carYearEditText = findViewById(R.id.editTextCarYear)
        carPriceEditText = findViewById(R.id.editTextCarPrice)
        availabbilityStatusSwitch = findViewById(R.id.availabilitySwitch)
        carImg = findViewById(R.id.carImg)

        saveBtn = findViewById(R.id.buttonSave)
        deleteBtn = findViewById(R.id.buttonDelete)

        var carId = intent.getIntExtra("CAR_ID", -1)

        val db = AppDatabase.getDatabase(this)
        val car = db.carDao().getCarById(carId)

        if (car != null) {
            showCarDetail(car)
        }

        saveBtn.setOnClickListener {
            val carModel = carModelEditText.text.toString()
            val carMake = carMakeEditText.text.toString()
            val licensePlate = licensePlateEditText.text.toString()
            val carYear = carYearEditText.text.toString()
            val carPrice = carPriceEditText.text.toString()
            val isAvailable = availabbilityStatusSwitch.isChecked

            car?.let { car ->
                val updatedCar = car.copy(
                    model = carModel,
                    make = carMake,
                    year = carYear,
                    licensePlate = licensePlate,
                    pricePerDay = carPrice.toDouble(),
                    availabilityStatus = isAvailable
                )
                db.carDao().updateCar(updatedCar);
            }


        }

        deleteBtn.setOnClickListener {
            if(car != null)
                db.carDao().deleteCar(car)
            startActivity(Intent(this, AdminManagementActivity::class.java))
            finish()
        }
    }

    fun showCarDetail(car: Car) {
        carModelEditText.setText(car.model)
        carMakeEditText.setText(car.make)
        carYearEditText.setText(car.year)
        carPriceEditText.setText(car.pricePerDay.toString())
        licensePlateEditText.setText(car.licensePlate)
        availabbilityStatusSwitch.isChecked = car.availabilityStatus

        val colorStateList = resources.getColorStateList(R.color.switch_colors)

        availabbilityStatusSwitch.trackTintList = colorStateList

        Glide.with(this)
            .load(getImageResourceId(car.imageName))
            .placeholder(R.drawable.default_car_image)
            .error(R.drawable.default_car_image)
            .into(carImg)
    }

    fun getImageResourceId(imageName: String): Int {
        return this.resources.getIdentifier(imageName, "drawable", this.packageName)
    }
}