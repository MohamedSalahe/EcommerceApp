package com.example.project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project.model.RegisterRequest
import com.example.project.network.RetrofitClient
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUsername          = findViewById<EditText>(R.id.Username_signup)
        val etPassword          = findViewById<EditText>(R.id.password_signup)
        val etName              = findViewById<EditText>(R.id.Name_signup)
        val btnCustomer         = findViewById<Button>(R.id.btnCustomer)
        val btnVendor           = findViewById<Button>(R.id.btnVendor)
        val btnSignup           = findViewById<Button>(R.id.btnSignup)
        val ownerVendorViewSign = findViewById<View>(R.id.OwnerVendorviewsign)
        val ownerVendorSignup   = findViewById<EditText>(R.id.OwnerVendorsignup)

        var selectedType = 0

        btnCustomer.isSelected = true
        btnVendor.isSelected   = false

        btnCustomer.setOnClickListener {
            selectedType           = 0
            btnCustomer.isSelected = true
            btnVendor.isSelected   = false
            ownerVendorViewSign.visibility = View.GONE
            ownerVendorSignup.visibility   = View.GONE
        }

        btnVendor.setOnClickListener {
            selectedType           = 1
            btnVendor.isSelected   = true
            btnCustomer.isSelected = false
            ownerVendorViewSign.visibility = View.VISIBLE
            ownerVendorSignup.visibility   = View.VISIBLE
        }

        btnSignup.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val name     = etName.text.toString().trim()
            val owner    = ownerVendorSignup.text.toString().trim()

            if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedType == 1 && owner.isEmpty()) {
                Toast.makeText(this, "Please enter the vendor owner name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            lifecycleScope.launch {
                try {
                    val request = if (selectedType == 0) {
                        RegisterRequest(
                            accountName     = username,
                            accountPassword = password,
                            accountType     = selectedType,
                            customerName    = name
                        )
                    } else {
                        RegisterRequest(
                            accountName     = username,
                            accountPassword = password,
                            accountType     = selectedType,
                            vendorName      = name,
                            vendorOwner     = owner
                        )
                    }

                    val response = RetrofitClient.apiService.register(request)

                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(this@RegisterActivity, "Account created!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Registration failed. Try a different username.", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(this@RegisterActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    btnSignup.isEnabled = true
                }
            }
        }
    }
}