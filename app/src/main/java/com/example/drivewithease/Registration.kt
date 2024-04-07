package com.example.drivewithease

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.drivewithease.entity.User
import java.security.MessageDigest

class Registration : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextUsername: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var registerBtn: Button
    private lateinit var textViewLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val loginTextView = findViewById<TextView>(R.id.textView_login)
        loginTextView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        editTextName = findViewById(R.id.editText_name_registration)
        editTextUsername = findViewById(R.id.editText_username_registration)
        editTextEmail = findViewById(R.id.editText_email_registration)
        editTextPassword = findViewById(R.id.editText_password_registration)
        registerBtn = findViewById(R.id.button_register)
        textViewLogin = findViewById(R.id.textView_login)

        registerBtn.setOnClickListener() {
            val name = editTextName.text.toString()
            val username = editTextUsername.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // Check if any field is empty
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Check if username or email is exist
            val db = AppDatabase.getDatabase(this)
            val findUsers = db.userDao().findUsernameAndEmailRegistration(username, email)
            if(findUsers.isNotEmpty()) {
                Toast.makeText(this, "Username or Email is exist. Please choose another", Toast.LENGTH_SHORT).show()
            } else {

                val hashedPassword = hashPassword(password)

                val newUser = User(name = name, email = email, password = hashedPassword, username = username)

                db.userDao().insertUser(newUser)

                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, MainActivity::class.java))
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
