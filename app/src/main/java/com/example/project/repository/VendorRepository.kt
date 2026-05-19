package com.example.project.repository

import com.example.project.model.VendorPhonesUpdateRequest
import com.example.project.model.VendorUpdateRequest
import com.example.project.model.AddProductRequest
import com.example.project.network.RetrofitClient

class VendorRepository {

    private val api = RetrofitClient.apiService

    suspend fun getVendor(vendorId: Int) =
        api.getVendor(vendorId)

    suspend fun updateVendor(request: VendorUpdateRequest) =
        api.updateVendor(request)

    suspend fun updateVendorPhones(request: VendorPhonesUpdateRequest) =
        api.updateVendorPhones(request)

    suspend fun getVendorOrders(vendorId: Int) =
        api.getVendorOrders(vendorId)

    suspend fun getVendorProducts(vendorId: Int) =
        api.getVendorProducts(vendorId)

    suspend fun deleteVendorOrders(orderNo: String?,vendorId: Int) =
        api.deleteVendorOrders(orderNo,vendorId)

    suspend fun addProduct(request: AddProductRequest) =
        api.addProduct(request)

    suspend fun updateProduct(request: com.example.project.model.UpdateProductRequest) =
        api.updateProduct(request)


    suspend fun deleteVendorProduct(productId: Int) =
        api.deleteVendorProduct(productId)
}
