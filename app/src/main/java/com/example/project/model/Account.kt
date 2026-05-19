package com.example.project.model

import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("account_id")       val accountId: Int? = null,
    @SerializedName("account_name")     val accountName: String,
    @SerializedName("account_type")     val accountType: Int,
    @SerializedName("account_password") val accountPassword: String? = null,
    @SerializedName("account_balance")  val accountBalance: Double? = null,
    @SerializedName("vendor_id")        val vendorId: Int? = null,
    @SerializedName("customer_id")      val customerId: Int? = null
)