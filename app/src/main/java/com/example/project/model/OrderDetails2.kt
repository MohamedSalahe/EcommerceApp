package com.example.project.model

import com.google.gson.annotations.SerializedName

data class OrderDetails2(
    @SerializedName("orderItem")     val orderItem: String,
    @SerializedName("orderPrices")    val itemPrice: String,
    @SerializedName("orderCounts")    val itemCount: String,
    @SerializedName("productId") val productId: String
)
