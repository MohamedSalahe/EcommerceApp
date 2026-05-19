package com.example.project.model

import com.google.gson.annotations.SerializedName

data class AddProductRequest(
    @SerializedName("vendor_id") val vendorId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("subcategory") val subcategory: String,
    @SerializedName("price") val price: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("image") val imageBase64: String
)
