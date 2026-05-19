package com.example.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.model.Customer
import com.example.project.model.CustomerOrders
import com.example.project.model.CustomerPhonesUpdateRequest
import com.example.project.model.CustomerUpdateRequest
import com.example.project.model.Orderdetails
import com.example.project.model.ProductItemDetails
import com.example.project.model.CartItem
import com.example.project.repository.CustomerRepository
import com.example.project.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CustomerViewModel : ViewModel() {

    private val repository = CustomerRepository()
    private val orderRepository = OrderRepository()

    private val _customerState =
        MutableStateFlow<UiState<Customer>>(UiState.Loading)

    val customerState: StateFlow<UiState<Customer>> =
        _customerState

    private val _updateCustomerState =
        MutableStateFlow<UiState<Customer>>(UiState.Loading)

    val updateCustomerState: StateFlow<UiState<Customer>> =
        _updateCustomerState

    private val _customerOrdersState =
        MutableStateFlow<UiState<CustomerOrders>>(UiState.Loading)

    val customerOrdersState: StateFlow<UiState<CustomerOrders>> =
        _customerOrdersState

    private val _orderDetailsState =
        MutableStateFlow<UiState<Orderdetails>>(UiState.Loading)

    val orderDetailsState: StateFlow<UiState<Orderdetails>> =
        _orderDetailsState

    private val _deleteOrderState =
        MutableStateFlow<UiState<String>>(UiState.Loading)

    val deleteOrderState: StateFlow<UiState<String>> =
        _deleteOrderState

    private val _productsState = MutableStateFlow<UiState<List<ProductItemDetails>>>(UiState.Loading)
    val productsState: StateFlow<UiState<List<ProductItemDetails>>> = _productsState

    private val _cartState = MutableStateFlow<UiState<List<CartItem>>>(UiState.Loading)
    val cartState: StateFlow<UiState<List<CartItem>>> = _cartState

    private val _completeOrderState = MutableStateFlow<UiState<String>>(UiState.Loading)
    val completeOrderState: StateFlow<UiState<String>> = _completeOrderState

    fun getCustomer(customerId: Int) {

        _customerState.value = UiState.Loading

        viewModelScope.launch {

            try {

                val response = repository.getCustomer(customerId)

                if (response.isSuccessful && response.body() != null) {

                    _customerState.value =
                        UiState.Success(response.body()!!)

                } else {

                    _customerState.value =
                        UiState.Error("Failed")
                }

            } catch (e: Exception) {

                _customerState.value =
                    UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun updateCustomer(request: CustomerUpdateRequest) {

        _updateCustomerState.value = UiState.Loading

        viewModelScope.launch {

            try {

                val response = repository.updateCustomer(request)

                if (response.isSuccessful && response.body() != null) {

                    _updateCustomerState.value =
                        UiState.Success(response.body()!!)

                } else {

                    _updateCustomerState.value =
                        UiState.Error("Update Failed")
                }

            } catch (e: Exception) {

                _updateCustomerState.value =
                    UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun updateCustomerPhones(request: CustomerPhonesUpdateRequest) {

        _updateCustomerState.value = UiState.Loading

        viewModelScope.launch {

            try {

                val response =
                    repository.updateCustomerPhones(request)

                if (response.isSuccessful && response.body() != null) {

                    _updateCustomerState.value =
                        UiState.Success(response.body()!!)

                } else {

                    _updateCustomerState.value =
                        UiState.Error("Phone Update Failed")
                }

            } catch (e: Exception) {

                _updateCustomerState.value =
                    UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun getCustomerOrders(customerId: Int) {

        _customerOrdersState.value = UiState.Loading

        viewModelScope.launch {

            try {

                val response =
                    repository.getCustomerOrders(customerId)

                if (response.isSuccessful && response.body() != null) {

                    _customerOrdersState.value =
                        UiState.Success(response.body()!!)

                } else {

                    _customerOrdersState.value =
                        UiState.Error("Failed")
                }

            } catch (e: Exception) {

                _customerOrdersState.value =
                    UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun deleteCustomerOrders(orderNo: String?, customerId: Int) {
        viewModelScope.launch {
            _deleteOrderState.value = UiState.Loading
            try {
                repository.deleteCustomerOrders(orderNo, customerId)
                _deleteOrderState.value = UiState.Success(orderNo ?: "")
            } catch (e: Exception) {
                _deleteOrderState.value = UiState.Error(e.message ?: "Failed to delete order")
            }
        }
    }

    fun getOrderDetails(orderId: String?) {
        _orderDetailsState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = orderRepository.getCustomerOrderDetails(orderId)
                if (response.isSuccessful && response.body() != null) {
                    _orderDetailsState.value = UiState.Success(response.body()!!)
                } else {
                    _orderDetailsState.value = UiState.Error("Failed to load order details")
                }
            } catch (e: Exception) {
                _orderDetailsState.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun loadCustomer(customerId: Int) {

        viewModelScope.launch {

            _customerState.value = UiState.Loading

            try {

                val response = repository.getCustomer(customerId)

                if (response.isSuccessful && response.body() != null) {
                    _customerState.value = UiState.Success(response.body()!!)
                } else {
                    _customerState.value = UiState.Error("Failed")
                }

            } catch (e: Exception) {
                _customerState.value = UiState.Error(e.message ?: "Error")
            }
        }
    }

    fun saveCustomer(
        customerId: Int,
        name: String,
        state: String,
        city: String,
        street: String,
        phones: List<String>,
        phonesToDelete: List<String>
    ) {
        viewModelScope.launch {

            _updateCustomerState.value = UiState.Loading

            try {

                val customerResponse = repository.updateCustomer(
                    CustomerUpdateRequest(
                        customerId = customerId,
                        customerName = name,
                        customerState = state,
                        customerCity = city,
                        customerStreet = street
                    )
                )

                if (!customerResponse.isSuccessful) {
                    _updateCustomerState.value = UiState.Error("Failed customer update")
                    return@launch
                }

                val phoneResponse = repository.updateCustomerPhones(
                    CustomerPhonesUpdateRequest(
                        customerId = customerId,
                        phones = phones,
                        phonesToDelete = phonesToDelete
                    )
                )

                if (!phoneResponse.isSuccessful) {
                    _updateCustomerState.value = UiState.Error("Failed phone update")
                    return@launch
                }

                _updateCustomerState.value =
                    UiState.Success(customerResponse.body()!!)
            } catch (e: Exception) {
                _updateCustomerState.value =
                    UiState.Error(e.message ?: "Network error")
            }
        }
    }

    fun loadProducts(category: String) {
        viewModelScope.launch {
            _productsState.value = UiState.Loading
            try {
                val response = repository.getAllProducts(category)
                _productsState.value = if (response.isSuccessful) {
                    UiState.Success(response.body()!!)
                } else {
                    UiState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _productsState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun addToCart(customerId: Int, productId: Int, quantity: Int) {
        viewModelScope.launch {
            try {
                val request =
                    com.example.project.model.AddToCartRequest(customerId, productId, quantity)
                repository.addToCart(request)
            } catch (e: Exception) {
            }
        }
    }

    fun loadCart(customerId: Int) {
        viewModelScope.launch {
            _cartState.value = UiState.Loading
            try {
                val response = repository.getCart(customerId)
                _cartState.value = if (response.isSuccessful) {
                    UiState.Success(response.body()!!)
                } else {
                    UiState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _cartState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun completeOrder(customerId: Int) {
        _completeOrderState.value = UiState.Loading
        viewModelScope.launch {
            try {
                repository.completeOrder(customerId)
                _completeOrderState.value = UiState.Success("Order Completed")
            } catch (e: Exception) {
                _completeOrderState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}