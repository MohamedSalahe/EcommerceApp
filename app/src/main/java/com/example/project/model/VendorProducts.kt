package com.example.project.model

import com.google.gson.annotations.SerializedName

data class VendorProducts(
    @SerializedName("vendorProducts") val vendorProducts: List<String>? = null,
    @SerializedName("vendorId") val vendorId: Int? = null,
    @SerializedName("productStock") val productStock: List<String>? = null,
    @SerializedName("prices") val prices: List<String>? = null,
    @SerializedName("dates") val dates: List<String>? = null,
    @SerializedName("ratings") val ratings: List<String>? = null,
    @SerializedName("descriptions") val descriptions: List<String>? = null,
    @SerializedName("views") val views: List<String>? = null,
    @SerializedName("stock") val stock: List<String>? = null,
    @SerializedName("subcategories") val subcategories: List<String>? = null,
    @SerializedName("categories") val categories: List<String>? = null,
    @SerializedName("productID") val productIDs: List<String>? = null,
    @SerializedName("images") val images: List<String>? = null
)


