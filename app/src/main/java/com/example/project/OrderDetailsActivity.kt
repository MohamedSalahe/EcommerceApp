package com.example.project

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.model.OrderDetails2
import com.example.project.network.RetrofitClient
import kotlinx.coroutines.launch
import kotlin.collections.indices

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView

    private lateinit var orderNo: TextView
    private lateinit var orderDate: TextView
    private lateinit var byCustomer: TextView
    private lateinit var addressText: TextView
    private lateinit var totalText: TextView
    private lateinit var productsContainer: LinearLayout

    private lateinit var adapter: OrderDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orderdetails)

        btnBack = findViewById(R.id.btnBack)
        orderNo = findViewById(R.id.OrderNO)
        orderDate = findViewById(R.id.OrderDate)
        byCustomer = findViewById(R.id.Bycustomer)
        addressText = findViewById(R.id.Addresstext)
        totalText = findViewById(R.id.TotalNumber)



        val orderId = intent.getStringExtra("order_id")
        val vendorId = intent.getIntExtra("vendor_id", 0)
        val accountType = intent.getIntExtra("account_type", 0)

        val recyclerView = findViewById<RecyclerView>(R.id.ProductsRecyclerView2)

        recyclerView.layoutManager = LinearLayoutManager(this)
        productsContainer = findViewById(R.id.ProductsContainer)

        lifecycleScope.launch {
            try {
                val response = if (accountType == 1) {
                    RetrofitClient.apiService.getOrderDetails(orderId, vendorId)
                } else {
                    RetrofitClient.apiService.getCustomerOrderDetails(orderId)
                }
                if (response.isSuccessful) {
                    val order = response.body()!!
                    totalText.text = "${order.total}$"
                    addressText.text = "${order.customerCity}, ${order.customerState}"
                    orderNo.text = "Order #$orderId"
                    orderDate.text = order.orderDate
                    byCustomer.text = order.customerName
                    val prices = order.orderPrices  ?: emptyList()
                    val counts = order.orderCounts  ?: emptyList()
                    val products = order.orderItems ?: emptyList()
                    val productIds = order.productId ?: emptyList()

                    val items = products.indices.map { i ->
                        OrderDetails2(
                            orderItem = products.getOrNull(i) ?: "",
                            itemPrice = prices.getOrNull(i) ?: "",
                            itemCount = counts.getOrNull(i) ?: "",
                            productId = productIds.getOrNull(i) ?: ""
                        )
                    }
                    adapter = OrderDetailsAdapter()
                    recyclerView.adapter = adapter
                    adapter.submitList(items)

                }
            } catch (e: Exception) {
                Toast.makeText(this@OrderDetailsActivity, "Failed to load order data", Toast.LENGTH_SHORT).show()
            }
        }
        btnBack.setOnClickListener {
            finish()
        }
    }

}