package com.example.project

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.project.viewmodel.CustomerViewModel
import com.example.project.viewmodel.UiState
import kotlinx.coroutines.launch

class HomeCustomerActivity : AppCompatActivity() {

    private lateinit var viewModel: CustomerViewModel

    private lateinit var customerAvatar: TextView
    private lateinit var customerNameView: TextView
    private lateinit var orderCountView: TextView
    private lateinit var itemCountView: TextView
    private lateinit var totalSpentView: TextView

    private var customerId = 0

    private val editLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.loadCustomer(customerId)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homecustomer)

        viewModel = ViewModelProvider(this)[CustomerViewModel::class.java]

        customerId = intent.getIntExtra("customer_id", 0)

        customerAvatar = findViewById(R.id.CustomerAvatar)
        customerNameView = findViewById(R.id.CustomerNameDisplay)
        orderCountView = findViewById(R.id.OrderNumber)
        itemCountView = findViewById(R.id.ItemsNumber)
        totalSpentView = findViewById(R.id.SpentNumber)

        findViewById<ImageView>(R.id.btnLogout).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.btnManageDetails).setOnClickListener {
            editLauncher.launch(
                Intent(this, EditCustomerActivity::class.java).apply {
                    putExtra("customer_id", customerId)
                }
            )
        }

        findViewById<LinearLayout>(R.id.btnOrders).setOnClickListener {
            editLauncher.launch(
                Intent(this, OrdersActivity::class.java).apply {
                    putExtra("customer_id", customerId)
                    putExtra("account_type", 0)
                }
            )
        }
        findViewById<LinearLayout>(R.id.btnManageProducts).setOnClickListener {
            editLauncher.launch(
                Intent(this, CustomerProductsActivity::class.java).apply {
                    putExtra("customer_id", customerId)
                }
            )
        }

        viewModel.loadCustomer(customerId)

        observeCustomer()
    }

    private fun observeCustomer() {

        lifecycleScope.launch {
            viewModel.customerState.collect { state ->

                when (state) {

                    is UiState.Loading -> {}

                    is UiState.Success -> {

                        val customer = state.data

                        val name = customer.customerName ?: ""

                        val initials = if (name.contains(" ")) {
                            name.split(" ")
                                .filter { it.isNotEmpty() }
                                .take(2)
                                .map { it.first().uppercaseChar() }
                                .joinToString("")
                        } else {
                            name.take(2).uppercase()
                        }

                        customerAvatar.text = initials
                        customerNameView.text = name
                        orderCountView.text = customer.orderCount?.toString() ?: "0"
                        itemCountView.text = customer.itemCount?.toString() ?: "0"
                        totalSpentView.text = customer.totalSpent ?: "0.00"
                    }

                    is UiState.Error -> {
                        Toast.makeText(
                            this@HomeCustomerActivity,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}