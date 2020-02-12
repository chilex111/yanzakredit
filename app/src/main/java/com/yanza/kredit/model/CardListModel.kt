package com.yanza.kredit.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CardListModel(
    @SerializedName("data")
    val `data`: List<CardsData>?,
    @SerializedName("status")
    val status: Boolean?
)


data class CardsData(
    @SerializedName("auth_code")
    val authCode: String?,
    @SerializedName("bank")
    val bank: String?,
    @SerializedName("expiry_month")
    val expiryMonth: String?,
    @SerializedName("expiry_year")
    val expiryYear: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("number")
    val number: String?
)