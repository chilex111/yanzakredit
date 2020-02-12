package com.yanza.kredit.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HistoryModel (
    @SerializedName("data")
    val `data`: List<History>?,
    @SerializedName("status")
    val status: Boolean?
)


class History (
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("interest")
    val interest: Double?,
    @SerializedName("paybackdate")
    val paybackdate: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("totalpayback")
    val totalpayback: Double?
)