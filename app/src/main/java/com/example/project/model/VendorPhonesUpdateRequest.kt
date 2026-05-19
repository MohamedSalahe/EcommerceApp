package com.example.project.model

import com.google.gson.annotations.SerializedName

data class VendorPhonesUpdateRequest(
    @SerializedName("vendor_id")       val vendorId: Int,
    @SerializedName("phones")          val phones: List<String>,
    @SerializedName("phones_to_delete") val phonesToDelete: List<String>
)