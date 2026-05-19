package com.example.project.model

import com.google.gson.annotations.SerializedName

data class VendorUpdateRequest(
    @SerializedName("vendor_id")     val vendorId: Int,
    @SerializedName("vendor_name")   val vendorName: String,
    @SerializedName("vendor_owner")  val vendorOwner: String,
    @SerializedName("vendor_state")  val vendorState: String,
    @SerializedName("vendor_city")   val vendorCity: String,
    @SerializedName("vendor_street") val vendorStreet: String
)