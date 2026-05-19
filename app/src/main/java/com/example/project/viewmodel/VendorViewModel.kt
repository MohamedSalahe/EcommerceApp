package com.example.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.model.Vendor
import com.example.project.model.VendorOrders
import com.example.project.model.VendorPhonesUpdateRequest
import com.example.project.model.VendorUpdateRequest
import com.example.project.model.AddProductRequest
import com.example.project.model.ProductItemDetails
import com.example.project.repository.VendorRepository
import com.example.project.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.project.model.Orderdetails
import android.util.Base64
import java.io.File

class VendorViewModel : ViewModel() {

    private val repository = VendorRepository()
    private val orderRepository = OrderRepository()

    private val _vendorState =
        MutableStateFlow<UiState<Vendor>>(UiState.Loading)

    val vendorState: StateFlow<UiState<Vendor>> = _vendorState

    private val _updateVendorState =
        MutableStateFlow<UiState<Vendor>>(UiState.Loading)

    val updateVendorState: StateFlow<UiState<Vendor>> =
        _updateVendorState

    private val _vendorOrdersState =
        MutableStateFlow<UiState<VendorOrders>>(UiState.Loading)

    val vendorOrdersState: StateFlow<UiState<VendorOrders>> =
        _vendorOrdersState

    private val _vendorProductsState =
        MutableStateFlow<UiState<List<ProductItemDetails>>>(UiState.Loading)

    val vendorProductsState: StateFlow<UiState<List<ProductItemDetails>>> =
        _vendorProductsState

    private val _orderDetailsState =
        MutableStateFlow<UiState<Orderdetails>>(UiState.Loading)

    val orderDetailsState: StateFlow<UiState<Orderdetails>> =
        _orderDetailsState

    private val _deleteOrderState =
        MutableStateFlow<UiState<String>>(UiState.Loading)

    val deleteOrderState: StateFlow<UiState<String>> =
        _deleteOrderState

    private val _addProductState =
        MutableStateFlow<UiState<String>>(UiState.Loading)

    val addProductState: StateFlow<UiState<String>> =
        _addProductState

    private val _updateProductState =
        MutableStateFlow<UiState<String>>(UiState.Loading)

    val updateProductState: StateFlow<UiState<String>> =
        _updateProductState

    private val _deleteProductState =
        MutableStateFlow<UiState<String>>(UiState.Loading)

    val deleteProductState: StateFlow<UiState<String>> =
        _deleteProductState

    fun getVendor(vendorId: Int) {

        _vendorState.value = UiState.Loading

        viewModelScope.launch {

            try {

                val response = repository.getVendor(vendorId)

                if (response.isSuccessful && response.body() != null) {

                    _vendorState.value =
                        UiState.Success(response.body()!!)

                } else {

                    _vendorState.value =
                        UiState.Error("Failed")
                }

            } catch (e: Exception) {

                _vendorState.value =
                    UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun updateVendor(request: VendorUpdateRequest) {

        _updateVendorState.value = UiState.Loading

        viewModelScope.launch {

            try {

                val response = repository.updateVendor(request)

                if (response.isSuccessful && response.body() != null) {

                    _updateVendorState.value =
                        UiState.Success(response.body()!!)

                } else {

                    _updateVendorState.value =
                        UiState.Error("Update Failed")
                }

            } catch (e: Exception) {

                _updateVendorState.value =
                    UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun updateVendorPhones(request: VendorPhonesUpdateRequest) {

        _updateVendorState.value = UiState.Loading

        viewModelScope.launch {

            try {

                val response = repository.updateVendorPhones(request)

                if (response.isSuccessful && response.body() != null) {

                    _updateVendorState.value =
                        UiState.Success(response.body()!!)

                } else {

                    _updateVendorState.value =
                        UiState.Error("Phone Update Failed")
                }

            } catch (e: Exception) {

                _updateVendorState.value =
                    UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun getVendorOrders(vendorId: Int) {

        _vendorOrdersState.value = UiState.Loading

        viewModelScope.launch {

            try {

                val response = repository.getVendorOrders(vendorId)

                if (response.isSuccessful && response.body() != null) {

                    _vendorOrdersState.value =
                        UiState.Success(response.body()!!)

                } else {

                    _vendorOrdersState.value =
                        UiState.Error("Failed")
                }

            } catch (e: Exception) {

                _vendorOrdersState.value =
                    UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun getVendorProducts(vendorId: Int) {

        _vendorProductsState.value = UiState.Loading

        viewModelScope.launch {

            try {

                val response = repository.getVendorProducts(vendorId)

                if (response.isSuccessful && response.body() != null) {

                    _vendorProductsState.value =
                        UiState.Success(response.body()!!)

                } else {

                    _vendorProductsState.value =
                        UiState.Error("Failed")
                }

            } catch (e: Exception) {

                _vendorProductsState.value =
                    UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }


    fun saveVendor(
        vendorId: Int,
        name: String,
        owner: String,
        state: String,
        city: String,
        street: String,
        phones: List<String>,
        phonesToDelete: List<String>
    ) {

        viewModelScope.launch {

            _updateVendorState.value = UiState.Loading

            try {

                val vendorResponse = repository.updateVendor(
                    VendorUpdateRequest(
                        vendorId = vendorId,
                        vendorName = name,
                        vendorOwner = owner,
                        vendorState = state,
                        vendorCity = city,
                        vendorStreet = street
                    )
                )

                if (!vendorResponse.isSuccessful) {
                    _updateVendorState.value =
                        UiState.Error("Failed to update vendor")
                    return@launch
                }

                val phoneResponse = repository.updateVendorPhones(
                    VendorPhonesUpdateRequest(
                        vendorId = vendorId,
                        phones = phones,
                        phonesToDelete = phonesToDelete
                    )
                )

                if (!phoneResponse.isSuccessful) {
                    _updateVendorState.value =
                        UiState.Error("Failed to update phones")
                    return@launch
                }

                _updateVendorState.value =
                    UiState.Success(vendorResponse.body()!!)
            } catch (e: Exception) {

                _updateVendorState.value =
                    UiState.Error(e.message ?: "Network error")
            }
        }
    }

    fun loadVendor(vendorId: Int) {

        viewModelScope.launch {

            _vendorState.value = UiState.Loading

            try {

                val response = repository.getVendor(vendorId)

                if (response.isSuccessful && response.body() != null) {

                    _vendorState.value =
                        UiState.Success(response.body()!!)

                } else {

                    _vendorState.value =
                        UiState.Error("Failed to load vendor")
                }

            } catch (e: Exception) {

                _vendorState.value =
                    UiState.Error(e.message ?: "Network error")
            }
        }
    }

    fun deleteVendorOrders(orderNo: String?, vendorId: Int) {

        viewModelScope.launch {

            _deleteOrderState.value = UiState.Loading

            try {

                repository.deleteVendorOrders(orderNo, vendorId)

                _deleteOrderState.value =
                    UiState.Success(orderNo ?: "")

            } catch (e: Exception) {

                _deleteOrderState.value =
                    UiState.Error(e.message ?: "Failed to delete order")
            }
        }
    }

    fun getOrderDetails(orderId: String?, vendorId: Int) {
        _orderDetailsState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = orderRepository.getVendorOrderDetails(orderId, vendorId)
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

    fun addProduct(vendorId: Int, name: String, description: String, category: String, subcategory: String, price: Double, stock: Int, imageFile: File) {
        _addProductState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val bytes = imageFile.readBytes()
                val base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)

                val request = AddProductRequest(
                    vendorId = vendorId,
                    name = name,
                    description = description,
                    category = category,
                    subcategory = subcategory,
                    price = price,
                    stock = stock,
                    imageBase64 = base64Image
                )

                val response = repository.addProduct(request)

                if (response.isSuccessful) {
                    _addProductState.value = UiState.Success("Product added successfully")
                } else {
                    _addProductState.value = UiState.Error("Failed to add product")
                }
            } catch (e: Exception) {
                _addProductState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun updateProduct(productId: Int, name: String, description: String, category: String, subcategory: String, price: Double, stock: Int, imageFile: File?) {
        _updateProductState.value = UiState.Loading

        viewModelScope.launch {
            try {
                var base64Image: String? = null
                imageFile?.let {
                    val bytes = it.readBytes()
                    base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)
                }

                val request = com.example.project.model.UpdateProductRequest(
                    productId = productId,
                    name = name,
                    description = description,
                    category = category,
                    subcategory = subcategory,
                    price = price,
                    stock = stock,
                    imageBase64 = base64Image
                )


                repository.updateProduct(request)
                _updateProductState.value = UiState.Success("Product updated successfully")
            } catch (e: Exception) {
                _updateProductState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
    fun deleteVendorProduct(productId: Int) {
        _deleteProductState.value = UiState.Loading
        viewModelScope.launch {
            try {
                repository.deleteVendorProduct(productId)
                _deleteProductState.value = UiState.Success("Product deleted successfully")
            } catch (e: Exception) {
                _deleteProductState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}