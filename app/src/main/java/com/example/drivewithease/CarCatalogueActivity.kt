package com.example.drivewithease

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivewithease.adapter.CarAdapter

class CarCatalogueActivity : AppCompatActivity() {

    private lateinit var textViewGreeting: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_catalogue)
        enableEdgeToEdge()

        textViewGreeting = findViewById(R.id.textViewGreeting)

        var name = getSharedPreferences("USER_PREFS", MODE_PRIVATE).getString("USER_NAME", "")

        textViewGreeting.text = "Hello, " + name;


        val db = AppDatabase.getDatabase(this)
        val carList = db.carDao().getAllCar()

        val carsView = findViewById<RecyclerView>(R.id.carListRv)
        carsView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val carAdapter = CarAdapter(this, carList, false)
        carsView.adapter = carAdapter

        val userIcon = findViewById<ImageView>(R.id.userIcon)
        userIcon.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
