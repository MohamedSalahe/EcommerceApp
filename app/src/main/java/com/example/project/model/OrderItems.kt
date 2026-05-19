package com.example.project.model

import com.google.gson.annotations.SerializedName

data class OrderItems(
    @SerializedName("productCount") val productCount: String,
    @SerializedName("price") val price: String,
    @SerializedName("date") val date: String,
    @SerializedName("orderID") val orderID: String
)
