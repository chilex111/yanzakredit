package com.yanza.kredit.model


import com.google.gson.annotations.SerializedName

data class BVNModel(
    @SerializedName("data")
    val `data`: BVNData?,
    @SerializedName("msg")
    val msg: String?,
    @SerializedName("status")
    val status: Boolean?
)


data class BVNData(
    @SerializedName("dob")
    val dob: String?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("formatted_dob")
    val formattedDob: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("phone")
    val phone: String?
)