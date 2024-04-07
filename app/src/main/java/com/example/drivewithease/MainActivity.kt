package com.example.drivewithease

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import com.example.drivewithease.entity.User
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnNavigate: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTextUsername = findViewById(R.id.editText_username)
        editTextPassword = findViewById(R.id.editText_password)



        val db = AppDatabase.getDatabase(this)

        btnNavigate = findViewById(R.id.buttonNavigate);

        btnNavigate.setOnClickListener{

            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()


            val loginAdmin = db.adminDao().getAdminByUsername(username)

            if(loginAdmin != null) {
                val dbPassword = loginAdmin.password
                if(dbPassword != password) {
                    Toast.makeText(this, "Your password is incorrect. Please Try Again!", Toast.LENGTH_SHORT).show()
                } else {
                    sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
                    val editor = sharedPreferences.edit();
                    editor.putInt("USER_ID", loginAdmin.id).apply()
                    editor.putString("USERNAME", loginAdmin.username).apply()
                    editor.putString("USER_NAME", loginAdmin.name).apply()
                    editor.putString("PASSWORD", password).apply()
                    val intent = Intent(this, AdminManagementActivity::class.java)
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Admin not found!", Toast.LENGTH_SHORT).show()
            }
        }

        val signUpTextView = findViewById<TextView>(R.id.textView_signup)
        signUpTextView.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }

        btnLogin = findViewById(R.id.button_login)

        btnLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()


            val loginUser = db.userDao().findUserByUsername(username)

            if(loginUser != null) {
                val hashedPassword = hashPassword(password)
                val dbPassword = loginUser.password
                if(dbPassword != hashedPassword) {
                    Toast.makeText(this, "Your password is incorrect. Please Try Again!", Toast.LENGTH_SHORT).show()
                } else {
                    sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
                    val editor = sharedPreferences.edit();
                    editor.putInt("USER_ID", loginUser.id).apply()
                    editor.putString("USERNAME", loginUser.username).apply()
                    editor.putString("USER_NAME", loginUser.name).apply()
                    editor.putString("PASSWORD", password).apply()
                    startActivity(Intent(this, CarCatalogueActivity::class.java))
                }
            } else {
                Toast.makeText(this, "Username not found!", Toast.LENGTH_SHORT).show()
            }
        }



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