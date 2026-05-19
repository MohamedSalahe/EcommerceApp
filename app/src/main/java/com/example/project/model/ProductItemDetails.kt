package com.example.project.model

import com.google.gson.annotations.SerializedName

data class ProductItemDetails(
    @SerializedName("name")        val name: String,
    @SerializedName("rating")      val rating: String,
    @SerializedName("stock")       val stock: String,
    @SerializedName("productID")   val productID: String,
    @SerializedName("image")       val image: String,
    @SerializedName("price")       val price: String,
    @SerializedName("description") val description: String,
    @SerializedName("category")    val category: String,
    @SerializedName("views")       val views: String,
    @SerializedName("date")        val date: String,
    @SerializedName("subcategory") val subcategory: String,
    @SerializedName("vendorID")    val vendorID: String? = null
)

