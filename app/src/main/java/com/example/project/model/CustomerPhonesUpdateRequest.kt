package com.example.project.model

import com.google.gson.annotations.SerializedName

data class CustomerPhonesUpdateRequest(
    @SerializedName("customer_id")       val customerId: Int,
    @SerializedName("phones")          val phones: List<String>,
    @SerializedName("phones_to_delete") val phonesToDelete: List<String>
)