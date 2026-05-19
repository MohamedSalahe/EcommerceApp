package com.example.project.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("account_name")     val accountName: String,
    @SerializedName("account_password") val accountPassword: String,
    @SerializedName("account_type")     val accountType: Int,
    @SerializedName("customer_name")    val customerName: String? = null,
    @SerializedName("vendor_name")      val vendorName: String? = null,
    @SerializedName("vendor_owner")     val vendorOwner: String? = null
)