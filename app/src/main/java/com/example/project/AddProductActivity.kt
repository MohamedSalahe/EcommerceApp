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

class AddProductActivity : AppCompatActivity() {

    private lateinit var viewModel: VendorViewModel

    private lateinit var layoutImagePicker: FrameLayout
    private lateinit var imgProductPreview: ImageView
    private lateinit var etProductName: EditText
    private lateinit var etProductDescription: EditText
    private lateinit var spinnerProductCategory: Spinner
    private lateinit var etProductSubcategory: EditText
    private lateinit var etProductPrice: EditText
    private lateinit var etProductStock: EditText
    private lateinit var btnPublishProduct: Button

    private var selectedImageUri: Uri? = null

    private var vendorId = 0
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            imgProductPreview.setImageURI(uri)
            imgProductPreview.visibility = View.VISIBLE
            findViewById<LinearLayout>(R.id.layoutImagePlaceholder).visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addproduct)

        layoutImagePicker = findViewById(R.id.layoutImagePicker)
        imgProductPreview = findViewById(R.id.imgProductPreview)
        etProductName = findViewById(R.id.etProductName)
        etProductDescription = findViewById(R.id.etProductDescription)
        spinnerProductCategory = findViewById(R.id.spinnerProductCategory)
        etProductSubcategory = findViewById(R.id.etProductSubcategory)
        etProductPrice = findViewById(R.id.etProductPrice)
        etProductStock = findViewById(R.id.etProductStock)
        btnPublishProduct = findViewById(R.id.btnPublishProduct)

        viewModel = ViewModelProvider(this)[VendorViewModel::class.java]

        vendorId = intent.getIntExtra("vendor_id", 0)

        val categories = arrayOf("Electronics", "Living & Leisure", "Food", "Pets", "Clothing", "Misc")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinnerProductCategory.adapter = adapter

        layoutImagePicker.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        btnPublishProduct.setOnClickListener {
            val name = etProductName.text.toString().trim()
            val description = etProductDescription.text.toString().trim()
            val category = spinnerProductCategory.selectedItem.toString()
            val subcategory = etProductSubcategory.text.toString().trim()
            val priceStr = etProductPrice.text.toString().trim()
            val stockStr = etProductStock.text.toString().trim()

            if (selectedImageUri == null) {
                Toast.makeText(this, "Please select a product image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (name.isEmpty() || description.isEmpty() || category == "Select Category" || 
                priceStr.isEmpty() || stockStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = priceStr.toDoubleOrNull()
            val stock = stockStr.toIntOrNull()

            if (price == null || price <= 0) {
                Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (stock == null || stock < 0) {
                Toast.makeText(this, "Please enter a valid stock quantity", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (vendorId == 0) {
                Toast.makeText(this, "Error: Vendor ID missing", Toast.LENGTH_SHORT).show()
            }

            val imageFile = uriToFile(selectedImageUri!!)
            if (imageFile == null) {
                Toast.makeText(this, "Failed to process image for upload", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.addProduct(vendorId, name, description, category, subcategory, price, stock, imageFile)
        }



        lifecycleScope.launch {
            viewModel.addProductState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        Toast.makeText(this@AddProductActivity, "Product Published!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is UiState.Error -> {
                        Toast.makeText(this@AddProductActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }

    }

    private fun uriToFile(uri: Uri): java.io.File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val tempFile = java.io.File.createTempFile("upload_img", ".jpg", cacheDir)
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