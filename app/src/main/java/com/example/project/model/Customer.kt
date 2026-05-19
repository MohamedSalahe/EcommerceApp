package com.example.project.model

import com.google.gson.annotations.SerializedName

data class Customer(
    @SerializedName("customer_id")     val customerId: Int? = null,
    @SerializedName("customer_name")   val customerName: String? = null,
    @SerializedName("customer_state")  val customerState: String? = null,
    @SerializedName("customer_city")   val customerCity: String? = null,
    @SerializedName("customer_street") val customerStreet: String? = null,
    @SerializedName("join_date")       val joinDate: String? = null,
    @SerializedName("customer_phones") val customerPhones: List<String>? = null,
    @SerializedName("order_count")     val orderCount: Int? = null,
    @SerializedName("item_count")      val itemCount: Int? = null,
    @SerializedName("total_spent")     val totalSpent: String? = null
)