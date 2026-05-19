package com.example.project

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.project.viewmodel.CustomerViewModel
import com.example.project.viewmodel.UiState
import kotlinx.coroutines.launch

class EditCustomerActivity : AppCompatActivity() {

    private lateinit var viewModel: CustomerViewModel
    private lateinit var phoneContainer: LinearLayout

    private val phonesToDelete = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editcustomer)

        viewModel = ViewModelProvider(this)[CustomerViewModel::class.java]

        val customerId = intent.getIntExtra("customer_id", 0)

        val etCustomerName = findViewById<EditText>(R.id.EditName)
        val etCustomerState = findViewById<EditText>(R.id.EditState)
        val etCustomerCity = findViewById<EditText>(R.id.EditCity)
        val etCustomerStreet = findViewById<EditText>(R.id.EditStreet)

        val btnAddPhone = findViewById<Button>(R.id.btnAddPhone)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnSave = findViewById<ImageView>(R.id.btnSave)

        phoneContainer = findViewById(R.id.PhoneContainer)

        viewModel.getCustomer(customerId)

        lifecycleScope.launch {
            viewModel.customerState.collect { state ->

                when (state) {

                    is UiState.Loading -> {}

                    is UiState.Success -> {
                        val customer = state.data

                        etCustomerName.setText(customer.customerName ?: "")
                        etCustomerState.setText(customer.customerState ?: "")
                        etCustomerCity.setText(customer.customerCity ?: "")
                        etCustomerStreet.setText(customer.customerStreet ?: "")

                        phoneContainer.removeAllViews()
                        customer.customerPhones?.forEach {
                            addPhoneRow(it)
                        }
                    }

                    is UiState.Error -> {
                        Toast.makeText(
                            this@EditCustomerActivity,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.updateCustomerState.collect { state ->

                when (state) {

                    is UiState.Loading -> {}

                    is UiState.Success -> {
                        Toast.makeText(
                            this@EditCustomerActivity,
                            "Saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }

                    is UiState.Error -> {
                        Toast.makeText(
                            this@EditCustomerActivity,
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

            val phoneList = mutableListOf<String>()

            for (i in 0 until phoneContainer.childCount) {

                val row = phoneContainer.getChildAt(i) as LinearLayout
                val et = row.getChildAt(0) as EditText

                val phone = et.text.toString().trim()

                if (phone.isNotEmpty()) {
                    phoneList.add(phone)
                }
            }

            viewModel.saveCustomer(
                customerId = customerId,
                name = etCustomerName.text.toString().trim(),
                state = etCustomerState.text.toString().trim(),
                city = etCustomerCity.text.toString().trim(),
                street = etCustomerStreet.text.toString().trim(),
                phones = phoneList,
                phonesToDelete = phonesToDelete
            )
        }
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