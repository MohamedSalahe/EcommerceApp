package com.example.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.model.Account
import com.example.project.repository.AuthRepository
import com.example.project.repository.CustomerRepository
import com.example.project.repository.VendorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val authRepo = AuthRepository()
    private val vendorRepo = VendorRepository()
    private val customerRepo = CustomerRepository()

    private val _loginState =
        MutableStateFlow<UiState<Account>>(UiState.Loading)
    val loginState: StateFlow<UiState<Account>> = _loginState

    private val _vendorState =
        MutableStateFlow<UiState<com.example.project.model.Vendor>>(UiState.Loading)
    val vendorState: StateFlow<UiState<com.example.project.model.Vendor>> = _vendorState

    private val _customerState =
        MutableStateFlow<UiState<com.example.project.model.Customer>>(UiState.Loading)
    val customerState: StateFlow<UiState<com.example.project.model.Customer>> = _customerState

    fun login(username: String, password: String) {

        viewModelScope.launch {

            try {

                val response = authRepo.login(username, password)

                if (response.isSuccessful && response.body() != null) {
                    _loginState.value = UiState.Success(response.body()!!)
                } else {
                    _loginState.value = UiState.Error("Invalid login")
                }

            } catch (e: Exception) {
                _loginState.value = UiState.Error(e.message ?: "Network error")
            }
        }
    }

    fun loadVendor(vendorId: Int) {

        viewModelScope.launch {

            try {

                val response = vendorRepo.getVendor(vendorId)

                if (response.isSuccessful && response.body() != null) {
                    _vendorState.value = UiState.Success(response.body()!!)
                } else {
                    _vendorState.value = UiState.Error("Vendor load failed")
                }

            } catch (e: Exception) {
                _vendorState.value = UiState.Error("Network error")
            }
        }
    }

    fun loadCustomer(customerId: Int) {

        viewModelScope.launch {

            try {

                val response = customerRepo.getCustomer(customerId)

                if (response.isSuccessful && response.body() != null) {
                    _customerState.value = UiState.Success(response.body()!!)
                } else {
                    _customerState.value = UiState.Error("Customer load failed")
                }

            } catch (e: Exception) {
                _customerState.value = UiState.Error("Network error")
            }
        }
    }
}