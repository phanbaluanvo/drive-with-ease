package com.example.drivewithease.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.drivewithease.CarBookingActivity
import com.example.drivewithease.CarModifyActivity
import com.example.drivewithease.R
import com.example.drivewithease.entity.Car


class CarAdapter(private val context: Context, private val carList: List<Car>, private val isAdmin: Boolean) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val carModel: TextView = itemView.findViewById(R.id.carModel)
        val carMake: TextView = itemView.findViewById(R.id.carMake)
        val licensePlate: TextView = itemView.findViewById(R.id.licensePlate)
        val carImg: ImageView = itemView.findViewById(R.id.carImg)
        val carBtn: Button = itemView.findViewById(R.id.carBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_car_catalogue, parent, false)
        if(isAdmin) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        }
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val currentCar = carList[position]
        holder.carMake.text = currentCar.make
        holder.carModel.text = currentCar.model
        holder.licensePlate.text = currentCar.licensePlate


        Glide.with(context)
            .load(getImageResourceId(currentCar.imageName))
            .placeholder(R.drawable.default_car_image)
            .error(R.drawable.default_car_image)
            .into(holder.carImg)

        if(isAdmin) {
            holder.carBtn.setOnClickListener {
                val intent = Intent(context, CarModifyActivity::class.java)
                intent.putExtra("CAR_ID", currentCar.carId)
                context.startActivity(intent)
            }
        } else {
            holder.carBtn.setOnClickListener {
                val intent = Intent(context, CarBookingActivity::class.java)
                intent.putExtra("CAR_ID", currentCar.carId)
                context.startActivity(intent)
            }
        }


    }

    override fun getItemCount(): Int {
        return carList.size
    }

    fun getImageResourceId(imageName: String): Int {
        return context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }
}