package com.example.project

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.model.OrderItems
import com.example.project.network.RetrofitClient
import com.example.project.viewmodel.UiState
import com.example.project.viewmodel.VendorViewModel
import com.example.project.viewmodel.CustomerViewModel
import kotlinx.coroutines.launch

class OrdersActivity : AppCompatActivity() {

    private lateinit var adapter: OrderAdapter

    private var vendorId: Int = 0
    private var customerId: Int = 0
    private var accountType: Int = 0

    private val vendorViewModel: VendorViewModel by viewModels()
    private val customerViewModel: CustomerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordersvendor)

        vendorId = intent.getIntExtra("vendor_id", 0)
        customerId = intent.getIntExtra("customer_id", 0)
        accountType = intent.getIntExtra("account_type", 0)

        val recyclerView = findViewById<RecyclerView>(R.id.OrdersRecyclerView)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            try {
                if (accountType == 1) {

                    val response =
                        RetrofitClient.apiService.getVendorOrders(vendorId)

                    if (response.isSuccessful) {

                        val vendorOrders = response.body()!!

                        val orders = vendorOrders.vendorOrders ?: emptyList()
                        val counts = vendorOrders.productCount ?: emptyList()
                        val prices = vendorOrders.price ?: emptyList()
                        val dates = vendorOrders.date ?: emptyList()

                        val items = orders.indices.map { i ->
                            OrderItems(
                                productCount = counts.getOrNull(i) ?: "",
                                price = prices.getOrNull(i) ?: "",
                                date = dates.getOrNull(i) ?: "",
                                orderID = orders.getOrNull(i) ?: ""
                            )
                        }

                        adapter = OrderAdapter(

                            onDelete = { orderId ->
                                vendorViewModel.deleteVendorOrders(orderId, vendorId)
                            },

                            onDetails = {
                                val intent = Intent(
                                    this@OrdersActivity,
                                    OrderDetailsActivity::class.java
                                )
                                intent.putExtra("order_id", it.orderID)
                                intent.putExtra("vendor_id", vendorId)
                                intent.putExtra("account_type", accountType)
                                startActivity(intent)
                            }
                        )

                        recyclerView.adapter = adapter
                        adapter.submitList(items)
                    }

                } else {

                    val response =
                        RetrofitClient.apiService.getCustomerOrders(customerId)

                    if (response.isSuccessful) {

                        val customerOrders = response.body()!!

                        val orders = customerOrders.customerOrders ?: emptyList()
                        val counts = customerOrders.productCount ?: emptyList()
                        val prices = customerOrders.price ?: emptyList()
                        val dates = customerOrders.date ?: emptyList()

                        val items = orders.indices.map { i ->
                            OrderItems(
                                productCount = counts.getOrNull(i) ?: "",
                                price = prices.getOrNull(i) ?: "",
                                date = dates.getOrNull(i) ?: "",
                                orderID = orders.getOrNull(i) ?: ""
                            )
                        }

                        adapter = OrderAdapter(

                            onDelete = { orderId ->
                                customerViewModel.deleteCustomerOrders(orderId, customerId)
                            },

                            onDetails = {
                                val intent = Intent(
                                    this@OrdersActivity,
                                    OrderDetailsActivity::class.java
                                )
                                intent.putExtra("order_id", it.orderID)
                                intent.putExtra("vendor_id", 0)
                                intent.putExtra("account_type", accountType)
                                startActivity(intent)
                            }
                        )

                        recyclerView.adapter = adapter
                        adapter.submitList(items)
                    }
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@OrdersActivity,
                    "Failed to load orders",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        lifecycleScope.launch {

            vendorViewModel.deleteOrderState.collect { state ->

                when (state) {

                    is UiState.Success -> {

                        val updatedList = adapter.currentList.filter {
                            it.orderID != state.data
                        }

                        adapter.submitList(updatedList)

                        Toast.makeText(
                            this@OrdersActivity,
                            "Order deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is UiState.Error -> {

                        Toast.makeText(
                            this@OrdersActivity,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {

            customerViewModel.deleteOrderState.collect { state ->

                when (state) {

                    is UiState.Success -> {

                        val updatedList = adapter.currentList.filter {
                            it.orderID != state.data
                        }

                        adapter.submitList(updatedList)

                        Toast.makeText(
                            this@OrdersActivity,
                            "Order deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is UiState.Error -> {

                        Toast.makeText(
                            this@OrdersActivity,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> Unit
                }
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}