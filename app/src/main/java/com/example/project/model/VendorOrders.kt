package com.example.project.model

import com.google.gson.annotations.SerializedName

data class VendorOrders(
    @SerializedName("vendorOrders") val vendorOrders: List<String>? = null,
    @SerializedName("vendorId") val vendorId: Int? = null,
    @SerializedName("productCount") val productCount: List<String>? = null,
    @SerializedName("price") val price: List<String>? = null,
    @SerializedName("date") val date: List<String>? = null,
    @SerializedName("orderID") val orderID: List<String>? = null
)
