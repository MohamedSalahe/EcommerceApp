package com.example.project

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.network.RetrofitClient
import kotlinx.coroutines.launch
import com.example.project.model.ProductItem
import com.example.project.model.ProductItemDetails
import android.content.Intent
import androidx.activity.viewModels
import com.example.project.viewmodel.VendorViewModel
import com.example.project.viewmodel.UiState

class ManageProductsVendorActivity : AppCompatActivity() {

    private lateinit var adapter: ProductAdapter
    private val viewModel: VendorViewModel by viewModels()
    private var vendorId: Int = 0

    override fun onResume() {
        super.onResume()
        if (vendorId != 0) {
            loadProducts()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manageproducts)

        vendorId = intent.getIntExtra("vendor_id", 0)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnAdd = findViewById<ImageView>(R.id.btnAdd)

        btnAdd.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            intent.putExtra("vendor_id", vendorId)
            startActivity(intent)
        }

        lifecycleScope.launch {
            viewModel.deleteProductState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        Toast.makeText(this@ManageProductsVendorActivity, "Product deleted", Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Error -> {
                        Toast.makeText(this@ManageProductsVendorActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.ProductsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnBack.setOnClickListener { finish() }
    }

    private fun loadProducts() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getVendorProducts(vendorId)
                if (response.isSuccessful) {
                    val products = response.body() ?: emptyList()

                    val items = products.map { details ->
                        ProductItem(
                            name      = details.name,
                            rating    = details.rating,
                            stock     = details.stock,
                            productID = details.productID,
                            image     = details.image
                        )
                    }

                    val detailsMap = products.associateBy { it.productID }

                    val recyclerView = findViewById<RecyclerView>(R.id.ProductsRecyclerView)
                    adapter = ProductAdapter(
                        onDelete = { productId ->
                            val updatedList = adapter.currentList.filter { it.productID != productId }
                            adapter.submitList(updatedList)
                            viewModel.deleteVendorProduct(productId.toInt())
                        },
                        onDetails = { item ->
                            val details = detailsMap[item.productID] ?: return@ProductAdapter
                            val intent = Intent(this@ManageProductsVendorActivity, ProductDetailsActivity::class.java)
                            intent.putExtra("product_name",        details.name)
                            intent.putExtra("product_price",       details.price)
                            intent.putExtra("product_rating",      details.rating)
                            intent.putExtra("product_stock",       details.stock)
                            intent.putExtra("product_category",    details.category)
                            intent.putExtra("product_description", details.description)
                            intent.putExtra("product_views",       details.views)
                            intent.putExtra("product_date",        details.date)
                            intent.putExtra("product_image",       details.image)
                            intent.putExtra("product_id",          item.productID)
                            intent.putExtra("product_subcategory", details.subcategory)
                            intent.putExtra("user_role",           "vendor")
                            startActivity(intent)
                        }
                    )
                    recyclerView.adapter = adapter
                    adapter.submitList(items)
                }
            } catch (e: Exception) {
                Toast.makeText(this@ManageProductsVendorActivity, "Failed to load products", Toast.LENGTH_SHORT).show()
            }
        }
    }
}