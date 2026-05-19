package com.example.project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.project.viewmodel.AuthViewModel
import com.example.project.viewmodel.UiState
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etUsername = findViewById<EditText>(R.id.Username_entry)
        val etPassword = findViewById<EditText>(R.id.password_entry)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val gotosignup = findViewById<TextView>(R.id.gotosignup)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        btnLogin.setOnClickListener {

            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter username & password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(username, password)
        }

        lifecycleScope.launch {
            viewModel.loginState.collect { state ->

                when (state) {

                    is UiState.Success -> {

                        val account = state.data

                        if (account.accountType == 1) {

                            viewModel.loadVendor(account.vendorId!!)
                        } else {

                            viewModel.loadCustomer(account.customerId!!)
                        }
                    }

                    is UiState.Error -> {
                        Toast.makeText(this@MainActivity, state.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewModel.vendorState.collect { state ->

                when (state) {

                    is UiState.Success -> {
                        val vendor = state.data

                        startActivity(Intent(this@MainActivity, HomeVendorActivity::class.java).apply {
                            putExtra("vendor_id", vendor.vendorId)
                        })
                        finish()
                    }

                    is UiState.Error -> {
                        Toast.makeText(this@MainActivity, state.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewModel.customerState.collect { state ->

                when (state) {

                    is UiState.Success -> {
                        val customer = state.data

                        startActivity(Intent(this@MainActivity, HomeCustomerActivity::class.java).apply {
                            putExtra("customer_id", customer.customerId)
                        })
                        finish()
                    }

                    is UiState.Error -> {
                        Toast.makeText(this@MainActivity, state.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }

        gotosignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

}