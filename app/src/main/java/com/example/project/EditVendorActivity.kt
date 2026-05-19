package com.example.project

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.project.viewmodel.UiState
import com.example.project.viewmodel.VendorViewModel
import kotlinx.coroutines.launch

class EditVendorActivity : AppCompatActivity() {

    private lateinit var viewModel: VendorViewModel
    private lateinit var phoneContainer: LinearLayout

    private val phonesToDelete = mutableListOf<String>()

    private var vendorId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editvendor)

        viewModel = ViewModelProvider(this)[VendorViewModel::class.java]

        vendorId = intent.getIntExtra("vendor_id", 0)

        val etVendorName = findViewById<EditText>(R.id.EditName)
        val etVendorOwner = findViewById<EditText>(R.id.EditOwner)
        val etVendorState = findViewById<EditText>(R.id.EditState)
        val etVendorCity = findViewById<EditText>(R.id.EditCity)
        val etVendorStreet = findViewById<EditText>(R.id.EditStreet)

        val btnAddPhone = findViewById<Button>(R.id.btnAddPhone)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnSave = findViewById<ImageView>(R.id.btnSave)
        val btnConfirm = findViewById<Button>(R.id.btnConfirm)


        phoneContainer = findViewById(R.id.PhoneContainer)

        viewModel.getVendor(vendorId)

        lifecycleScope.launch {
            viewModel.vendorState.collect { state ->

                when (state) {

                    is UiState.Loading -> {}

                    is UiState.Success -> {
                        val vendor = state.data

                        etVendorName.setText(vendor.vendorName ?: "")
                        etVendorOwner.setText(vendor.vendorOwner ?: "")
                        etVendorState.setText(vendor.vendorState ?: "")
                        etVendorCity.setText(vendor.vendorCity ?: "")
                        etVendorStreet.setText(vendor.vendorStreet ?: "")

                        phoneContainer.removeAllViews()
                        vendor.vendorPhones?.forEach {
                            addPhoneRow(it)
                        }
                    }

                    is UiState.Error -> {
                        Toast.makeText(
                            this@EditVendorActivity,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.updateVendorState.collect { state ->

                when (state) {

                    is UiState.Loading -> {}

                    is UiState.Success -> {
                        Toast.makeText(
                            this@EditVendorActivity,
                            "Saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }

                    is UiState.Error -> {
                        Toast.makeText(
                            this@EditVendorActivity,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnAddPhone.setOnClickListener {
            addPhoneRow("")
        }

        btnSave.setOnClickListener {
            saveVendorData(
                currentPhones = getEnteredPhones(),
                name = etVendorName.text.toString().trim(),
                owner = etVendorOwner.text.toString().trim(),
                state = etVendorState.text.toString().trim(),
                city = etVendorCity.text.toString().trim(),
                street = etVendorStreet.text.toString().trim()
            )
        }

        btnConfirm.setOnClickListener {
            saveVendorData(
                currentPhones = getEnteredPhones(),
                name = etVendorName.text.toString().trim(),
                owner = etVendorOwner.text.toString().trim(),
                state = etVendorState.text.toString().trim(),
                city = etVendorCity.text.toString().trim(),
                street = etVendorStreet.text.toString().trim()
            )
        }
    }

    private fun getEnteredPhones(): List<String> {
        val phoneList = mutableListOf<String>()
        for (i in 0 until phoneContainer.childCount) {
            val row = phoneContainer.getChildAt(i) as LinearLayout
            val et = row.getChildAt(0) as EditText
            val phone = et.text.toString().trim()
            if (phone.isNotEmpty()) phoneList.add(phone)
        }
        return phoneList
    }
    private fun saveVendorData(currentPhones: List<String>, name: String, owner: String, state: String, city: String, street: String) {

        viewModel.saveVendor(
            vendorId = vendorId,
            name = name,
            owner = owner,
            state = state,
            city = city,
            street = street,
            phones = currentPhones,
            phonesToDelete = phonesToDelete
        )
    }

    private fun addPhoneRow(phone: String) {

        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val et = EditText(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            setText(phone)
            hint = "Phone number"
        }

        val btnDelete = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(100, 100)
            setImageResource(R.drawable.ic_delete)

            setOnClickListener {
                if (phone.isNotEmpty()) {
                    phonesToDelete.add(phone)
                }
                phoneContainer.removeView(row)
            }
        }

        row.addView(et)
        row.addView(btnDelete)

        phoneContainer.addView(row)
    }
}