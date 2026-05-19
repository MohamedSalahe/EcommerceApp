package com.example.project.model

import com.google.gson.annotations.SerializedName

data class CustomerUpdateRequest(
    @SerializedName("customer_id")     val customerId: Int,
    @SerializedName("customer_name")   val customerName: String,
    @SerializedName("customer_state")  val customerState: String,
    @SerializedName("customer_city")   val customerCity: String,
    @SerializedName("customer_street") val customerStreet: String
)
