package com.example.project.model

import com.google.gson.annotations.SerializedName

data class CartItem(
    @SerializedName("product_id")    val productId: String,
    @SerializedName("name")          val name: String,
    @SerializedName("price")         val price: Double,
    @SerializedName("stock")         val stock: Int,
    @SerializedName("image_url")     val imageUrl: String,
    @SerializedName("cart_quantity") val cartQuantity: Int
)
