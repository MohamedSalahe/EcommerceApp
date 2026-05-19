package com.example.project

import android.os.Bundle
import android.widget.*
import android.net.Uri
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.project.viewmodel.VendorViewModel
import com.example.project.viewmodel.UiState
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import java.io.File

class EditProductActivity : AppCompatActivity() {

    private lateinit var viewModel: VendorViewModel

    private lateinit var layoutImagePicker: FrameLayout
    private lateinit var ivProductImage: ImageView
    private lateinit var etProductName: EditText
    private lateinit var etProductDescription: EditText
    private lateinit var spinnerProductCategory: Spinner
    private lateinit var etProductSubcategory: EditText
    private lateinit var etProductPrice: EditText
    private lateinit var etProductStock: EditText
    private lateinit var btnSaveChanges: Button
    private lateinit var btnBack: ImageView

    private var selectedImageUri: Uri? = null
    private var productId: String = ""

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            ivProductImage.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        layoutImagePicker = findViewById(R.id.layoutImagePicker)
        ivProductImage = findViewById(R.id.ivProductImage)
        etProductName = findViewById(R.id.etProductName)
        etProductDescription = findViewById(R.id.etProductDescription)
        spinnerProductCategory = findViewById(R.id.spinnerProductCategory)
        etProductSubcategory = findViewById(R.id.etProductSubcategory)
        etProductPrice = findViewById(R.id.etProductPrice)
        etProductStock = findViewById(R.id.etProductStock)
        btnSaveChanges = findViewById(R.id.btnSaveChanges)
        btnBack = findViewById(R.id.btnBack)

        viewModel = ViewModelProvider(this)[VendorViewModel::class.java]

        productId = intent.getStringExtra("product_id") ?: ""
        val name = intent.getStringExtra("product_name") ?: ""
        val price = intent.getStringExtra("product_price") ?: ""
        val stock = intent.getStringExtra("product_stock") ?: ""
        val category = intent.getStringExtra("product_category") ?: ""
        val description = intent.getStringExtra("product_description") ?: ""
        val subcategory = intent.getStringExtra("product_subcategory") ?: ""
        val image = intent.getStringExtra("product_image") ?: ""

        etProductName.setText(name)
        etProductPrice.setText(price)
        etProductStock.setText(stock)
        etProductDescription.setText(description)
        etProductSubcategory.setText(subcategory)
        
        if (image.isNotEmpty()) {
            Glide.with(this).load(image).into(ivProductImage)
        }

        val categories = arrayOf("Electronics", "Living & Leisure", "Food", "Pets", "Clothing", "Misc")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinnerProductCategory.adapter = adapter
        
        val categoryIndex = categories.indexOf(category)
        if (categoryIndex >= 0) {
            spinnerProductCategory.setSelection(categoryIndex)
        }

        layoutImagePicker.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        btnBack.setOnClickListener { finish() }

        btnSaveChanges.setOnClickListener {
            val updatedName = etProductName.text.toString().trim()
            val updatedDescription = etProductDescription.text.toString().trim()
            val updatedCategory = spinnerProductCategory.selectedItem.toString()
            val updatedSubcategory = etProductSubcategory.text.toString().trim()
            val updatedPriceStr = etProductPrice.text.toString().trim()
            val updatedStockStr = etProductStock.text.toString().trim()

            if (updatedName.isEmpty() || updatedDescription.isEmpty() || updatedPriceStr.isEmpty() || updatedStockStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val priceVal = updatedPriceStr.toDoubleOrNull()
            val stockVal = updatedStockStr.toIntOrNull()

            if (priceVal == null || stockVal == null) {
                Toast.makeText(this, "Invalid price or stock", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val imageFile = selectedImageUri?.let { uriToFile(it) }
            
            viewModel.updateProduct(
                productId.toIntOrNull() ?: 0,
                updatedName,
                updatedDescription,
                updatedCategory,
                updatedSubcategory,
                priceVal,
                stockVal,
                imageFile
            )
        }


        lifecycleScope.launch {
            viewModel.updateProductState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        Toast.makeText(this@EditProductActivity, state.data, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is UiState.Error -> {
                        Toast.makeText(this@EditProductActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun uriToFile(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("update_img", ".jpg", cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
