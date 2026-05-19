package com.example.project.model

import com.google.gson.annotations.SerializedName

data class Orderdetails(
    @SerializedName("customer_name")  val customerName: String? = null,
    @SerializedName("customer_state") val customerState: String? = null,
    @SerializedName("customer_city")  val customerCity: String? = null,
    @SerializedName("vendor_name")    val vendorName: String? = null,
    @SerializedName("orderItems")     val orderItems: List<String>? = null,
    @SerializedName("orderDate")      val orderDate: String? = null,
    @SerializedName("total")          val total: String? = null,
    @SerializedName("orderPrices")    val orderPrices: List<String>? = null,
    @SerializedName("orderCounts")    val orderCounts: List<String>? = null,
    @SerializedName("productId") val productId: List<String>? = null
)
