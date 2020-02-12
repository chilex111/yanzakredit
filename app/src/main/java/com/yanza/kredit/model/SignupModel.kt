package com.yanza.kredit.model


import com.google.gson.annotations.SerializedName

data class SignupModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("msg")
    val msg: String?

)

data class Data(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("token")
    val otp: String?
)