package com.example.project

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.viewmodel.CustomerViewModel
import com.example.project.viewmodel.UiState
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {

    private lateinit var adapter: CartItemsAdapter
    private lateinit var viewModel: CustomerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val btnBack      = findViewById<ImageView>(R.id.btnBack)
        val recyclerView = findViewById<RecyclerView>(R.id.ProductsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel      = ViewModelProvider(this)[CustomerViewModel::class.java]
        val customerId = intent.getIntExtra("customer_id", 0)

        if (customerId == 0) {
            Toast.makeText(this, "Debug: customer_id is 0! Check Intent passing.", Toast.LENGTH_LONG).show()
        }

        adapter = CartItemsAdapter { productId, quantity ->
            viewModel.addToCart(customerId, productId.toIntOrNull() ?: 0, quantity)
        }
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.cartState.collect { state ->
                when (state) {
                    is UiState.Success ->
                        adapter.submitList(state.data)
                    is UiState.Error ->
                        Toast.makeText(this@CartActivity, state.message, Toast.LENGTH_SHORT).show()
                    else -> {}
                }
            }
        }

        val btnPlaceOrder = findViewById<android.widget.Button>(R.id.button)
        btnPlaceOrder.setOnClickListener {
            viewModel.completeOrder(customerId)
        }

        lifecycleScope.launch {
            viewModel.completeOrderState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        Toast.makeText(this@CartActivity, "Order Completed!", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    is UiState.Error -> {
                        Toast.makeText(this@CartActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }

        viewModel.loadCart(customerId)
        btnBack.setOnClickListener { finish() }
    }
}
