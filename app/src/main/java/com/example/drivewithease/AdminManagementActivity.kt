package com.example.drivewithease

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivewithease.adapter.BookingHistoryAdapter
import com.example.drivewithease.adapter.CarAdapter
import com.example.drivewithease.entity.Admin
import com.example.drivewithease.entity.BookingHistory
import com.example.drivewithease.entity.Car
import com.example.drivewithease.entity.User
import java.time.Instant
import java.time.temporal.ChronoUnit


class AdminManagementActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin)

        val recyclerViewCar: RecyclerView = findViewById(R.id.recyclerViewCars);
        val recyclerViewBookings: RecyclerView = findViewById(R.id.recyclerViewBookingHistory);

        val db = AppDatabase.getDatabase(this);

        //Initialize some data
        val admin = Admin(name= "Admin", username = "admin", password = "admin", email = "admin@gmail.com")


        val car1 = Car(1, "BMW X4 Sports","BMW", "2019","NR320F", 70.00,true, "bmw_car_img")
        val car2 = Car(2, "Ford Mustang","Ford", "2023","SC937A", 60.00,true, "ford_mustang")
        val car3 = Car(3, "Honda Accord", "Honda", "2020", "AC392E", 30.00, false, "honda_accord")


        db.carDao().insertCar(car1)
        db.carDao().insertCar(car2)
        db.carDao().insertCar(car3)

        db.adminDao().insertAdmin(admin)


        val carList = db.carDao().getAllCar();
        val bookingList = db.bookingHistoryDao().getAll();

        recyclerViewCar.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        val carAdapter = CarAdapter(this, carList, true)
        recyclerViewCar.adapter = carAdapter;

        val bookingAdapter = BookingHistoryAdapter(this, bookingList)
        recyclerViewBookings.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewBookings.adapter = bookingAdapter;


    }
}