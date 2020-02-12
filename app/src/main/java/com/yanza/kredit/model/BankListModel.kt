package com.yanza.kredit.model


import com.google.gson.annotations.SerializedName

data class BankListModel(
    @SerializedName("data")
    val `data`: List<BankData?>?,
    @SerializedName("status")
    val status: Boolean?
)

data class BankData(
    @SerializedName("code")
    val code: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)