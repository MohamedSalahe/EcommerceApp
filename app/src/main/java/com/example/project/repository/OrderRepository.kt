package com.example.project.repository

import com.example.project.network.RetrofitClient

class OrderRepository {

    private val api = RetrofitClient.apiService


    suspend fun getCustomerOrder(customerId: Int) =
        api.getCustomerOrders(customerId)
    suspend fun getVendorOrder(vendorId: Int) =
        api.getVendorOrders(vendorId)
    suspend fun getCustomerOrderDetails(orderNo: String?) =
        api.getCustomerOrderDetails(orderNo)
    suspend fun getVendorOrderDetails(orderNo: String?, vendorId: Int) =
        api.getOrderDetails(orderNo, vendorId)
}