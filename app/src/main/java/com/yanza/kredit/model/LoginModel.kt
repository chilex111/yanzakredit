package com.yanza.kredit.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginModel (
    @SerializedName("data")
    val `data`: LoginData?,
    @SerializedName("msg")
    val msg: String?,
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("token")
    val token: String?
)

data class LoginData(
    @SerializedName("address")
    val address: String?,
    @SerializedName("dob")
    val dob: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("firstname")
    val firstname: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("lastname")
    val lastname: String?,
    @SerializedName("office_address")
    val officeAddress: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("user_id")
    val userId: Int?
)


data class Interest(
    @SerializedName("data")
    val `data`: InterestData?,
    @SerializedName("status")
    val status: Boolean?
)

data class InterestData(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("rate")
    val rate: Double?
)