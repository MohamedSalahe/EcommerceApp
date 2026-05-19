package com.example.project

import android.os.Bundle
import android.widget.Button
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

class CustomerProductsActivity : AppCompatActivity() {

    private lateinit var adapter: CustomerProductItems
    private lateinit var viewModel: CustomerViewModel
    private var currentCategory: String = "Food"

    override fun onResume() {
        super.onResume()
        if (::viewModel.isInitialized) {
            viewModel.loadProducts(currentCategory)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customerproducts)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val recyclerView = findViewById<RecyclerView>(R.id.CustomerProductsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this)[CustomerViewModel::class.java]

        val customerId = intent.getIntExtra("customer_id", 0)

        adapter = CustomerProductItems(
            onQuantityChanged = { productId, quantity ->
                viewModel.addToCart(customerId, productId.toIntOrNull() ?: 0, quantity)
            },
            onDetailsClicked = { item ->
                val intent = android.content.Intent(this, ProductDetailsActivity::class.java).apply {
                    putExtra("product_name",        item.name)
                    putExtra("product_price",       item.price)
                    putExtra("product_rating",      item.rating)
                    putExtra("product_stock",       item.stock)
                    putExtra("product_category",    item.category)
                    putExtra("product_description", item.description)
                    putExtra("product_views",       item.views)
                    putExtra("product_date",        item.date)
                    putExtra("product_image",       item.image)
                    putExtra("product_id",          item.productID)
                    putExtra("product_subcategory", item.subcategory)
                    putExtra("user_role",           "customer")
                }
                startActivity(intent)
            }
        )
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.productsState.collect { state ->
                when (state) {
                    is UiState.Success -> adapter.submitList(state.data)
                    is UiState.Error   -> Toast.makeText(this@CustomerProductsActivity, state.message, Toast.LENGTH_SHORT).show()
                    else -> {}
                }
            }
        }

        val btnFood     = findViewById<Button>(R.id.btnCategory1)
        val btnClothing = findViewById<Button>(R.id.btnCategory2)
        val btnLiving   = findViewById<Button>(R.id.btnCategory3)
        val btnElectronics = findViewById<Button>(R.id.btnCategory4)
        val btnPets     = findViewById<Button>(R.id.btnCategory5)
        val btnMisc     = findViewById<Button>(R.id.btnCategory6)

        val categoryButtons = listOf(btnFood, btnClothing, btnLiving, btnElectronics, btnPets, btnMisc)

        fun selectCategory(selectedButton: Button, category: String) {
            currentCategory = category
            categoryButtons.forEach { it.isSelected = (it == selectedButton) }
            viewModel.loadProducts(category)
        }

        btnFood.isSelected = true

        btnBack.setOnClickListener { finish() }

        findViewById<ImageView>(R.id.cartIcon).setOnClickListener {
            val cartIntent = android.content.Intent(this, CartActivity::class.java).apply {
                putExtra("customer_id", customerId)
            }
            startActivity(cartIntent)
        }

        btnFood.setOnClickListener        { selectCategory(btnFood, "Food") }
        btnClothing.setOnClickListener    { selectCategory(btnClothing, "Clothing") }
        btnLiving.setOnClickListener      { selectCategory(btnLiving, "Living & Leisure") }
        btnElectronics.setOnClickListener { selectCategory(btnElectronics, "Electronics") }
        btnPets.setOnClickListener        { selectCategory(btnPets, "Pets") }
        btnMisc.setOnClickListener        { selectCategory(btnMisc, "Misc") }
    }
}