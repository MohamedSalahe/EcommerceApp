package com.example.project.model

import com.google.gson.annotations.SerializedName

data class Vendor(
    @SerializedName("vendor_id")     val vendorId: Int? = null,
    @SerializedName("vendor_name")   val vendorName: String? = null,
    @SerializedName("vendor_owner")  val vendorOwner: String? = null,
    @SerializedName("vendor_state")  val vendorState: String? = null,
    @SerializedName("vendor_city")   val vendorCity: String? = null,
    @SerializedName("vendor_street") val vendorStreet: String? = null,
    @SerializedName("order_count") val orderCount: Int? = null,
    @SerializedName("product_count") val productCount: Int? = null,
    @SerializedName("avg_rating") val avgRating: String? = null,
    @SerializedName("vendor_phones") val vendorPhones: List<String>? = null
)