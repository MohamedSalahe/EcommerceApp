package com.example.project

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.project.network.RetrofitClient
import kotlinx.coroutines.launch
import android.content.Intent

class ProductDetailsActivity : AppCompatActivity() {

    private var productId: String = ""
    private var userRole: String = "customer"
    private var currentViews: Int = 0

    override fun onResume() {
        super.onResume()
        if (productId.isNotEmpty()) {
            loadProductDetails()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        productId = intent.getStringExtra("product_id") ?: ""
        userRole = intent.getStringExtra("user_role") ?: "customer"
        val viewsStr = intent.getStringExtra("product_views") ?: "0"
        currentViews = viewsStr.toIntOrNull() ?: 0

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (userRole == "customer" && productId.isNotEmpty()) {
            lifecycleScope.launch {
                try {
                    RetrofitClient.apiService.incrementViews(productId.toInt())
                    currentViews++
                    findViewById<TextView>(R.id.tvViews).text = "$currentViews Views"
                } catch (e: Exception) {}
            }
        }
        
        displayInitialData()
    }

    private fun displayInitialData() {
        val name = intent.getStringExtra("product_name") ?: ""
        val price = intent.getStringExtra("product_price") ?: ""
        val rating = intent.getStringExtra("product_rating") ?: ""
        val stock = intent.getStringExtra("product_stock") ?: ""
        val category = intent.getStringExtra("product_category") ?: ""
        val description = intent.getStringExtra("product_description") ?: ""
        val date = intent.getStringExtra("product_date") ?: ""
        val image = intent.getStringExtra("product_image") ?: ""
        val subcategory = intent.getStringExtra("product_subcategory") ?: ""

        updateUI(name, price, rating, stock, category, description, date, image, subcategory)
    }

    private fun loadProductDetails() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getProductDetails(productId.toInt())
                if (response.isSuccessful && response.body() != null) {
                    val details = response.body()!!
                    updateUI(
                        details.name,
                        details.price,
                        details.rating,
                        details.stock,
                        details.category,
                        details.description,
                        details.date,
                        details.image,
                        details.subcategory
                    )
                }
            } catch (e: Exception) {}
        }
    }

    private fun updateUI(name: String, price: String, rating: String, stock: String, category: String, description: String, date: String, image: String, subcategory: String) {
        findViewById<TextView>(R.id.tvProductName).text = name
        findViewById<TextView>(R.id.tvProductPrice).text = "$$price"
        findViewById<TextView>(R.id.tvRating).text = "★ $rating"
        findViewById<TextView>(R.id.tvStockStatus).text = "In Stock ($stock)"
        findViewById<TextView>(R.id.tvCategoryInfo).text = category
        findViewById<TextView>(R.id.tvDescription).text = description
        findViewById<TextView>(R.id.tvViews).text = "$currentViews Views"
        findViewById<TextView>(R.id.tvDateAdded).text = "Added: $date"

        val ivProductImage = findViewById<ImageView>(R.id.ivProductImage)
        if (image.isNotEmpty()) {
            Glide.with(this).load(image).into(ivProductImage)
        }

        val btnVendorAction = findViewById<ImageView>(R.id.btnVendorAction)
        btnVendorAction.visibility = if (userRole == "vendor") View.VISIBLE else View.GONE
        btnVendorAction.setOnClickListener {
            val intent = Intent(this, EditProductActivity::class.java).apply {
                putExtra("product_id", productId)
                putExtra("product_name", name)
                putExtra("product_price", price)
                putExtra("product_stock", stock)
                putExtra("product_category", category)
                putExtra("product_description", description)
                putExtra("product_subcategory", subcategory)
                putExtra("product_image", image)
            }
            startActivity(intent)
        }
    }
}