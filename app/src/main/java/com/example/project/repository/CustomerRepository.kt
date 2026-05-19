package com.example.project.repository

import com.example.project.model.CustomerPhonesUpdateRequest
import com.example.project.model.CustomerUpdateRequest
import com.example.project.network.RetrofitClient

class CustomerRepository {

    private val api = RetrofitClient.apiService

    suspend fun getCustomer(customerId: Int) =
        api.getCustomer(customerId)

    suspend fun updateCustomer(request: CustomerUpdateRequest) =
        api.updateCustomer(request)

    suspend fun updateCustomerPhones(request: CustomerPhonesUpdateRequest) =
        api.updateCustomerPhones(request)

    suspend fun getCustomerOrders(customerId: Int) =
        api.getCustomerOrders(customerId)

    suspend fun deleteCustomerOrders(orderNo: String?, customerId: Int) =
        api.deleteCustomerOrders(orderNo, customerId)

    suspend fun getAllProducts(category: String) =
        api.getAllProducts(category)

    suspend fun addToCart(request: com.example.project.model.AddToCartRequest) =
        api.addToCart(request)

    suspend fun getCart(customerId: Int) =
        api.getCart(customerId)

    suspend fun completeOrder(customerId: Int) =
        api.completeOrder(mapOf("customer_id" to customerId))

    suspend fun getCustomerOrderDetails(orderNo: String?) =
        api.getCustomerOrderDetails(orderNo)
}