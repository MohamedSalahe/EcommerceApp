package com.example.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.model.CustomerOrders
import com.example.project.model.VendorOrders
import com.example.project.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrdersViewModel : ViewModel() {

    private val repository = OrderRepository()

    private val _vendorOrdersState =
        MutableStateFlow<UiState<VendorOrders>>(UiState.Loading)

    val vendorOrdersState: StateFlow<UiState<VendorOrders>> =
        _vendorOrdersState

    private val _customerOrdersState =
        MutableStateFlow<UiState<CustomerOrders>>(UiState.Loading)

    val customerOrdersState: StateFlow<UiState<CustomerOrders>> =
        _customerOrdersState

    fun getVendorOrders(vendorId: Int) {

        _vendorOrdersState.value = UiState.Loading

        viewModelScope.launch {

            try {

                val response = repository.getVendorOrder(vendorId)

                if (response.isSuccessful && response.body() != null) {

                    _vendorOrdersState.value =
                        UiState.Success(response.body()!!)

                } else {

                    _vendorOrdersState.value =
                        UiState.Error("Failed to load vendor orders")
                }

            } catch (e: Exception) {

                _vendorOrdersState.value =
                    UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun getCustomerOrders(customerId: Int) {

        _customerOrdersState.value = UiState.Loading

        viewModelScope.launch {

            try {

                val response = repository.getCustomerOrder(customerId)

                if (response.isSuccessful && response.body() != null) {

                    _customerOrdersState.value =
                        UiState.Success(response.body()!!)

                } else {

                    _customerOrdersState.value =
                        UiState.Error("Failed to load customer orders")
                }

            } catch (e: Exception) {

                _customerOrdersState.value =
                    UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}