package com.example.drivewithease.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.drivewithease.AppDatabase
import com.example.drivewithease.BookingDetailActivity
import com.example.drivewithease.R
import com.example.drivewithease.entity.BookingHistory
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

class BookingHistoryAdapter(private val context: Context, private val bookingHistoryList: List<BookingHistory>) : RecyclerView.Adapter<BookingHistoryAdapter.BookingHistoryViewHolder>() {

    inner class BookingHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val startDate: TextView = itemView.findViewById(R.id.startDate)
        val endDate: TextView = itemView.findViewById(R.id.endDate)
        val customerName: TextView = itemView.findViewById(R.id.customerName)
        val totalPrice: TextView = itemView.findViewById(R.id.totalPrice)
        val cardView: CardView = itemView.findViewById(R.id.bookingCardView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedBookingHistory = bookingHistoryList[position]

                    val intent = Intent(context, BookingDetailActivity::class.java)

                    intent.putExtra("BOOKING_ID", clickedBookingHistory.id)

                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booking_history, parent, false)
        return BookingHistoryViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: BookingHistoryViewHolder, position: Int) {
        val currentBookingHistory = bookingHistoryList[position]

        val custName = AppDatabase.getDatabase(context).userDao().getCustomerNameById(currentBookingHistory.userId)

        val startDate = Instant.ofEpochMilli(currentBookingHistory.startDate)
        val endDate = Instant.ofEpochMilli(currentBookingHistory.endDate)

        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.CANADA)
        val formattedStartDate = formatter.format(Date.from(startDate))
        val formattedEndDate = formatter.format(Date.from(endDate))

        holder.startDate.text = formattedStartDate
        holder.endDate.text = formattedEndDate
        holder.customerName.text = custName
        holder.totalPrice.text = currentBookingHistory.totalPrice.toString()

        if (currentBookingHistory.status == "WAITING") {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.backgroundWaiting))
        } else if (currentBookingHistory.status == "CANCEL") {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.backgroundCancel))
        } else if (currentBookingHistory.status == "CONFIRMED") {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.backgroundConfirmed))
        }
    }


    override fun getItemCount(): Int {
        return bookingHistoryList.size
    }

}