package com.example.project.model

import com.google.gson.annotations.SerializedName

data class AddToCartRequest(
    @SerializedName("customer_id") val customerId: Int,
    @SerializedName("product_id") val productId: Int,
    @SerializedName("quantity") val quantity: Int
)
