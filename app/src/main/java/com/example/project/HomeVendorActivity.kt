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
import com.example.project.viewmodel.UiState
import com.example.project.viewmodel.VendorViewModel
import kotlinx.coroutines.launch

class HomeVendorActivity : AppCompatActivity() {

    private lateinit var viewModel: VendorViewModel

    private lateinit var vendorAvatar: TextView
    private lateinit var vendorNameView: TextView
    private lateinit var productCountView: TextView
    private lateinit var orderCountView: TextView
    private lateinit var ratingCountView: TextView

    private var vendorId = 0

    private val editLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.loadVendor(vendorId)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homevendor)

        viewModel = ViewModelProvider(this)[VendorViewModel::class.java]

        vendorId = intent.getIntExtra("vendor_id", 0)

        vendorAvatar = findViewById(R.id.vendorAvatar)
        vendorNameView = findViewById(R.id.VendorNameDisplay)
        productCountView = findViewById(R.id.ProductNumber)
        orderCountView = findViewById(R.id.OrderNumber)
        ratingCountView = findViewById(R.id.RatingNumber)

        findViewById<ImageView>(R.id.btnLogout).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.btnManageProducts).setOnClickListener {
            editLauncher.launch(
                Intent(this, ManageProductsVendorActivity::class.java).apply {
                    putExtra("vendor_id", vendorId)
                }
            )
        }

        findViewById<LinearLayout>(R.id.btnManageDetails).setOnClickListener {
            editLauncher.launch(
                Intent(this, EditVendorActivity::class.java).apply {
                    putExtra("vendor_id", vendorId)
                }
            )
        }

        findViewById<LinearLayout>(R.id.btnDeliveries).setOnClickListener {
            editLauncher.launch(
                Intent(this, OrdersActivity::class.java).apply {
                    putExtra("vendor_id", vendorId)
                    putExtra("account_type", 1)
                }
            )
        }

        viewModel.loadVendor(vendorId)

        observeVendor()
    }

    private fun observeVendor() {

        lifecycleScope.launch {
            viewModel.vendorState.collect { state ->

                when (state) {

                    is UiState.Loading -> {}

                    is UiState.Success -> {
                        val vendor = state.data
                        val name = vendor.vendorName ?: ""
                        val initials = if (name.contains(" ")) {
                            name.split(" ")
                                .filter { it.isNotEmpty() }
                                .take(2)
                                .map { it.first().uppercaseChar() }
                                .joinToString("")
                        } else {
                            name.take(2).uppercase()
                        }

                        vendorAvatar.text = initials
                        vendorNameView.text = name
                        orderCountView.text = vendor.orderCount?.toString() ?: "0"
                        productCountView.text = vendor.productCount?.toString() ?: "0"
                        ratingCountView.text = vendor.avgRating ?: "0.0"
                    }

                    is UiState.Error -> {
                        Toast.makeText(
                            this@HomeVendorActivity,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}