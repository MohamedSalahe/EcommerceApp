package com.example.project.model

import com.google.gson.annotations.SerializedName

data class CustomerOrders(
    @SerializedName("vendorOrders") val customerOrders: List<String>? = null,
    @SerializedName("vendorId") val customerId: Int? = null,
    @SerializedName("productCount") val productCount: List<String>? = null,
    @SerializedName("price") val price: List<String>? = null,
    @SerializedName("date") val date: List<String>? = null,
    @SerializedName("orderID") val orderID: List<String>? = null
)
